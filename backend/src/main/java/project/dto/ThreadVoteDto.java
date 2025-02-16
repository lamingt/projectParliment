package project.dto;

import java.util.UUID;

public class ThreadVoteDto {
    private UUID threadId;
    private UUID userId;

    public ThreadVoteDto(UUID threadId, UUID userId) {
        this.threadId = threadId;
        this.userId = userId;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public UUID getUserId() {
        return userId;
    }

}
