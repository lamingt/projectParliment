package users.Threads;

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

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import project.Main;
import project.threads.Thread;
import project.threads.ThreadRepository;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LikeThreadTest {
    @Autowired
    private MockMvc mockMvc;
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
        });

        thread = new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                "This is a parliament bill.", true);
        threadRepository.save(thread);
    }

    @Transactional
    @Test
    public void invalidToken() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String userId = obj.get("data").get("userId").asText();
            JSONObject data = new JSONObject();
            data.put("threadId", thread.getId().toString());
            data.put("userId", userId);
            mockMvc.perform(post("/api/v1/threads/like").contentType("application/json").header("Authorization", "123")
                    .content(data.toJSONString())).andExpect(status().isForbidden());
        });
    }

    @Transactional
    @Test
    public void successfulLike() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            String userId = obj.get("data").get("userId").asText();
            JSONObject data = new JSONObject();
            data.put("threadId", thread.getId().toString());

            mockMvc.perform(post("/api/v1/threads/like").contentType("application/json").header("Authorization", token)
                    .content(data.toJSONString())).andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/threads/thread?threadId=" + thread.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.likedBy[0]").value(userId));
        });
    }

    @Transactional
    @Test
    public void successfulUnLike() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            JSONObject data = new JSONObject();
            data.put("threadId", thread.getId().toString());

            mockMvc.perform(post("/api/v1/threads/like").contentType("application/json").header("Authorization", token)
                    .content(data.toJSONString())).andExpect(status().isOk());
            mockMvc.perform(post("/api/v1/threads/like").contentType("application/json").header("Authorization", token)
                    .content(data.toJSONString())).andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/threads/thread?threadId=" + thread.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.likedBy", Matchers.empty()));
        });
    }

    @Transactional
    @Test
    public void likingDislikedThread() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            String userId = obj.get("data").get("userId").asText();
            JSONObject data = new JSONObject();
            data.put("threadId", thread.getId().toString());

            mockMvc.perform(
                    post("/api/v1/threads/dislike").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    post("/api/v1/threads/like").contentType("application/json").header("Authorization", token)
                            .content(data.toJSONString()))
                    .andExpect(status().isOk());
            mockMvc.perform(
                    get("/api/v1/threads/thread?threadId=" + thread.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.likedBy[0]").value(userId));
            mockMvc.perform(
                    get("/api/v1/threads/thread?threadId=" + thread.getId().toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.dislikedBy", Matchers.empty()));
        });
    }
}
