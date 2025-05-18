package project.users;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.dto.AvatarUserDto;
import project.dto.LoginDto;
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

    @PostMapping("/avatar")
    public ResponseEntity<ResponseDto> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) {
        AvatarUserDto avatarDetails = new AvatarUserDto(file);
        try {
            return new ResponseEntity<ResponseDto>(userService.uploadAvatar(avatarDetails, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/avatar/{userId}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("userId") UUID userId) {
        try {
            return userService.getAvatar(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @DeleteMapping("/logout")
    public ResponseEntity<ResponseDto> logout(@RequestHeader("Authorization") String token) {
        try {
            return new ResponseEntity<ResponseDto>(userService.logoutUser(token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }
}
