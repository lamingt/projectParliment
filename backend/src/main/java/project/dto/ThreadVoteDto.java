package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThreadVoteDto {
    private UUID threadId;

    public ThreadVoteDto(@JsonProperty("threadId") UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }
}
