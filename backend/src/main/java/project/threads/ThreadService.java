package project.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.dto.ResponseDto;
import project.dto.ThreadInfoDto;
import project.dto.ThreadListDto;
import project.dto.ThreadVoteDto;
import project.dto.returns.ThreadInfoReturnDto;
import project.dto.returns.ThreadListInfoReturnDto;
import project.dto.returns.ThreadListPaginationDto;
import project.dto.returns.ThreadListReturnDto;
import project.users.Token;
import project.users.TokenRepository;
import project.users.User;
import project.users.UserRepository;
import project.utils.TokenUtils;

@Service
public class ThreadService {
    private ThreadRepository threadRepository;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private static final int threadsPerPage = 10;

    public ThreadService(ThreadRepository threadRepository, TokenRepository tokenRepository,
            UserRepository userRepository) {
        this.threadRepository = threadRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public ResponseDto getThreads(ThreadListDto dto) {
        Integer pageNum = dto.getPageNum();
        TokenUtils.validateToken(tokenRepository, dto.getToken());

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

    public ResponseDto likeThread(ThreadVoteDto data, String tokenString) {
        UUID threadId = data.getThreadId();
        UUID userId = data.getUserId();

        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);
        if (!token.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        if (thread.likedBy(user)) {
            thread.unlike(user);
        } else {
            if (thread.dislikedBy(user)) {
                thread.undislike(user);
            }
            thread.addLike(user);
        }
        threadRepository.save(thread);

        return new ResponseDto("Thread updated successfully", null);
    }

    public ResponseDto dislikeThread(ThreadVoteDto data, String tokenString) {
        UUID threadId = data.getThreadId();
        UUID userId = data.getUserId();

        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);
        if (!token.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Invalid token");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        if (thread.dislikedBy(user)) {
            thread.undislike(user);
        } else {
            if (thread.likedBy(user)) {
                thread.unlike(user);
            }
            thread.addDislike(user);
        }
        threadRepository.save(thread);

        return new ResponseDto("Thread updated successfully", null);
    }
}
