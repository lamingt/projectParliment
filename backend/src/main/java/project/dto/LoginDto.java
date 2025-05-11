package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto {
    private final String email;
    private final String password;

    public LoginDto(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
