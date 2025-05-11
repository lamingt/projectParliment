package project.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRootDto {
    private UUID threadId;
    private Integer pageNum;
    private String sort;

    public CommentRootDto(@JsonProperty("threadId") UUID threadId, @JsonProperty("pageNum") Integer pageNum,
            @JsonProperty("sort") String sort) {
        this.threadId = threadId;
        this.pageNum = pageNum;
        this.sort = sort;
    }

    public UUID getThreadId() {
        return threadId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public String getSort() {
        return sort;
    }
}
