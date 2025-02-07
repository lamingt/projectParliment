package project.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import project.dto.ResponseDto;
import project.dto.ThreadListDto;
import project.dto.returns.ThreadListInfoDto;
import project.dto.returns.ThreadListPaginationDto;
import project.dto.returns.ThreadListReturnDto;
import project.users.Token;
import project.users.TokenRepository;

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
        Optional<Token> token = tokenRepository.findByToken(dto.getToken());
        if (!token.isPresent() || token.get().isExpired()) {
            throw new IllegalAccessError("Token is invalid");
        }

        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
        List<Thread> threads = threadRepository.getThreadsByDate(pageRequest);
        long numThreads = threadRepository.count();
        if ((pageNum - 1) * threadsPerPage >= numThreads) {
            throw new IllegalArgumentException("No threads exist on this page");
        }

        List<ThreadListInfoDto> pageInfo = new ArrayList<>();
        for (Thread thread : threads) {
            pageInfo.add(new ThreadListInfoDto(thread.getId(), thread.getTitle(),
                    thread.getDate(),
                    thread.getChamber(), thread.getStatus(), thread.getActive(),
                    thread.getComments().size(), thread.getLikedBy().size()));
        }

        ThreadListPaginationDto pagination = new ThreadListPaginationDto(
                Integer.valueOf((int) Math.ceil((double) numThreads / 10)),
                pageNum,
                numThreads);

        ThreadListReturnDto response = new ThreadListReturnDto(pageInfo, pagination);

        // // To act as an index
        // pageNum--;
        // for (int i = pageNum * threadsPerPage; i < (pageNum + 1) * threadsPerPage &&
        // i < threads.size(); i++) {
        // Thread thread = threads.get(i);
        // response.add(new ThreadListReturnDto(thread.getId(), thread.getTitle(),
        // thread.getDate(),
        // thread.getChamber(), thread.getStatus(), thread.getActive(),
        // thread.getComments().size(), thread.getLikedBy().size()));
        // }

        return new ResponseDto("Threads obtained successfully", response);
    }
}
