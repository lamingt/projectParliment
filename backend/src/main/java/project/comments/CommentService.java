package project.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import project.dto.CommentCreateDto;
import project.dto.CommentGetDto;
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
        UUID userId = dto.getUserId();
        UUID threadId = dto.getThreadId();
        UUID parentCommentId = dto.getParentComment();
        String text = dto.getText();

        TokenUtils.validateToken(tokenRepository, tokenString);
        Optional<Token> token = tokenRepository.findByToken(tokenString);
        if (!token.get().getUserId().equals(userId)) {
            throw new IllegalArgumentException("Invalid user id");
        }

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
        String token = dto.getToken();
        UUID threadID = dto.getThreadId();

        TokenUtils.validateToken(tokenRepository, token);
        Thread thread = threadRepository.findById(threadID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid thread id"));
        List<Comment> comments = thread.getComments();
        List<CommentGetReturnDto> res = new ArrayList<>();
        comments.forEach(c -> res.add(new CommentGetReturnDto(c.getId(), c.getThreadId(), c.getCreatorId(), c.getText(),
                c.getParentCommentId())));

        return new ResponseDto("Comments retrieved successfuly", res);
    }
}
