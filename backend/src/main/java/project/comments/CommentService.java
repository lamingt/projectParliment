package project.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import project.dto.CommentCreateDto;
import project.dto.CommentGetDto;
import project.dto.CommentVoteDto;
import project.dto.ResponseDto;
import project.dto.returns.CommentCreateReturnDto;
import project.dto.returns.CommentGetReturnDto;
import project.threads.ThreadRepository;
import project.users.Token;
import project.threads.Thread;
import project.users.TokenRepository;
import project.users.User;
import project.users.UserRepository;
import project.utils.TokenUtils;

@Service
public class CommentService {
    private ThreadRepository threadRepository;
    private CommentRepository commentRepository;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, TokenRepository tokenRepository,
            ThreadRepository threadRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tokenRepository = tokenRepository;
        this.threadRepository = threadRepository;
        this.userRepository = userRepository;
    }

    public ResponseDto createComment(CommentCreateDto dto, String tokenString) {
        UUID threadId = dto.getThreadId();
        UUID parentCommentId = dto.getParentComment();
        String text = dto.getText();

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        UUID userId = token.get().getUserId();

        Thread thread = threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        Comment parentComment = thread.getComments().stream().filter(c -> c.getId().equals(parentCommentId))
                .findFirst().orElse(null);
        if (parentCommentId != null && parentComment == null) {
            throw new IllegalArgumentException("Invalid parent comment id");
        }

        Comment comment = new Comment(thread, user, text, parentComment);
        if (parentComment != null) {
            parentComment.addReply(comment);
        }
        commentRepository.save(comment);
        // cascading should handle the rest of the saving
        thread.addComment(comment);
        threadRepository.save(thread);

        return new ResponseDto("Comment created successfully", new CommentCreateReturnDto(comment.getId()));
    }

    public ResponseDto getComments(CommentGetDto dto) {
        UUID threadID = dto.getThreadId();

        Thread thread = threadRepository.findById(threadID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
        List<Comment> comments = thread.getComments();
        List<CommentGetReturnDto> res = new ArrayList<>();
        comments.forEach(c -> res.add(new CommentGetReturnDto(c.getId(), c.getThreadId(), c.getCreatorId(), c.getText(),
                c.getParentCommentId(), c.getLikedBy().stream().map(u -> u.getId()).collect(Collectors.toList()),
                c.getDislikedBy().stream().map(u -> u.getId()).collect(Collectors.toList()))));

        return new ResponseDto("Comments retrieved successfuly", res);
    }

    public ResponseDto likeComment(CommentVoteDto data, String tokenString) {
        UUID commentId = data.getCommentId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment id"));

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);

        UUID userId = token.get().getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        if (comment.likedBy(user)) {
            comment.unlike(user);
        } else {
            if (comment.dislikedBy(user)) {
                comment.undislike(user);
            }
            comment.addLike(user);
        }
        commentRepository.save(comment);

        return new ResponseDto("Thread updated successfully", null);
    }

    public ResponseDto dislikeComment(CommentVoteDto data, String tokenString) {
        UUID commentId = data.getCommentId();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment id"));

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findById(tokenString);

        UUID userId = token.get().getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        if (comment.dislikedBy(user)) {
            comment.undislike(user);
        } else {
            if (comment.likedBy(user)) {
                comment.unlike(user);
            }
            comment.addDislike(user);
        }
        commentRepository.save(comment);

        return new ResponseDto("Thread updated successfully", null);
    }
}
