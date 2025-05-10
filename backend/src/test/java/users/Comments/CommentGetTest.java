package users.Comments;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import ch.qos.logback.core.subst.Token;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import project.Main;
import project.comments.Comment;
import project.comments.CommentRepository;
import project.threads.Thread;
import project.threads.ThreadRepository;
import project.users.User;
import project.users.UserRepository;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentGetTest {
    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;
    private Thread thread;
    private UUID userId;
    private User user;
    private Token token;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            res = mockMvc
                    .perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isOk()).andReturn();
            token = JsonPath.read(res.getResponse().getContentAsString(), "$.data.token");
            userId = JsonPath.read(res.getResponse().getContentAsString(), "$.data.userId");
            user = userRepository.findById(userId).get();
        });
        thread = new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                "This is a parliament bill.", true);
        threadRepository.save(thread);
        commentRepository.save(new Comment(thread, user, "This is the first comment.", null));
        commentRepository.save(new Comment(thread, user, "This is the second comment.", null));
        Comment comment = new Comment(thread, user, "This is the third comment.", null);
        commentRepository.save(comment);
        commentRepository.save(new Comment(thread, user, "This is a reply.", comment));
    }

    @Test
    @Transactional
    public void getRootComments() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/api/v1/comments/root?pageNum=1" + thread.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[*].text", Matchers.containsInAnyOrder("This is the first comment.",
                            "This is the second comment.", "This is the third comment.")));
        });
    }
}
