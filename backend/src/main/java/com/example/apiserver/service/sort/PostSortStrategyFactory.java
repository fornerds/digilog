package com.example.apiserver.service.sort;

import com.example.apiserver.entity.Post;
import com.example.apiserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostSortStrategyFactory {

    private final PostRepository postRepository;
    private Map<String, SortStrategy<Post>> strategies;

    private void initializeStrategies() {
        if (strategies == null) {
            strategies = new HashMap<>();
            
            // 기본 정렬 (createdAt)
            strategies.put("createdAt", new DefaultPostSortStrategy(postRepository));
            
            // updatedAt 정렬
            strategies.put("updatedAt", new UpdatedAtPostSortStrategy(postRepository));
            
            // title 정렬
            strategies.put("title", new TitlePostSortStrategy(postRepository));
            
            // popularity 정렬 (특별 처리 필요)
            strategies.put("popularity", new PopularityPostSortStrategy(postRepository));
        }
    }

    public Page<Post> sort(String sortBy, String search, Pageable pageable) {
        initializeStrategies();
        SortStrategy<Post> strategy = strategies.getOrDefault(sortBy, strategies.get("createdAt"));
        return strategy.sort(pageable, search);
    }

    // 기본 정렬 전략 (createdAt)
    @RequiredArgsConstructor
    private static class DefaultPostSortStrategy implements SortStrategy<Post> {
        private final PostRepository repository;

        @Override
        public Page<Post> sort(Pageable pageable, String search, Object... params) {
            return repository.findAllWithSearch(search, pageable);
        }

        @Override
        public String getSortType() {
            return "createdAt";
        }
    }

    // updatedAt 정렬 전략
    @RequiredArgsConstructor
    private static class UpdatedAtPostSortStrategy implements SortStrategy<Post> {
        private final PostRepository repository;

        @Override
        public Page<Post> sort(Pageable pageable, String search, Object... params) {
            return repository.findAllWithSearchOrderByUpdatedAt(search, pageable);
        }

        @Override
        public String getSortType() {
            return "updatedAt";
        }
    }

    // title 정렬 전략
    @RequiredArgsConstructor
    private static class TitlePostSortStrategy implements SortStrategy<Post> {
        private final PostRepository repository;

        @Override
        public Page<Post> sort(Pageable pageable, String search, Object... params) {
            return repository.findAllWithSearchOrderByTitle(search, pageable);
        }

        @Override
        public String getSortType() {
            return "title";
        }
    }

    // popularity 정렬 전략 (특별 처리)
    @RequiredArgsConstructor
    private static class PopularityPostSortStrategy implements SortStrategy<Post> {
        private final PostRepository repository;

        @Override
        public Page<Post> sort(Pageable pageable, String search, Object... params) {
            // popularity는 PostService에서 별도 처리 필요 (좋아요 수 + 댓글 수 계산)
            // 여기서는 기본 정렬 반환
            return repository.findAllWithSearch(search, pageable);
        }

        @Override
        public String getSortType() {
            return "popularity";
        }
    }
}

