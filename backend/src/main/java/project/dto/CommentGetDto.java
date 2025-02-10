package project.dto;

import java.util.UUID;

public class CommentGetDto {
    private UUID threadId;
    private String token;

    public CommentGetDto(UUID threadId, String token) {
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
