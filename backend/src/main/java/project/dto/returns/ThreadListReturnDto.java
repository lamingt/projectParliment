package project.dto.returns;

import java.util.List;

public class ThreadListReturnDto {
    private List<ThreadInfoReturnDto> pageInfo;
    private PaginationDto pagination;

    public ThreadListReturnDto(List<ThreadInfoReturnDto> pageInfo, PaginationDto pagination) {
        this.pageInfo = pageInfo;
        this.pagination = pagination;
    }

    public List<ThreadInfoReturnDto> getPageInfo() {
        return pageInfo;
    }

    public PaginationDto getPagination() {
        return pagination;
    }

}
