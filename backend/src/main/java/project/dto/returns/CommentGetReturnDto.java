package project.dto.returns;

import java.util.UUID;

public class CommentGetReturnDto {
    private UUID id;
    private UUID threadId;
    private UUID creatorId;
    private String text;
    private UUID parentCommentId;

    public CommentGetReturnDto(UUID id, UUID threadId, UUID creatorId, String text, UUID parentCommentId) {
        this.id = id;
        this.threadId = threadId;
        this.creatorId = creatorId;
        this.text = text;
        this.parentCommentId = parentCommentId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public String getText() {
        return text;
    }

    public UUID getParentCommentId() {
        return parentCommentId;
    }

}
