package com.example.apiserver.service;

import com.example.apiserver.dto.comment.CommentResponse;
import com.example.apiserver.entity.Comment;
import com.example.apiserver.entity.Post;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.ForbiddenException;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.CommentRepository;
import com.example.apiserver.repository.PostRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.service.sort.CommentSortStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService extends BaseService<Comment, Long> {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentSortStrategyFactory sortStrategyFactory;

    @Override
    protected BaseRepository<Comment, Long> getRepository() {
        return commentRepository;
    }

    public Page<CommentResponse> getComments(Long postId, int page, int limit, String sortBy, String order) {
        // 게시글 존재 확인
        postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다"));

        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Comment> comments = sortStrategyFactory.sort(postId, sortBy, order, pageable);

        return comments.map(comment -> {
            User user = comment.getUser();
            return CommentResponse.from(comment, user);
        });
    }

    @Transactional
    public CommentResponse createComment(Long postId, Long userId, String content) {
        Post post = postRepository.findByIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment, user);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, Long userId, String content) {
        Comment comment = findById(commentId);

        // 작성자 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("댓글을 수정할 권한이 없습니다");
        }

        comment.updateContent(content);
        return CommentResponse.from(comment, comment.getUser());
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = findById(commentId);

        // 작성자 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("댓글을 삭제할 권한이 없습니다");
        }

        comment.softDelete();
        commentRepository.save(comment); // soft delete 후 저장
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }

    // 관리자용 메서드들
    public Page<CommentResponse> getCommentsForAdmin(int page, int limit, String search, Long postId, Long userId, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Comment> comments;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                comments = commentRepository.findAllWithFiltersOrderByUpdatedAt(postId, userId, search, pageable);
                break;
            default:
                comments = commentRepository.findAllWithFilters(postId, userId, search, pageable);
        }
        
        return comments.map(comment -> {
            User user = comment.getUser();
            return CommentResponse.from(comment, user);
        });
    }

    @Transactional
    public CommentResponse createCommentForAdmin(com.example.apiserver.dto.comment.CommentRequest.CreateAdmin request) {
        Post post = postRepository.findByIdAndIsDeletedFalse(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다"));

        User user = userRepository.findByIdAndIsDeletedFalse(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        // 관리자가 생성하는 댓글은 비속어 검사를 건너뜁니다
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment, user);
    }

    @Transactional
    public CommentResponse updateCommentForAdmin(Long commentId, String content) {
        Comment comment = findById(commentId);
        
        // 관리자가 수정하는 댓글은 비속어 검사를 건너뜁니다
        comment.updateContent(content);
        return CommentResponse.from(comment, comment.getUser());
    }

    @Transactional
    public void deleteCommentForAdmin(Long commentId) {
        Comment comment = findById(commentId);
        comment.softDelete();
        commentRepository.save(comment); // soft delete 후 저장
    }
}

