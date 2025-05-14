package project.users;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import project.utils.AuthUtils;
import project.utils.PasswordUtils;
import project.dto.AvatarUserDto;
import project.dto.LoginDto;
import project.dto.RegisterDto;
import project.dto.ResponseDto;
import project.dto.returns.AvatarUploadReturnDto;
import project.dto.returns.LoginReturnDto;
import project.users.storage.StorageService;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private StorageService storageService;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.storageService = storageService;
    }

    @Transactional
    public ResponseDto registerUser(RegisterDto registerDetails) throws IllegalArgumentException {
        String email = registerDetails.getEmail();
        String password = registerDetails.getPassword();
        String username = registerDetails.getUsername();

        if (userRepository.findUserByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        } else if (userRepository.findUserByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username is already taken.");
        } else if (password.length() < 5) {
            throw new IllegalArgumentException("Password is less than 5 characters long.");
        } else if (!password.matches("^.*[a-zA-Z0-9].*$")) {
            throw new IllegalArgumentException("Password does not have at least one alphanumeric character.");
        } else if (username.length() > 20) {
            throw new IllegalArgumentException("Username is greater than 20 characters.");
        }

        User user = new User(username, PasswordUtils.encode(password), email);
        userRepository.save(user);
        Token token = new Token(user.getId(), LocalDate.now());
        tokenRepository.save(token);

        LoginReturnDto res = new LoginReturnDto(token.getToken(), user.getId());

        return new ResponseDto("User registered successfully.", res);
    }

    @Transactional
    public ResponseDto loginUser(LoginDto loginDetails) {
        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();

        Optional<User> user = userRepository.findUserByEmail(email);
        if (!user.isPresent() || (user.isPresent() && !PasswordUtils.matches(password, user.get().getPassword()))) {
            throw new IllegalArgumentException("Email or password are incorrect.");
        }

        Token token = new Token(user.get().getId(), LocalDate.now());
        tokenRepository.save(token);

        LoginReturnDto res = new LoginReturnDto(token.getToken(), user.get().getId());

        return new ResponseDto("User logged in successfully.", res);
    }

    @Transactional
    public ResponseDto logoutUser(String tokenString) {
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        if (!token.isPresent() || token.get().isExpired()) {
            throw new IllegalArgumentException("Token is invalid.");
        }

        tokenRepository.delete(token.get());
        return new ResponseDto("User logged out successfully.", null);
    }

    @Transactional
    public ResponseDto uploadAvatar(AvatarUserDto avatarDetails, String tokenString) throws IOException {
        MultipartFile file = avatarDetails.getFile();
        User user = AuthUtils.authenticate(tokenRepository, userRepository, tokenString);

        String filename = storageService.store(file, user.getId());

        AvatarUploadReturnDto res = new AvatarUploadReturnDto(filename);

        return new ResponseDto("File uploaded successfully", res);
    }
}
