package project.dto;

public class RegisterDto {
    private final String username;
    private final String password;
    private final String email;

    public RegisterDto(String email, String username, String password) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
