package project.dto.returns;

import java.util.UUID;

public class CommentCreateReturnDto {
    private UUID commentId;

    public CommentCreateReturnDto(UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }

}
