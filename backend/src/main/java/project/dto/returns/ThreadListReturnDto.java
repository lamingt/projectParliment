package project.dto.returns;

import java.util.List;

public class ThreadListReturnDto {
    private List<ThreadListInfoReturnDto> pageInfo;
    private PaginationDto pagination;

    public ThreadListReturnDto(List<ThreadListInfoReturnDto> pageInfo, PaginationDto pagination) {
        this.pageInfo = pageInfo;
        this.pagination = pagination;
    }

    public List<ThreadListInfoReturnDto> getPageInfo() {
        return pageInfo;
    }

    public PaginationDto getPagination() {
        return pagination;
    }

}
