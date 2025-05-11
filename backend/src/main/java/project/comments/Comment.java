package project.comments;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import project.users.User;
import project.threads.Thread;

@Entity
@Table(name = "comments", indexes = {
    @Index(name = "idx_comments_thread", columnList = "thread"),
    @Index(name = "idx_comments_parent", columnList = "parent"),
    @Index(name = "idx_comments_created", columnList = "thread, createdAt"),
    @Index(name = "idx_comments_likes", columnList = "thread, netLikeCount")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "thread", nullable = false)
    private Thread thread;

    @ManyToOne
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @Column(length = 10000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "parent_comment")
    private Comment parentComment;

    @ManyToMany
    private List<User> likedBy;

    @ManyToMany
    private List<User> dislikedBy;

    private Date createdAt;

    private Integer netLikeCount;

    // cascade all seems like questionable behaviour but implementing fully correct
    // behaviour seems too annoying right now
    // do i even need this?
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    public Comment() {

    }

    public Comment(Thread thread, User creator, String text, Comment parentComment) {
        this.thread = thread;
        this.creator = creator;
        this.text = text;
        this.parentComment = parentComment;
        this.replies = new ArrayList<>();
        this.createdAt = Date.from(Instant.now());
        this.likedBy = new ArrayList<>();
        this.dislikedBy = new ArrayList<>();
        this.netLikeCount = 0;
    }

    public UUID getId() {
        return id;
    }

    public Thread getThread() {
        return thread;
    }

    public User getCreator() {
        return creator;
    }

    public String getText() {
        return text;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void addReply(Comment comment) {
        replies.add(comment);
    }

    public UUID getThreadId() {
        return thread.getId();
    }

    public UUID getCreatorId() {
        return creator.getId();
    }

    public UUID getParentCommentId() {
        return parentComment != null ? parentComment.getId() : null;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public List<User> getDislikedBy() {
        return dislikedBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void addLike(User user) {
        likedBy.add(user);
        netLikeCount += 1;
    }

    public void addDislike(User user) {
        dislikedBy.add(user);
        netLikeCount -= 1;
    }

    public void unlike(User user) {
        likedBy.remove(user);
        netLikeCount -= 1;
    }

    public boolean likedBy(User user) {
        return likedBy.contains(user);
    }

    public boolean dislikedBy(User user) {
        return dislikedBy.contains(user);
    }

    public void undislike(User user) {
        dislikedBy.remove(user);
        netLikeCount += 1;
    }

    public Integer getNetLikeCount() {
        return netLikeCount;
    }
}
