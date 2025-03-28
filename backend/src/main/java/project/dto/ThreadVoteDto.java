package project.dto;

import java.util.UUID;

public class ThreadVoteDto {
    private UUID threadId;

    public ThreadVoteDto(UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }
}
