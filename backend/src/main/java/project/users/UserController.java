package project.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.dto.LoginDto;
import project.dto.LogoutDto;
import project.dto.RegisterDto;
import project.dto.ResponseDto;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RegisterDto registerDetails) {
        try {
            return new ResponseEntity<ResponseDto>(userService.registerUser(registerDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDetails) {
        try {
            return new ResponseEntity<ResponseDto>(userService.loginUser(loginDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestBody LogoutDto logoutDetails) {
        try {
            return new ResponseEntity<ResponseDto>(userService.logoutUser(logoutDetails), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }
}
