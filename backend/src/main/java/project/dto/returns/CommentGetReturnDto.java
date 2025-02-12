package project.dto.returns;

import java.util.List;
import java.util.UUID;

import project.users.User;

public class CommentGetReturnDto {
    private UUID id;
    private UUID threadId;
    private UUID creatorId;
    private String text;
    private UUID parentCommentId;
    private List<User> likedBy;
    private List<User> dislikedBy;

    public CommentGetReturnDto(UUID id, UUID threadId, UUID creatorId, String text, UUID parentCommentId,
            List<User> likedBy, List<User> dislikedBy) {
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

    public List<User> getLikedBy() {
        return likedBy;
    }

    public List<User> getDislikedBy() {
        return dislikedBy;
    }

}
