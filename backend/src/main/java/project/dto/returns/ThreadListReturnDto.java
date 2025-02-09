package project.dto.returns;

import java.util.List;

public class ThreadListReturnDto {
    private List<ThreadInfoReturnDto> pageInfo;
    private ThreadListPaginationDto pagination;

    public ThreadListReturnDto(List<ThreadInfoReturnDto> pageInfo, ThreadListPaginationDto pagination) {
        this.pageInfo = pageInfo;
        this.pagination = pagination;
    }

    public List<ThreadInfoReturnDto> getPageInfo() {
        return pageInfo;
    }

    public ThreadListPaginationDto getPagination() {
        return pagination;
    }

}
