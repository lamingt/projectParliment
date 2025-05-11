package project.dto.returns;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentGetReturnDto {
    private UUID id;
    private UUID threadId;
    private UUID creatorId;
    private String text;
    private UUID parentCommentId;
    private List<UUID> likedBy;
    private List<UUID> dislikedBy;

    public CommentGetReturnDto(@JsonProperty("id") UUID id, @JsonProperty("threadId") UUID threadId,
            @JsonProperty("creatorId") UUID creatorId, @JsonProperty("text") String text,
            @JsonProperty("parentCommentId") UUID parentCommentId,
            @JsonProperty("likedBy") List<UUID> likedBy, @JsonProperty("dislikedBy") List<UUID> dislikedBy) {
        this.id = id;
        this.threadId = threadId;
        this.creatorId = creatorId;
        this.text = text;
        this.parentCommentId = parentCommentId;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
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

    public List<UUID> getLikedBy() {
        return likedBy;
    }

    public List<UUID> getDislikedBy() {
        return dislikedBy;
    }

}
