package users.Users;

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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import project.Main;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LogoutTests {

    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;
    private String token;

    @BeforeEach
    public void setup() {
        JSONObject obj = new JSONObject();
        obj.put("email", "abc@gmail.com");
        obj.put("password", "pass1");
        obj.put("username", "lamington");
        assertDoesNotThrow(() -> {
            res = mockMvc
                    .perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                    .andReturn();
            token = JsonPath.read(res.getResponse().getContentAsString(), "$.data.token");
        });
    }

    @Transactional
    @Test
    public void successfulLogout() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(
                    delete("/api/v1/user/logout")
                        .contentType("application/json")
                        .header("Authorization", token)
                )
                .andExpect(status().isOk());
        });
    }

    @Transactional
    @Test
    public void invalidLogout() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(
                    delete("/api/v1/user/logout")
                        .contentType("application/json")
                        .header("Authorization", "4gAjVOL86fRjgBcYPNHOI3-VTfEejw_P")
                )
                .andExpect(status().isBadRequest());
        });
    }
}
