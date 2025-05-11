package project.dto.returns;

import java.util.List;

public class CommentRootReturnDto {
    private PaginationDto paginationDto;
    private List<CommentInfoDto> commentInfo;

    public CommentRootReturnDto(PaginationDto paginationDto, List<CommentInfoDto> commentInfo) {
        this.paginationDto = paginationDto;
        this.commentInfo = commentInfo;
    }

    public PaginationDto getPaginationDto() {
        return paginationDto;
    }

    public List<CommentInfoDto> getCommentInfo() {
        return commentInfo;
    }

}
