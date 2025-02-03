package project.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import project.comments.Comment;
import project.parliament.ParliamentBill;
import project.users.User;

@Entity
@Table(name = "threads")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Maybe just put all bill info in the thread
    @JoinColumn(name = "bill")
    @OneToOne
    private ParliamentBill parliamentBill;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    private List<User> likedBy;

    public Thread(ParliamentBill parliamentBill) {
        this.parliamentBill = parliamentBill;
        this.comments = new ArrayList<>();
        this.likedBy = new ArrayList<>();
    }

}
