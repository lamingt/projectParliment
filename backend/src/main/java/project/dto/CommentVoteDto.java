package project.dto;

import java.util.UUID;

public class CommentVoteDto {
    private UUID commentId;
    private UUID userId;

    public CommentVoteDto(UUID commentId, UUID userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public UUID getUserId() {
        return userId;
    }
}
