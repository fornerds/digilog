package com.example.apiserver.service.sort;

import com.example.apiserver.entity.Comment;
import com.example.apiserver.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommentSortStrategyFactory {

    private final CommentRepository commentRepository;
    private Map<String, SortStrategy<Comment>> strategies;

    private void initializeStrategies() {
        if (strategies == null) {
            strategies = new HashMap<>();
            
            // 기본 정렬 (createdAt asc)
            strategies.put("createdAt", new DefaultCommentSortStrategy(commentRepository));
            
            // updatedAt 정렬
            strategies.put("updatedAt", new UpdatedAtCommentSortStrategy(commentRepository));
        }
    }

    public Page<Comment> sort(Long postId, String sortBy, String order, Pageable pageable) {
        initializeStrategies();
        
        // order 파라미터에 따라 정렬 방향 결정
        if ("updatedAt".equals(sortBy)) {
            return strategies.get("updatedAt").sort(pageable, null, postId);
        } else if ("desc".equalsIgnoreCase(order)) {
            return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        } else {
            return strategies.get("createdAt").sort(pageable, null, postId);
        }
    }

    // 기본 정렬 전략 (createdAt asc)
    @RequiredArgsConstructor
    private static class DefaultCommentSortStrategy implements SortStrategy<Comment> {
        private final CommentRepository repository;

        @Override
        public Page<Comment> sort(Pageable pageable, String search, Object... params) {
            Long postId = (Long) params[0];
            return repository.findByPostIdOrderByCreatedAtAsc(postId, pageable);
        }

        @Override
        public String getSortType() {
            return "createdAt";
        }
    }

    // updatedAt 정렬 전략
    @RequiredArgsConstructor
    private static class UpdatedAtCommentSortStrategy implements SortStrategy<Comment> {
        private final CommentRepository repository;

        @Override
        public Page<Comment> sort(Pageable pageable, String search, Object... params) {
            Long postId = (Long) params[0];
            return repository.findByPostIdOrderByUpdatedAtDesc(postId, pageable);
        }

        @Override
        public String getSortType() {
            return "updatedAt";
        }
    }
}

