package users.Threads;

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
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import project.Main;
import project.threads.Thread;
import project.threads.ThreadRepository;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ThreadListTests {

    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;
    @Autowired
    private ThreadRepository threadRepository;

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

        threadRepository.save(new Thread("Parliament Bill", LocalDate.now(), "Senate", "Before Senate",
                "This is a parliament bill.", true));
    }

    @Transactional
    @Test
    public void successfulList() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            mockMvc.perform(get("/api/v1/threads/list?pageNum=1").header("Authorization", token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.pageInfo[0].title").value("Parliament Bill"))
                    .andExpect(jsonPath("$.data.pagination.totalThreads").value(1))
                    .andExpect(jsonPath("$.data.pagination.totalPages").value(1));
        });
    }

    @Transactional
    @Test
    public void invalidPageNum() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            mockMvc.perform(get("/api/v1/threads/list?pageNum=2").header("Authorization", token))
                    .andExpect(status().isBadRequest());
        });
    }

}
