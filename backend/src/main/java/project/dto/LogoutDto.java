package project.dto;

public class LogoutDto {
    private String token;

    public LogoutDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
