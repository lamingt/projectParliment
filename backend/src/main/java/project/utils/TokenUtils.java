package project.utils;

import java.util.Optional;

import project.users.Token;
import project.users.TokenRepository;

public class TokenUtils {
    public static void validateToken(TokenRepository tokenRepository, String tokenString) {
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        if (!token.isPresent() || token.get().isExpired()) {
            throw new IllegalAccessError("Token is invalid");
        }
    }
}
