package com.example.apiserver.service.sort;

import com.example.apiserver.entity.Notice;
import com.example.apiserver.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NoticeSortStrategyFactory {

    private final NoticeRepository noticeRepository;
    private Map<String, SortStrategy<Notice>> strategies;

    private void initializeStrategies() {
        if (strategies == null) {
            strategies = new HashMap<>();
            
            // 기본 정렬 (createdAt)
            strategies.put("createdAt", new DefaultNoticeSortStrategy(noticeRepository));
            
            // updatedAt 정렬
            strategies.put("updatedAt", new UpdatedAtNoticeSortStrategy(noticeRepository));
            
            // startDate 정렬
            strategies.put("startDate", new StartDateNoticeSortStrategy(noticeRepository));
            
            // endDate 정렬
            strategies.put("endDate", new EndDateNoticeSortStrategy(noticeRepository));
            
            // title 정렬
            strategies.put("title", new TitleNoticeSortStrategy(noticeRepository));
        }
    }

    public Page<Notice> sort(String sortBy, String type, String search, Pageable pageable) {
        initializeStrategies();
        SortStrategy<Notice> strategy = strategies.getOrDefault(sortBy, strategies.get("createdAt"));
        return strategy.sort(pageable, search, type);
    }

    // 기본 정렬 전략 (createdAt)
    @RequiredArgsConstructor
    private static class DefaultNoticeSortStrategy implements SortStrategy<Notice> {
        private final NoticeRepository repository;

        @Override
        public Page<Notice> sort(Pageable pageable, String search, Object... params) {
            String type = (String) params[0];
            return repository.findAllWithFilters(type, search, pageable);
        }

        @Override
        public String getSortType() {
            return "createdAt";
        }
    }

    // updatedAt 정렬 전략
    @RequiredArgsConstructor
    private static class UpdatedAtNoticeSortStrategy implements SortStrategy<Notice> {
        private final NoticeRepository repository;

        @Override
        public Page<Notice> sort(Pageable pageable, String search, Object... params) {
            String type = (String) params[0];
            return repository.findAllWithFiltersOrderByUpdatedAt(type, search, pageable);
        }

        @Override
        public String getSortType() {
            return "updatedAt";
        }
    }

    // startDate 정렬 전략
    @RequiredArgsConstructor
    private static class StartDateNoticeSortStrategy implements SortStrategy<Notice> {
        private final NoticeRepository repository;

        @Override
        public Page<Notice> sort(Pageable pageable, String search, Object... params) {
            String type = (String) params[0];
            return repository.findAllWithFiltersOrderByStartDate(type, search, pageable);
        }

        @Override
        public String getSortType() {
            return "startDate";
        }
    }

    // endDate 정렬 전략
    @RequiredArgsConstructor
    private static class EndDateNoticeSortStrategy implements SortStrategy<Notice> {
        private final NoticeRepository repository;

        @Override
        public Page<Notice> sort(Pageable pageable, String search, Object... params) {
            String type = (String) params[0];
            return repository.findAllWithFiltersOrderByEndDate(type, search, pageable);
        }

        @Override
        public String getSortType() {
            return "endDate";
        }
    }

    // title 정렬 전략
    @RequiredArgsConstructor
    private static class TitleNoticeSortStrategy implements SortStrategy<Notice> {
        private final NoticeRepository repository;

        @Override
        public Page<Notice> sort(Pageable pageable, String search, Object... params) {
            String type = (String) params[0];
            return repository.findAllWithFiltersOrderByTitle(type, search, pageable);
        }

        @Override
        public String getSortType() {
            return "title";
        }
    }
}

