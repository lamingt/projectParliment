package project.dto.returns;

import java.util.List;

public class ThreadListReturnDto {
    private List<ThreadListInfoDto> pageInfo;
    private ThreadListPaginationDto pagination;

    public ThreadListReturnDto(List<ThreadListInfoDto> pageInfo, ThreadListPaginationDto pagination) {
        this.pageInfo = pageInfo;
        this.pagination = pagination;
    }

    public List<ThreadListInfoDto> getPageInfo() {
        return pageInfo;
    }

    public ThreadListPaginationDto getPagination() {
        return pagination;
    }

}
