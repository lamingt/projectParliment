package project.dto;

public class ThreadListDto {
    private Integer pageNum;

    public ThreadListDto(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

}
