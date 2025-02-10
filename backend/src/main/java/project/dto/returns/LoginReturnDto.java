package project.dto.returns;

import java.util.UUID;

public class LoginReturnDto {
    private String token;
    private UUID userId;

    public LoginReturnDto(String token, UUID userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

}
