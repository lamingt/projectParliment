package project.dto;

import java.util.UUID;

public class ThreadInfoDto {
    private UUID threadId;
    private String token;

    public ThreadInfoDto(UUID threadId, String token) {
        this.threadId = threadId;
        this.token = token;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public String getToken() {
        return token;
    }

}
