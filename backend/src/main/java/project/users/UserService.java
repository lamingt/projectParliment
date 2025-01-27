package project.users;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project.utils.PasswordUtils;
import project.utils.ResponseDto;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public ResponseDto registerUser(String email, String password, String username) throws IllegalArgumentException {
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

        return new ResponseDto("User registered successfully.", token);
    }
}
