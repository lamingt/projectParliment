package project.comments;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.dto.CommentCreateDto;
import project.dto.CommentGetDto;
import project.dto.CommentRepliesDto;
import project.dto.CommentRootDto;
import project.dto.CommentVoteDto;
import project.dto.ResponseDto;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> createComment(
            @RequestBody CommentCreateDto dto,
            @RequestHeader("Authorization") String token
        ) {
        try {
            return new ResponseEntity<ResponseDto>(commentService.createComment(dto, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @Deprecated
    @GetMapping("")
    public ResponseEntity<ResponseDto> getComments(@RequestParam("threadId") UUID threadId) {
        CommentGetDto dto = new CommentGetDto(threadId);
        try {
            return new ResponseEntity<ResponseDto>(commentService.getComments(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/root")
    public ResponseEntity<ResponseDto> getRootComments(
        @RequestParam("threadId") UUID threadId,
        @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(name = "sort", defaultValue = "recent") String sort
    ) {
        CommentRootDto dto = new CommentRootDto(threadId, pageNum, sort);
        try {
            return new ResponseEntity<ResponseDto>(commentService.getRootComments(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/replies")
    public ResponseEntity<ResponseDto> getReplies(
            @RequestParam("commentId") UUID commentId,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum
        ) {
        CommentRepliesDto dto = new CommentRepliesDto(commentId, pageNum);
        try {
            return new ResponseEntity<ResponseDto>(commentService.getReplies(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    

    @PostMapping("/like")
    public ResponseEntity<ResponseDto> likeComment(
            @RequestBody CommentVoteDto data,
            @RequestHeader("Authorization") String token
        ) {
        try {
            return new ResponseEntity<ResponseDto>(commentService.likeComment(data, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<ResponseDto> dislikeComment(
            @RequestBody CommentVoteDto data,
            @RequestHeader("Authorization") String token
        ) {
        try {
            return new ResponseEntity<ResponseDto>(commentService.dislikeComment(data, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }
}
