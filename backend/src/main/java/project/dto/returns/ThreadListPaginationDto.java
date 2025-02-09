package project.dto.returns;

public class ThreadListPaginationDto {
    private Integer totalPages;
    private Integer currentPage;
    private long totalThreads;

    public ThreadListPaginationDto(Integer totalPages, Integer currentPage, long totalThreads) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalThreads = totalThreads;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public long getTotalThreads() {
        return totalThreads;
    }

}
