package project.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.dto.ResponseDto;
import project.dto.ThreadInfoDto;
import project.dto.ThreadListDto;
import project.dto.returns.ThreadInfoReturnDto;
import project.dto.returns.ThreadListInfoReturnDto;
import project.dto.returns.ThreadListPaginationDto;
import project.dto.returns.ThreadListReturnDto;
import project.users.TokenRepository;
import project.utils.TokenUtils;

@Service
public class ThreadService {
    private ThreadRepository threadRepository;
    private TokenRepository tokenRepository;
    private static final int threadsPerPage = 10;

    public ThreadService(ThreadRepository threadRepository, TokenRepository tokenRepository) {
        this.threadRepository = threadRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseDto getThreads(ThreadListDto dto) {
        Integer pageNum = dto.getPageNum();
        TokenUtils.validateToken(tokenRepository, dto.getToken()); // make sure this works
        // Optional<Token> token = tokenRepository.findByToken(dto.getToken());
        // if (!token.isPresent() || token.get().isExpired()) {
        // throw new IllegalAccessError("Token is invalid");
        // }

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
        List<Thread> threads = threadRepository.getThreadsByDate(pageRequest);
        long numThreads = threadRepository.count();
        if ((pageNum - 1) * threadsPerPage >= numThreads) {
            throw new IllegalArgumentException("No threads exist on this page");
        }

        List<ThreadListInfoReturnDto> pageInfo = new ArrayList<>();
        for (Thread thread : threads) {
            pageInfo.add(new ThreadListInfoReturnDto(thread.getId(), thread.getTitle(), thread.getSummary(),
                    thread.getDate(),
                    thread.getChamber(), thread.getStatus(), thread.getActive(),
                    thread.getComments().size(), thread.getLikedBy().size()));
        }

        ThreadListPaginationDto pagination = new ThreadListPaginationDto(
                Integer.valueOf((int) Math.ceil((double) numThreads / 10)),
                pageNum,
                numThreads);

        ThreadListReturnDto response = new ThreadListReturnDto(pageInfo, pagination);

        return new ResponseDto("Threads obtained successfully", response);
    }

    public ResponseDto getThreadInfo(ThreadInfoDto threadInfo) {
        UUID threadId = threadInfo.getThreadId();
        TokenUtils.validateToken(tokenRepository, threadInfo.getToken());

        Optional<Thread> thread = threadRepository.findById(threadId);
        if (!thread.isPresent()) {
            throw new IllegalArgumentException("Invalid thread id");
        }

        Thread t = thread.get();

        ThreadInfoReturnDto response = new ThreadInfoReturnDto(threadId, t.getTitle(), t.getSummary(), t.getDate(),
                t.getChamber(), t.getStatus(), t.getActive(),
                t.getLikedBy().stream().map(user -> user.getId()).collect(Collectors.toList()),
                t.getDislikedBy().stream().map(user -> user.getId()).collect(Collectors.toList()));

        return new ResponseDto("Thread obtained successfully", response);
    }
}
