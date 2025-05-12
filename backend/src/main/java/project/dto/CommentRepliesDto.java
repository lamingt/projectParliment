package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRepliesDto {
    private UUID commentId;
    private Integer pageNum;

    public CommentRepliesDto(@JsonProperty("commentId") UUID commentId, @JsonProperty("pageNum") Integer pageNum) {
        this.commentId = commentId;
        this.pageNum = pageNum;
    }

    public UUID getCommentId() {
        return commentId;
    }

    public Integer getPageNum() {
        return pageNum;
    }
}
