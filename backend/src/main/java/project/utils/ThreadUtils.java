package project.utils;

import java.util.UUID;

import project.comments.Comment;
import project.comments.CommentRepository;
import project.threads.Thread;
import project.threads.ThreadRepository;

public class ThreadUtils {
    public static Thread loadThread(ThreadRepository threadRepository, UUID threadId) {
        return threadRepository
                .findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
    }

    public static Comment loadComment(CommentRepository commentRepository, UUID commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
    }
}
