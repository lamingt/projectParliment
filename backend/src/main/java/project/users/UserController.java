package project.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.utils.ResponseDto;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> postMethodName(@RequestBody User user) {
        try {
            return new ResponseEntity<ResponseDto>(
                    userService.registerUser(user.getEmail(), user.getPassword(), user.getUsername()),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }
}
