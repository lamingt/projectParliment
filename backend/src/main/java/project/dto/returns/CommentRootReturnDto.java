package project.dto.returns;

import java.util.List;

public class CommentRootReturnDto {
    private PaginationDto pagination;
    private List<CommentInfoDto> commentInfo;

    public CommentRootReturnDto(PaginationDto pagination, List<CommentInfoDto> commentInfo) {
        this.pagination = pagination;
        this.commentInfo = commentInfo;
    }

    public PaginationDto getPagination() {
        return pagination;
    }

    public List<CommentInfoDto> getCommentInfo() {
        return commentInfo;
    }

}
