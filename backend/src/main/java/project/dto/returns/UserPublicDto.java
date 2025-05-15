package project.dto.returns;

import java.util.UUID;

public class UserPublicDto {
    private UUID userId;
    private String username;

    public UserPublicDto(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
