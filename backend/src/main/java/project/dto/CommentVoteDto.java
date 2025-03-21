package project.dto;

import java.util.UUID;

public class CommentVoteDto {
    private UUID commentId;

    public CommentVoteDto(UUID commentId, UUID userId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }
}
