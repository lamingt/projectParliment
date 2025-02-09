package users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import project.Main;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class ThreadListTests {

    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;

    @BeforeEach
    private void setup() {
        String projectRootPath = new File("../").getAbsolutePath();  // This will point to the current working directory
        System.setProperty("user.dir", projectRootPath);
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            res = mockMvc
                    .perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isOk()).andReturn();
        });
    }

    @Transactional
    @Test
    public void successfulList() {
        assertDoesNotThrow(() -> {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode obj = objectMapper.readTree(res.getResponse().getContentAsString());

            String token = obj.get("data").get("token").asText();
            mockMvc.perform(get("/api/v1/threads/list?pageNum=1").header("Authorization", token))
                    .andExpect(status().isOk());
        });
    }

}
