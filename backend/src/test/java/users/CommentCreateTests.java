package users;

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

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import project.Main;
import project.threads.Thread;
import project.threads.ThreadRepository;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentCreateTests {
    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;
    Thread thread;
    @Autowired
    private ThreadRepository threadRepository;

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
        });
        thread = new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                "This is a parliament bill.", true);
        threadRepository.save(thread);
    }

    @Test
    @Transactional
    public void createSimpleComment() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonObj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = jsonObj.get("data").get("token").asText();
            String userId = jsonObj.get("data").get("userId").asText();
            data.put("token", token);
            data.put("userId", userId);
            data.put("threadId", thread.getId().toString());
            data.put("parentComment", null);
            data.put("text", "God this bill is terrible!");

            mockMvc.perform(
                    post("/api/v1/comments/create").contentType("application/json").content(data.toJSONString())
                            .header("Authorization", token))
                    .andExpect(status().isOk());
        });
    }

    @Test
    @Transactional
    public void createChainedComments() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonObj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = jsonObj.get("data").get("token").asText();
            String userId = jsonObj.get("data").get("userId").asText();
            data.put("token", token);
            data.put("userId", userId);
            data.put("threadId", thread.getId().toString());
            data.put("parentComment", null);
            data.put("text", "God this bill is terrible!");
            MvcResult commentRes = mockMvc.perform(
                    post("/api/v1/comments").contentType("application/json").content(data.toJSONString())
                            .header("Authorization", token))
                    .andExpect(status().isOk()).andReturn();

            jsonObj = objectMapper.readTree(commentRes.getResponse().getContentAsString());
            String parentCommentId = jsonObj.get("data").get("commentId").asText();
            data.put("parentComment", parentCommentId);
            data.put("text", "No it isnt!");
            mockMvc.perform(
                    post("/api/v1/comments").contentType("application/json").content(data.toJSONString())
                            .header("Authorization", token))
                    .andExpect(status().isOk());

            data.put("text", "Your opinion is wrong!");
            commentRes = mockMvc.perform(
                    post("/api/v1/comments").contentType("application/json").content(data.toJSONString())
                            .header("Authorization", token))
                    .andExpect(status().isOk()).andReturn();

            jsonObj = objectMapper.readTree(commentRes.getResponse().getContentAsString());
            String parentCommentId2 = jsonObj.get("data").get("commentId").asText();
            data.put("parentComment", parentCommentId2);
            data.put("text", "No, your opinion is wrong!");
            mockMvc.perform(
                    post("/api/v1/comments").contentType("application/json").content(data.toJSONString())
                            .header("Authorization", token))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/comments?threadId=" + thread.getId().toString())
                    .header("Authorization", token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[*].text",
                            Matchers.containsInAnyOrder("God this bill is terrible!", "No it isnt!",
                                    "Your opinion is wrong!", "No, your opinion is wrong!")))
                    .andExpect(jsonPath("$.data[*].parentCommentId",
                            Matchers.containsInAnyOrder(null, parentCommentId, parentCommentId, parentCommentId2)))
                    .andExpect(jsonPath("$.data[*].creatorId",
                            Matchers.containsInAnyOrder(userId, userId, userId, userId)));
        });
    }
}
