package project.users;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import project.utils.PasswordUtils;
import project.dto.LoginDto;
import project.dto.RegisterDto;
import project.dto.ResponseDto;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
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

        return new ResponseDto("User registered successfully.", token);
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

        return new ResponseDto("User logged in successfully.", token);
    }
}
