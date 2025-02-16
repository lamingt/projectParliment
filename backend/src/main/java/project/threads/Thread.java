package project.threads;

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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import project.comments.Comment;
import project.users.User;

@Entity
@Table(name = "threads")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String title;
    private LocalDate date;
    private String chamber;
    private String status;
    @Column(length = 5000)
    private String summary;
    private Boolean active;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    private List<User> likedBy;

    @ManyToMany
    private List<User> dislikedBy;

    public Thread() {
    }

    public Thread(String title, LocalDate date, String chamber, String status, String summary, Boolean active) {
        this.title = title;
        this.date = date;
        this.chamber = chamber;
        this.status = status;
        this.summary = summary;
        this.active = active;
        this.comments = new ArrayList<>();
        this.likedBy = new ArrayList<>();
        this.dislikedBy = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getChamber() {
        return chamber;
    }

    public String getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public Boolean getActive() {
        return active;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<User> getDislikedBy() {
        return dislikedBy;
    }

    public void addLike(User user) {
        likedBy.add(user);
    }

    public void addDislike(User user) {
        dislikedBy.add(user);
    }

    public void unlike(User user) {
        likedBy.remove(user);
    }

    public boolean likedBy(User user) {
        return likedBy.contains(user);
    }

    public boolean dislikedBy(User user) {
        return dislikedBy.contains(user);
    }

    public void undislike(User user) {
        dislikedBy.remove(user);
    }
}
