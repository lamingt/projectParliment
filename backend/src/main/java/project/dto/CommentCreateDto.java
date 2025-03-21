package project.dto;

import java.util.UUID;

public class CommentCreateDto {
    private UUID threadId;
    private UUID parentComment;
    private String text;

    public CommentCreateDto(UUID threadId, UUID parentComment, String text) {
        this.threadId = threadId;
        this.parentComment = parentComment;
        this.text = text;
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
