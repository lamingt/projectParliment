package users.Comments;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

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

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import project.Main;
import project.threads.Thread;
import project.threads.ThreadRepository;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentDislikeTest {
    @Autowired
    private MockMvc mockMvc;
    private String commentId;
    private String token;
    private String userId;
    private MvcResult res;
    @Autowired
    private ThreadRepository threadRepository;
    private Thread thread;

    @BeforeEach
    private void setup() {
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
        });

        thread = new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                "This is a parliament bill.", true);
        threadRepository.save(thread);

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
            commentId = JsonPath.read(commentRes.getResponse().getContentAsString(), "$.data.commentId");
        });
    }

    @Transactional
    @Test
    public void invalidToken() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();
            data.put("commentId", commentId);
            data.put("userId", userId);
            mockMvc.perform(
                    post("/api/v1/comments/dislike").contentType("application/json").header("Authorization", "123")
                            .content(data.toJSONString()))
                    .andExpect(status().isForbidden());
        });
    }

    @Transactional
    @Test
    public void successfulDislike() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();
            data.put("commentId", commentId);
            data.put("userId", userId);
            mockMvc.perform(
                    post("/api/v1/comments/dislike").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/comments?threadId=" + thread.getId().toString()).header("Authorization",
                            token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].dislikedBy[0]").value(userId));
        });
    }

    @Transactional
    @Test
    public void successfulUndislike() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();
            data.put("commentId", commentId);
            data.put("userId", userId);
            mockMvc.perform(
                    post("/api/v1/comments/dislike").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    post("/api/v1/comments/dislike").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/comments?threadId=" + thread.getId().toString()).header("Authorization",
                            token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].dislikedBy", Matchers.empty()));
        });
    }

    @Transactional
    @Test
    public void dislikingLikedComment() {
        assertDoesNotThrow(() -> {
            JSONObject data = new JSONObject();
            data.put("commentId", commentId);
            data.put("userId", userId);
            mockMvc.perform(
                    post("/api/v1/comments/like").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    post("/api/v1/comments/dislike").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/comments?threadId=" + thread.getId().toString()).header("Authorization",
                            token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].dislikedBy[0]").value(userId));
            mockMvc.perform(
                    get("/api/v1/comments?threadId=" + thread.getId().toString()).header("Authorization",
                            token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].likedBy", Matchers.empty()));
        });
    }
}
