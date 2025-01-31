package project.threads;

import org.springframework.stereotype.Service;

@Service
public class ThreadService {
    private ThreadRepository threadRepository;

    public ThreadService(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }
}
