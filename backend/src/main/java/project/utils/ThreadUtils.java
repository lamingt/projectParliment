package project.utils;

import java.util.UUID;

import project.threads.Thread;
import project.threads.ThreadRepository;

public class ThreadUtils {
    public static Thread loadThread(ThreadRepository threadRepository, UUID threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
    }
}
