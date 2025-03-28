package project.dto.returns;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ThreadInfoReturnDto {
    private UUID id;
    private String title;
    private String summary;
    private LocalDate date;
    private String chamber;
    private String status;
    private Boolean active;
    private List<UUID> likedBy;
    private List<UUID> dislikedBy;

    public ThreadInfoReturnDto(UUID id, String title, String summary, LocalDate date, String chamber, String status,
            Boolean active, List<UUID> likedBy, List<UUID> dislikedBy) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.chamber = chamber;
        this.status = status;
        this.active = active;
        this.likedBy = likedBy;
        this.dislikedBy = dislikedBy;
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

    public Boolean getActive() {
        return active;
    }

    public List<UUID> getLikedBy() {
        return likedBy;
    }

    public List<UUID> getDislikedBy() {
        return dislikedBy;
    }

    public String getSummary() {
        return summary;
    }
}
