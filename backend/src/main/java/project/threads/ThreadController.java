package project.threads;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.dto.ResponseDto;
import project.dto.ThreadInfoDto;
import project.dto.ThreadListDto;
import project.dto.ThreadVoteDto;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadController {
    private ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    // Gets a list of all threads ordered by date on page num
    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getThreads(@RequestParam("pageNum") Integer pageNum) {
        ThreadListDto dto = new ThreadListDto(pageNum);
        try {
            return new ResponseEntity<ResponseDto>(threadService.getThreads(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/thread")
    public ResponseEntity<ResponseDto> getThreadInfo(@RequestParam("threadId") UUID threadId) {
        ThreadInfoDto dto = new ThreadInfoDto(threadId);
        try {
            return new ResponseEntity<ResponseDto>(threadService.getThreadInfo(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    // Will like a thread if it is unliked, or unlike if it is already liked
    @PostMapping("/like")
    public ResponseEntity<ResponseDto> likeThread(@RequestBody ThreadVoteDto data,
            @RequestHeader("Authorization") String token) {
        try {
            return new ResponseEntity<ResponseDto>(threadService.likeThread(data, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<ResponseDto> dislikeThread(@RequestBody ThreadVoteDto data,
            @RequestHeader("Authorization") String token) {
        try {
            return new ResponseEntity<ResponseDto>(threadService.dislikeThread(data, token), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

}
