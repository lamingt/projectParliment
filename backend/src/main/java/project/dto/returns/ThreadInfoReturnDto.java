package project.dto.returns;

import java.time.LocalDate;
import java.util.UUID;

public class ThreadInfoReturnDto {
    private UUID id;
    private String title;
    private String summary;
    private LocalDate date;
    private String chamber;
    private String status;
    private Boolean active;
    private Integer numLikes;
    private Integer numDislikes;
    private Integer numComments;
    private String likeStatus;

    public ThreadInfoReturnDto(UUID id, String title, String summary, LocalDate date, String chamber, String status,
            Boolean active,
            Integer numLikes, Integer numDislikes, Integer numComments, String likeStatus) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.date = date;
        this.chamber = chamber;
        this.status = status;
        this.active = active;
        this.numLikes = numLikes;
        this.numDislikes = numDislikes;
        this.numComments = numComments;
        this.likeStatus = likeStatus;
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

    public Integer getNumDislikes() {
        return numDislikes;
    }

    public Integer getNumComments() {
        return numComments;
    }

    public String getSummary() {
        return summary;
    }

    public String getLikeStatus() {
        return likeStatus;
    }
}
