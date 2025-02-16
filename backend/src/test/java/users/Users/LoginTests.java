package users.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import project.Main;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()));
        });
    }

    @Transactional
    @Test
    public void successfulLogin() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/login").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isOk());
        });
    }

    @Transactional
    @Test
    public void incorrectEmail() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc1@gmail.com");
        obj.put("password", "pass1");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/login").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }

    @Transactional
    @Test
    public void incorrectPassword() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass2");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/login").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }
}
