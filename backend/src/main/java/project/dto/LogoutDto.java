package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutDto {
    private String token;

    public LogoutDto(@JsonProperty("token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
