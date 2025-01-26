package project.users;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @PostMapping("/register")
    public String postMethodName(@RequestBody String username, @RequestBody String password,
            @RequestBody String email) {

        return username;
    }
}
