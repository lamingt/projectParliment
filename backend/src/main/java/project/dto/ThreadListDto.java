package project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ThreadListDto {
    private Integer pageNum;

    public ThreadListDto(@JsonProperty("pageNum") Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

}
