package project.comments;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.dto.CommentCreateDto;
import project.dto.CommentGetDto;
import project.dto.ResponseDto;
import project.dto.ThreadInfoDto;

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
    public ResponseEntity<ResponseDto> postMethodName(@RequestBody CommentCreateDto dto,
            @RequestHeader("Authorization") String token) {
        try {
            return new ResponseEntity<ResponseDto>(commentService.createComment(dto, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> getComments(@RequestParam UUID threadId,
            @RequestHeader("Authorization") String token) {
        CommentGetDto dto = new CommentGetDto(threadId, token);
        try {
            return new ResponseEntity<ResponseDto>(commentService.getComments(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

}
