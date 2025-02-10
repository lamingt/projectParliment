package project.dto;

import java.util.UUID;

public class CommentCreateDto {
    private UUID userId;
    private UUID threadId;
    private UUID parentComment;
    private String text;

    public CommentCreateDto(UUID userId, UUID threadId, UUID parentComment, String text) {
        this.userId = userId;
        this.threadId = threadId;
        this.parentComment = parentComment;
        this.text = text;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public UUID getParentComment() {
        return parentComment;
    }

    public String getText() {
        return text;
    }

}
