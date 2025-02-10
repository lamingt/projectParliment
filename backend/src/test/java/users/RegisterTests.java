package users;

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
public class RegisterTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void succesfulRegistration() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isOk());
        });
    }

    @Test
    @Transactional
    public void passwordTooShort() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    @Transactional
    public void passwordNoAlphanumeric() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "..........");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    @Transactional
    public void usernameTaken() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "passssssss123");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().is(200));
        });

        obj.put("email", "123@gmail.com");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    @Transactional
    public void emailTaken() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "passssssss123");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().is(200));
        });
        obj.put("password", "password123");
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }

    @Test
    @Transactional
    public void usernameTooLong() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "passssssss");
        obj.put("username", "1".repeat(21));
        assertDoesNotThrow(() -> {
            mockMvc.perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andExpect(status().isBadRequest());
        });
    }
}
