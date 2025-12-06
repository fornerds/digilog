package com.example.apiserver.dto.skin;

import com.example.apiserver.entity.SkinAnalysisReport;
import com.example.apiserver.entity.User;
import com.example.apiserver.service.JsonConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkinAnalysisReportResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Integer userAge;
    private Integer skinAge;
    private String skinCondition;
    private String skinConditionDescription;
    private String skinCode;
    private String skinType;
    private AnalysisScores scores;
    private List<String> skinTags;
    private String skinCodeDescription;
    private List<String> careTips;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalysisScores {
        private Integer pores;
        private Integer blackheads;
        private Integer pigmentation;
        private Integer wrinkles;
        private Integer porphyrin;
        private Integer sensitivity;
        private Integer darkCircles;
    }

    public static SkinAnalysisReportResponse from(SkinAnalysisReport report, User user, JsonConverter jsonConverter) {
        AnalysisScores scores = AnalysisScores.builder()
                .pores(report.getScorePores())
                .blackheads(report.getScoreBlackheads())
                .pigmentation(report.getScorePigmentation())
                .wrinkles(report.getScoreWrinkles())
                .porphyrin(report.getScorePorphyrin())
                .sensitivity(report.getScoreSensitivity())
                .darkCircles(report.getScoreDarkCircles())
                .build();

        List<String> skinTags = parseJsonArray(report.getSkinTags(), jsonConverter);
        List<String> careTips = parseJsonArray(report.getCareTips(), jsonConverter);

        return SkinAnalysisReportResponse.builder()
                .id(report.getId())
                .userId(user != null ? user.getId() : null)
                .userName(user != null ? user.getName() : null)
                .userAge(report.getUserAge())
                .skinAge(report.getSkinAge())
                .skinCondition(report.getSkinCondition())
                .skinConditionDescription(report.getSkinConditionDescription())
                .skinCode(report.getSkinCode())
                .skinType(report.getSkinType())
                .scores(scores)
                .skinTags(skinTags)
                .skinCodeDescription(report.getSkinCodeDescription())
                .careTips(careTips)
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }

    private static List<String> parseJsonArray(String json, JsonConverter jsonConverter) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return jsonConverter.fromJsonArray(json, String.class);
        } catch (Exception e) {
            return List.of();
        }
    }
}

