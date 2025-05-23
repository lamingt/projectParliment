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
import project.dto.returns.PaginationDto;
import project.dto.returns.ThreadListReturnDto;
import project.users.Token;
import project.users.TokenRepository;
import project.users.User;
import project.users.UserRepository;
import project.utils.AuthUtils;

@Service
public class ThreadService {
    private ThreadRepository threadRepository;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private static final int THREADS_PER_PAGE = 10;

    public ThreadService(ThreadRepository threadRepository, TokenRepository tokenRepository,
            UserRepository userRepository) {
        this.threadRepository = threadRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public ResponseDto getThreads(ThreadListDto dto, String tokenString) {
        Integer pageNum = dto.getPageNum();

        PageRequest pageRequest = PageRequest.of(pageNum - 1, THREADS_PER_PAGE);
        List<Thread> threads = threadRepository.getThreadsByDate(pageRequest);
        long numThreads = threadRepository.count();
        if ((pageNum - 1) * THREADS_PER_PAGE >= numThreads) {
            throw new IllegalArgumentException("No threads exist on this page");
        }

        List<ThreadInfoReturnDto> pageInfo = new ArrayList<>();
        for (Thread thread : threads) {
            pageInfo.add(getThreadInfoDto(thread, tokenString));
        }

        PaginationDto pagination = new PaginationDto(
                Integer.valueOf((int) Math.ceil((double) numThreads / THREADS_PER_PAGE)),
                pageNum,
                numThreads);

        ThreadListReturnDto response = new ThreadListReturnDto(pageInfo, pagination);

        return new ResponseDto("Threads obtained successfully", response);
    }

    public ResponseDto getThreadInfo(ThreadInfoDto threadInfo, String tokenString) {
        UUID threadId = threadInfo.getThreadId();

        Optional<Thread> thread = threadRepository.findById(threadId);
        if (!thread.isPresent()) {
            throw new IllegalArgumentException("Invalid thread id");
        }

        Thread t = thread.get();
        ThreadInfoReturnDto response = getThreadInfoDto(t, tokenString);

        return new ResponseDto("Thread obtained successfully", response);
    }

    private ThreadInfoReturnDto getThreadInfoDto(Thread thread, String tokenString) {
        Optional<User> user = AuthUtils.tryAuthenticate(tokenRepository, userRepository, tokenString);
        String likeStatus = "";
        if (user.isPresent() && thread.getDislikedBy().contains(user.get())) {
            likeStatus = "disliked";
        } else if (user.isPresent() && thread.getLikedBy().contains(user.get())) {
            likeStatus = "liked";
        }

        return new ThreadInfoReturnDto(thread.getId(), 
            thread.getTitle(), 
            thread.getSummary(),  
            thread.getDate(),
            thread.getChamber(), 
            thread.getStatus(), 
            thread.getActive(),
            thread.getLikedBy().size(), 
            thread.getDislikedBy().size(), 
            thread.getComments().size(),
            likeStatus
        );
    }

    public ResponseDto likeThread(ThreadVoteDto data, String tokenString) {
        UUID threadId = data.getThreadId();

        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));

        AuthUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);

        UUID userId = token.get().getUserId();
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
        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));

        AuthUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);

        UUID userId = token.get().getUserId();
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
