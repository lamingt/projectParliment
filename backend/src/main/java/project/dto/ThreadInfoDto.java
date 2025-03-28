package project.dto;

import java.util.UUID;

public class ThreadInfoDto {
    private UUID threadId;

    public ThreadInfoDto(UUID threadId) {
        this.threadId = threadId;
    }

    public UUID getThreadId() {
        return threadId;
    }
}
