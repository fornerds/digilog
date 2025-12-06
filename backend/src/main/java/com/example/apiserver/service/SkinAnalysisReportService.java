package com.example.apiserver.service;

import com.example.apiserver.dto.skin.SkinAnalysisReportRequest;
import com.example.apiserver.dto.skin.SkinAnalysisReportResponse;
import com.example.apiserver.entity.SkinAnalysisReport;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.SkinAnalysisReportRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.service.sort.SkinAnalysisReportSortStrategyFactory;
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
public class SkinAnalysisReportService extends BaseService<SkinAnalysisReport, Long> {

    private final SkinAnalysisReportRepository reportRepository;
    private final UserRepository userRepository;
    private final SkinAnalysisReportSortStrategyFactory sortStrategyFactory;
    private final JsonConverter jsonConverter;

    @Override
    protected BaseRepository<SkinAnalysisReport, Long> getRepository() {
        return reportRepository;
    }

    public Page<SkinAnalysisReportResponse> getReports(int page, int limit, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<SkinAnalysisReport> reports = sortStrategyFactory.sort(sortBy != null ? sortBy : "createdAt", pageable);

        return reports.map(report -> {
            User user = report.getUser();
            return SkinAnalysisReportResponse.from(report, user, jsonConverter);
        });
    }

    public SkinAnalysisReportResponse getReport(Long reportId) {
        SkinAnalysisReport report = findById(reportId);
        User user = report.getUser();
        return SkinAnalysisReportResponse.from(report, user, jsonConverter);
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }

    // 관리자용 메서드들
    public Page<SkinAnalysisReportResponse> getReportsForAdmin(int page, int limit, String search, Long userId, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<SkinAnalysisReport> reports;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                reports = reportRepository.findAllWithFiltersOrderByUpdatedAt(userId, search, pageable);
                break;
            default:
                reports = reportRepository.findAllWithFilters(userId, search, pageable);
        }
        
        return reports.map(report -> {
            User user = report.getUser();
            return SkinAnalysisReportResponse.from(report, user, jsonConverter);
        });
    }

    @Transactional
    public SkinAnalysisReportResponse createReportForAdmin(SkinAnalysisReportRequest.CreateAdmin request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        String skinTagsJson = request.getSkinTags() != null ? jsonConverter.toJson(request.getSkinTags()) : null;
        String careTipsJson = jsonConverter.toJson(request.getCareTips());

        SkinAnalysisReport report = SkinAnalysisReport.builder()
                .user(user)
                .userAge(request.getUserAge())
                .skinAge(request.getSkinAge())
                .skinCondition(request.getSkinCondition())
                .skinConditionDescription(request.getSkinConditionDescription())
                .skinCode(request.getSkinCode())
                .skinType(request.getSkinType())
                .scorePores(request.getAnalysisScores().getPores())
                .scoreBlackheads(request.getAnalysisScores().getBlackheads())
                .scorePigmentation(request.getAnalysisScores().getPigmentation())
                .scoreWrinkles(request.getAnalysisScores().getWrinkles())
                .scorePorphyrin(request.getAnalysisScores().getPorphyrin())
                .scoreSensitivity(request.getAnalysisScores().getSensitivity())
                .scoreDarkCircles(request.getAnalysisScores().getDarkCircles())
                .skinTags(skinTagsJson)
                .skinCodeDescription(request.getSkinCodeDescription())
                .careTips(careTipsJson)
                .build();

        SkinAnalysisReport savedReport = reportRepository.save(report);
        return SkinAnalysisReportResponse.from(savedReport, user, jsonConverter);
    }

    @Transactional
    public SkinAnalysisReportResponse updateReportForAdmin(Long reportId, SkinAnalysisReportRequest.UpdateAdmin request) {
        SkinAnalysisReport report = findById(reportId);

        if (request.getUserAge() != null) {
            report.updateUserAge(request.getUserAge());
        }
        if (request.getSkinAge() != null) {
            report.updateSkinAge(request.getSkinAge());
        }
        if (request.getSkinCondition() != null) {
            report.updateSkinCondition(request.getSkinCondition());
        }
        if (request.getSkinConditionDescription() != null) {
            report.updateSkinConditionDescription(request.getSkinConditionDescription());
        }
        if (request.getSkinCode() != null) {
            report.updateSkinCode(request.getSkinCode());
        }
        if (request.getSkinType() != null) {
            report.updateSkinType(request.getSkinType());
        }
        if (request.getAnalysisScores() != null) {
            SkinAnalysisReportRequest.UpdateAdmin.AnalysisScores scores = request.getAnalysisScores();
            report.updateScores(
                    scores.getPores(),
                    scores.getBlackheads(),
                    scores.getPigmentation(),
                    scores.getWrinkles(),
                    scores.getPorphyrin(),
                    scores.getSensitivity(),
                    scores.getDarkCircles()
            );
        }
        if (request.getSkinTags() != null) {
            report.updateSkinTags(jsonConverter.toJson(request.getSkinTags()));
        }
        if (request.getSkinCodeDescription() != null) {
            report.updateSkinCodeDescription(request.getSkinCodeDescription());
        }
        if (request.getCareTips() != null) {
            report.updateCareTips(jsonConverter.toJson(request.getCareTips()));
        }

        User user = report.getUser();
        return SkinAnalysisReportResponse.from(report, user, jsonConverter);
    }

    @Transactional
    public void deleteReportForAdmin(Long reportId) {
        SkinAnalysisReport report = findById(reportId);
        report.softDelete();
    }
}

