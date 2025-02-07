package project.dto;

public class ThreadListDto {
    private String token;
    private Integer pageNum;

    public ThreadListDto(String token, Integer pageNum) {
        this.token = token;
        this.pageNum = pageNum;
    }

    public String getToken() {
        return token;
    }

    public Integer getPageNum() {
        return pageNum;
    }

}
