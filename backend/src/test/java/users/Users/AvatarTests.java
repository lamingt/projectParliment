package users.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.jayway.jsonpath.JsonPath;

import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;
import project.Main;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@SpringBootTest(classes = Main.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AvatarTests {
    @Autowired
    private MockMvc mockMvc;
    private MvcResult res;
    private String token;
    private UUID userId;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> {
            JSONObject obj = new JSONObject();
            obj.put("email", "abc@gmail.com");
            obj.put("password", "pass1");
            obj.put("username", "lamington");
            assertDoesNotThrow(() -> {
                res = mockMvc
                        .perform(post("/api/v1/user/register").contentType("application/json").content(obj.toJSONString()))
                        .andExpect(status().isOk()).andReturn();
                token = JsonPath.read(res.getResponse().getContentAsString(), "$.data.token");
                userId = UUID.fromString(JsonPath.read(res.getResponse().getContentAsString(), "$.data.userId"));
            });
        });
    }

    @Test
    @Transactional
    public void getAndSetAvatar() {
        byte[] expected = "HELLO!!!!".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", expected);
        JSONObject data = new JSONObject();
        data.put("file", file);

        assertDoesNotThrow(() -> {
            mockMvc.perform(multipart("/api/v1/user/avatar").file(file).header("Authorization", token))
                .andExpect(status().isOk());
            
            mockMvc.perform(get("/api/v1/user/avatar/" + userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().bytes(expected));
        });
    }

    @Test
    @Transactional
    public void getDefaultAvatar() {
        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/api/v1/user/avatar/" + userId.toString()))
                .andExpect(status().isOk());
        });
    }
}
