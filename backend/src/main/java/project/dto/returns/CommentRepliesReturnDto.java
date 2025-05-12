package project.dto.returns;

import java.util.List;

public class CommentRepliesReturnDto {
    private PaginationDto pagination;
    private List<CommentInfoDto> commentInfo;

    public CommentRepliesReturnDto(PaginationDto pagination, List<CommentInfoDto> commentInfo) {
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
