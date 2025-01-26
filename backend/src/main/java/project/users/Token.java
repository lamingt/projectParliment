package project.users;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Token {
    private UUID userId;

    @Id
    private String token;
    private LocalDate createdAt;

    private static final Integer tokenExpirationLength = 7;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public Token(UUID userId, LocalDate createdAt) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.token = generateToken();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getExpiresAt() {
        return createdAt.plusDays(tokenExpirationLength);
    }

    public boolean isExpired() {
        return getExpiresAt().isBefore(LocalDate.now());
    }

    // https://stackoverflow.com/questions/13992972/how-to-create-an-authentication-token-using-java
    private static String generateToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
