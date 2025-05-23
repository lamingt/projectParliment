package project.dto.returns;

import java.util.Date;
import java.util.UUID;

public class CommentInfoDto {
    private UUID id;
    private UUID threadId;
    private UserPublicDto creatorInfo;
    private String text;
    private UUID parentCommentId;
    private Integer netLikes;
    private Date createdAt;

    public CommentInfoDto(UUID id, UUID threadId, UserPublicDto creatorInfo, String text, UUID parentCommentId,
            Integer netLikes, Date createdAt) {
        this.id = id;
        this.threadId = threadId;
        this.creatorInfo = creatorInfo;
        this.text = text;
        this.parentCommentId = parentCommentId;
        this.netLikes = netLikes;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public UserPublicDto getCreatorInfo() {
        return creatorInfo;
    }

    public String getText() {
        return text;
    }

    public UUID getParentCommentId() {
        return parentCommentId;
    }

    public Integer getNetLikes() {
        return netLikes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
