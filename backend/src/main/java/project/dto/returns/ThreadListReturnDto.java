package project.dto.returns;

import java.util.List;

public class ThreadListReturnDto {
    private List<ThreadListInfoReturnDto> pageInfo;
    private ThreadListPaginationDto pagination;

    public ThreadListReturnDto(List<ThreadListInfoReturnDto> pageInfo, ThreadListPaginationDto pagination) {
        this.pageInfo = pageInfo;
        this.pagination = pagination;
    }

    public List<ThreadListInfoReturnDto> getPageInfo() {
        return pageInfo;
    }

    public ThreadListPaginationDto getPagination() {
        return pagination;
    }

}
