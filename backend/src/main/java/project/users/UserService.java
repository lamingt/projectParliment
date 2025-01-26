package project.users;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public String registerUser(String email, String password, String username) {
        if (userRepository.findUserByEmail(email).isPresent()) {
            // throw exception
        } else if (userRepository.findUserByUsername(username).isPresent()) {
            // throw exception
        } else if (password.length() < 5) {
            // throw password too short exception
        } else if (!password.matches("^.*[a-zA-Z0-9].*$")) {
            // not at least one alphanumeric character
        } else if (username.length() > 20) {
            // throw username too long exception
        }

        // TODO: encrypt passwords
        User user = new User(username, password, email);
        userRepository.save(user);
        Token token = new Token(user.getId(), LocalDate.now());
        tokenRepository.save(token);

        return token.getToken();
    }
}
