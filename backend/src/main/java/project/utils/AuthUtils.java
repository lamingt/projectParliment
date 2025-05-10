package project.utils;

import java.util.Optional;

import project.users.Token;
import project.users.TokenRepository;
import project.users.User;
import project.users.UserRepository;

public class AuthUtils {
    public static void validateToken(TokenRepository tokenRepository, String tokenString) {
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        if (!token.isPresent() || token.get().isExpired()) {
            throw new IllegalAccessError("Token is invalid");
        }
    }

    public static User authenticate(TokenRepository tokenRepository, UserRepository userRepository,
            String tokenString) {
        validateToken(tokenRepository, tokenString);
        Token token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        return userRepository.findById(token.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
    }
}
