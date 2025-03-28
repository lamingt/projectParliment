package project.dto;

import java.util.UUID;

public class CommentGetDto {
    private UUID threadId;

    public CommentGetDto(UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }

}
