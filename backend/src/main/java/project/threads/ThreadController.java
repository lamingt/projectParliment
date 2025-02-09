package project.threads;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.dto.ResponseDto;
import project.dto.ThreadInfoDto;
import project.dto.ThreadListDto;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadController {
    private ThreadService threadService;

    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    // Gets a list of all threads ordered by date on page num
    @GetMapping("/list")
    public ResponseEntity<ResponseDto> getThreads(@RequestParam Integer pageNum,
            @RequestHeader("Authorization") String token) {
        ThreadListDto dto = new ThreadListDto(token, pageNum);
        try {
            return new ResponseEntity<ResponseDto>(threadService.getThreads(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

    @GetMapping("/thread")
    public ResponseEntity<ResponseDto> getThreadInfo(@RequestParam UUID threadId,
            @RequestHeader("Authorization") String token) {
        ThreadInfoDto dto = new ThreadInfoDto(threadId, token);
        try {
            return new ResponseEntity<ResponseDto>(threadService.getThreadInfo(dto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        } catch (IllegalAccessError e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(e.getMessage(), null));
        }
    }

}
