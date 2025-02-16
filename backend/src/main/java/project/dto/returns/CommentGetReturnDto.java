package project.dto.returns;

import java.util.List;
import java.util.UUID;

public class CommentGetReturnDto {
    private UUID id;
    private UUID threadId;
    private UUID creatorId;
    private String text;
    private UUID parentCommentId;
    private List<UUID> likedBy;
    private List<UUID> dislikedBy;

    public CommentGetReturnDto(UUID id, UUID threadId, UUID creatorId, String text, UUID parentCommentId,
            List<UUID> likedBy, List<UUID> dislikedBy) {
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
