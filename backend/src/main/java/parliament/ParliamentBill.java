package parliament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class ParliamentBill {

    @Id
    @SequenceGenerator(name = "parliament_sequence", sequenceName = "parliament_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parliament_sequence")

    private Integer id;
    private String title;
    private String date;
    private String chamber;
    private String status;
    @Column(length = 5000)
    private String summary;

    public ParliamentBill() {
    }

    public ParliamentBill(String title, String date, String chamber, String status, String summary) {
        this.title = title;
        this.date = date;
        this.chamber = chamber;
        this.status = status;
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
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

    public String getTitle() {
        return title;
    }
}
