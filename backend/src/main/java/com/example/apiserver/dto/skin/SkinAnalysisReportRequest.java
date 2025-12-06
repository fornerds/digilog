package com.example.apiserver.dto.skin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SkinAnalysisReportRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateAdmin {
        @NotNull(message = "사용자 ID는 필수입니다")
        private Long userId;

        @NotNull(message = "사용자 나이는 필수입니다")
        private Integer userAge;

        @NotNull(message = "피부나이는 필수입니다")
        private Integer skinAge;

        @NotBlank(message = "피부상태는 필수입니다")
        private String skinCondition;

        @NotBlank(message = "피부 상태 설명은 필수입니다")
        private String skinConditionDescription;

        @NotBlank(message = "피부코드는 필수입니다")
        private String skinCode;

        @NotBlank(message = "피부타입은 필수입니다")
        private String skinType;

        @NotNull(message = "분석 점수는 필수입니다")
        private AnalysisScores analysisScores;

        private List<String> skinTags;

        @NotBlank(message = "피부코드 설명은 필수입니다")
        private String skinCodeDescription;

        @NotNull(message = "피부관리 팁은 필수입니다")
        private List<String> careTips;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class AnalysisScores {
            @NotNull(message = "모공 점수는 필수입니다")
            private Integer pores;

            @NotNull(message = "여드름 점수는 필수입니다")
            private Integer blackheads;

            @NotNull(message = "색소 침착 점수는 필수입니다")
            private Integer pigmentation;

            @NotNull(message = "주름 점수는 필수입니다")
            private Integer wrinkles;

            @NotNull(message = "포르피린 점수는 필수입니다")
            private Integer porphyrin;

            @NotNull(message = "민감도 점수는 필수입니다")
            private Integer sensitivity;

            @NotNull(message = "다크서클 점수는 필수입니다")
            private Integer darkCircles;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateAdmin {
        private Integer userAge;
        private Integer skinAge;
        private String skinCondition;
        private String skinConditionDescription;
        private String skinCode;
        private String skinType;
        private AnalysisScores analysisScores;
        private List<String> skinTags;
        private String skinCodeDescription;
        private List<String> careTips;

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
    }
}

