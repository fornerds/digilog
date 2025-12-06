package com.example.apiserver.service.sort;

import com.example.apiserver.entity.SkinAnalysisReport;
import com.example.apiserver.repository.SkinAnalysisReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SkinAnalysisReportSortStrategyFactory {

    private final SkinAnalysisReportRepository reportRepository;
    private Map<String, SortStrategy<SkinAnalysisReport>> strategies;

    private void initializeStrategies() {
        if (strategies == null) {
            strategies = new HashMap<>();
            
            // 기본 정렬 (createdAt)
            strategies.put("createdAt", new DefaultSkinAnalysisReportSortStrategy(reportRepository));
            
            // updatedAt 정렬
            strategies.put("updatedAt", new UpdatedAtSkinAnalysisReportSortStrategy(reportRepository));
        }
    }

    public Page<SkinAnalysisReport> sort(String sortBy, Pageable pageable) {
        initializeStrategies();
        SortStrategy<SkinAnalysisReport> strategy = strategies.getOrDefault(sortBy, strategies.get("createdAt"));
        return strategy.sort(pageable, null);
    }

    // 기본 정렬 전략 (createdAt)
    @RequiredArgsConstructor
    private static class DefaultSkinAnalysisReportSortStrategy implements SortStrategy<SkinAnalysisReport> {
        private final SkinAnalysisReportRepository repository;

        @Override
        public Page<SkinAnalysisReport> sort(Pageable pageable, String search, Object... params) {
            return repository.findAllActive(pageable);
        }

        @Override
        public String getSortType() {
            return "createdAt";
        }
    }

    // updatedAt 정렬 전략
    @RequiredArgsConstructor
    private static class UpdatedAtSkinAnalysisReportSortStrategy implements SortStrategy<SkinAnalysisReport> {
        private final SkinAnalysisReportRepository repository;

        @Override
        public Page<SkinAnalysisReport> sort(Pageable pageable, String search, Object... params) {
            return repository.findAllActiveOrderByUpdatedAt(pageable);
        }

        @Override
        public String getSortType() {
            return "updatedAt";
        }
    }
}

