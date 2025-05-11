package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThreadInfoDto {
    private UUID threadId;

    public ThreadInfoDto(@JsonProperty("threadId") UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }
}
