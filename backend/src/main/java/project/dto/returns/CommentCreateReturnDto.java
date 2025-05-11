package project.dto.returns;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentCreateReturnDto {
    private UUID commentId;

    public CommentCreateReturnDto(@JsonProperty("commentId") UUID commentId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }

}
