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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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
    private String userId;
    private User user;
    private String token;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
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
            user = userRepository.findById(UUID.fromString(userId)).get();

            thread = new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                    "This is a parliament bill.", true);
            threadRepository.save(thread);
            comment1 = new Comment(thread, user, "This is the first comment.", null);
            commentRepository.save(comment1);
            java.lang.Thread.sleep(500);
            comment2 = new Comment(thread, user, "This is the second comment.", null);
            commentRepository.save(comment2);
            java.lang.Thread.sleep(500);
            comment3 = new Comment(thread, user, "This is the third comment.", null);
            commentRepository.save(comment3);
            commentRepository.save(new Comment(thread, user, "This is a reply.", comment3));
        });

    }

    @Test
    @Transactional
    public void getRootCommentsOrderedByTime() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/api/v1/comments/root?pageNum=1&sort=recent&threadId=" + thread.getId().toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentInfo[*].text",
                    Matchers.contains(
                        "This is the third comment.",
                        "This is the second comment.",
                        "This is the first comment."
                    )));

            mockMvc.perform(get("/api/v1/comments/root?pageNum=1&sort=oldest&threadId=" + thread.getId().toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentInfo[*].text",
                    Matchers.contains(
                        "This is the first comment.",
                        "This is the second comment.",
                        "This is the third comment."
                    )));
        });
    }

    @Test
    @Transactional
    public void getRootCommentsOrderedByLikes() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();
            data.put("commentId", comment3.getId().toString());

            mockMvc.perform(post("/api/v1/comments/like").contentType("application/json").header("Authorization", token)
                .content(data.toJSONString()))
                .andExpect(status().isOk());
            
            data = new JSONObject();
            data.put("commentId", comment1.getId().toString());
            mockMvc.perform(
                        post("/api/v1/comments/dislike")
                            .contentType("application/json")
                            .header("Authorization", token)
                            .content(data.toJSONString())
                    )
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/comments/root?pageNum=1&sort=likes&threadId=" + thread.getId().toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentInfo[*].text",
                    Matchers.contains(
                        "This is the third comment.",
                        "This is the second comment.",
                        "This is the first comment."
                    )));
        });
    }
}
