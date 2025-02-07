package project.dto.returns;

import java.time.LocalDate;
import java.util.UUID;

public class ThreadListInfoDto {
    private UUID id;
    private String title;
    private LocalDate date;
    private String chamber;
    private String status;
    private Boolean active;
    private Integer numLikes;
    private Integer numComments;

    public ThreadListInfoDto(UUID id, String title, LocalDate date, String chamber, String status, Boolean active,
            Integer numLikes, Integer numComments) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.chamber = chamber;
        this.status = status;
        this.active = active;
        this.numLikes = numLikes;
        this.numComments = numComments;
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

    public Integer getNumLikes() {
        return numLikes;
    }

    public Integer getNumComments() {
        return numComments;
    }
}
