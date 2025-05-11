package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentVoteDto {
    private UUID commentId;

    public CommentVoteDto(@JsonProperty("commentId") UUID commentId, @JsonProperty("userId") UUID userId) {
        this.commentId = commentId;
    }

    public UUID getCommentId() {
        return commentId;
    }
}
