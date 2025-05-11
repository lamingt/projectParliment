package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentGetDto {
    private UUID threadId;

    public CommentGetDto(@JsonProperty("threadId") UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }

}
