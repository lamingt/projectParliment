package project.dto.returns;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationDto {
    private Integer numPages;
    private Integer currentPage;
    private long numItems;

    public PaginationDto(Integer numPages, Integer currentPage, @JsonProperty("numItems") long numItems) {
        this.numPages = numPages;
        this.currentPage = currentPage;
        this.numItems = numItems;
    }

    public Integer getNumPages() {
        return numPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public long getNumItems() {
        return numItems;
    }

}
