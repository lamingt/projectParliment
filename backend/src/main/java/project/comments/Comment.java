package project.comments;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import project.users.User;
import project.threads.Thread;

@Entity
@Table(name = "comments")
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

    private LocalDate createdAt;

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
        this.createdAt = LocalDate.now();
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

}
