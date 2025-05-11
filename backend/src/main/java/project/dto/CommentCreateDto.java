package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentCreateDto {
    private UUID threadId;
    private UUID parentComment;
    private String text;

    public CommentCreateDto(@JsonProperty("threadId") UUID threadId, @JsonProperty("parentComment") UUID parentComment,
            @JsonProperty("text") String text) {
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
