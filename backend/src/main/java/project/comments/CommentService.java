package project.comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import project.dto.CommentCreateDto;
import project.dto.CommentGetDto;
import project.dto.CommentRepliesDto;
import project.dto.CommentRootDto;
import project.dto.CommentVoteDto;
import project.dto.ResponseDto;
import project.dto.returns.CommentCreateReturnDto;
import project.dto.returns.CommentGetReturnDto;
import project.dto.returns.CommentInfoDto;
import project.dto.returns.CommentRepliesReturnDto;
import project.dto.returns.CommentRootReturnDto;
import project.dto.returns.PaginationDto;
import project.dto.returns.ThreadListInfoReturnDto;
import project.threads.ThreadRepository;
import project.users.Token;
import project.threads.Thread;
import project.users.TokenRepository;
import project.users.User;
import project.users.UserRepository;
import project.utils.AuthUtils;
import project.utils.ThreadUtils;

@Service
public class CommentService {
    private ThreadRepository threadRepository;
    private CommentRepository commentRepository;
    private TokenRepository tokenRepository;
    private UserRepository userRepository;
    private static final int COMMENTS_PER_PAGE = 20;
    private static final int REPLIES_PER_PAGE = 5;

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

        Thread thread = ThreadUtils.loadThread(threadRepository, threadId);
        User user = AuthUtils.authenticate(tokenRepository, userRepository, tokenString);

        Comment parentComment = findParentComment(thread, parentCommentId);
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

    public ResponseDto getRootComments(CommentRootDto dto) {
        UUID threadId = dto.getThreadId();
        Integer pageNum = dto.getPageNum();
        String sort = dto.getSort();
        Thread thread = ThreadUtils.loadThread(threadRepository, threadId);

        Sort sorting = Sort.by("createdAt").descending();
        switch (sort.toLowerCase()) {
            case "likes":
                sorting = Sort.by("netLikeCount").descending();
                break;
            case "recent":
                sorting = Sort.by("createdAt").descending();
                break;
            case "oldest":
                sorting = Sort.by("createdAt").ascending();
                break;
            default:
                break;
        }

        Pageable pageable = PageRequest.of(pageNum - 1, COMMENTS_PER_PAGE, sorting);
        Page<Comment> page = commentRepository.findByThreadAndParentCommentIsNull(thread, pageable);
        long numComments = page.getTotalElements();
        int numPages = page.getTotalPages();
        List<Comment> rootComments = page.getContent();

        PaginationDto paginationInfo = new PaginationDto(numPages, pageNum, numComments);

        List<CommentInfoDto> pageInfo = new ArrayList<>();
        for (Comment comment : rootComments) {
            pageInfo.add(new CommentInfoDto(
                comment.getId(), 
                threadId, 
                comment.getCreatorId(), 
                comment.getText(),
                comment.getParentCommentId(), 
                comment.getNetLikeCount(), 
                comment.getCreatedAt()
            ));
        }

        CommentRootReturnDto response = new CommentRootReturnDto(paginationInfo, pageInfo);

        return new ResponseDto("Comments retrieved successfully", response);
    }

    public ResponseDto getReplies(CommentRepliesDto dto) {
        UUID commentId = dto.getCommentId();
        Integer pageNum = dto.getPageNum();
        Comment comment = ThreadUtils.loadComment(commentRepository, commentId);

        Sort sorting = Sort.by("netLikeCount").descending();
        Pageable pageable = PageRequest.of(pageNum - 1, REPLIES_PER_PAGE, sorting);
        Page<Comment> page = commentRepository.findByParentComment(comment, pageable);
        long numComments = page.getTotalElements();
        int numPages = page.getTotalPages();
        List<Comment> rootComments = page.getContent();

        PaginationDto paginationInfo = new PaginationDto(numPages, pageNum, numComments);

        List<CommentInfoDto> pageInfo = new ArrayList<>();
        for (Comment c : rootComments) {
            pageInfo.add(new CommentInfoDto(
                c.getId(), 
                c.getThreadId(), 
                c.getCreatorId(), 
                c.getText(),
                c.getParentCommentId(), 
                c.getNetLikeCount(), 
                c.getCreatedAt()
            ));
        }

        CommentRepliesReturnDto response = new CommentRepliesReturnDto(paginationInfo, pageInfo);

        return new ResponseDto("Replies retrieved successfully", response);
    }

    

    public ResponseDto getComments(CommentGetDto dto) {
        UUID threadID = dto.getThreadId();

        Thread thread = ThreadUtils.loadThread(threadRepository, threadID);

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

        User user = AuthUtils.authenticate(tokenRepository, userRepository, tokenString);
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

        User user = AuthUtils.authenticate(tokenRepository, userRepository, tokenString);
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

    private Comment findParentComment(Thread thread, UUID parentId) {
        if (parentId == null)
            return null;
        return thread.getComments().stream()
                .filter(c -> c.getId().equals(parentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment id"));
    }
}
