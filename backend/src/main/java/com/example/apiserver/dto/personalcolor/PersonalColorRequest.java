package com.example.apiserver.dto.personalcolor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class PersonalColorRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateColor {
        @NotBlank(message = "색상명은 필수입니다")
        private String name;

        @NotBlank(message = "HEX 코드는 필수입니다")
        private String hexCode;

        @NotBlank(message = "카테고리는 필수입니다")
        private String category;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateColor {
        private String name;
        private String hexCode;
        private String category;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateDiagnosis {
        @NotNull(message = "사용자 ID는 필수입니다")
        private Long userId;

        @NotBlank(message = "퍼스널컬러 타입은 필수입니다")
        private String personalColor;

        @NotNull(message = "타입 확률은 필수입니다")
        private Integer typePercentage;

        @NotNull(message = "타입 설명은 필수입니다")
        private List<String> typeDescriptions;

        @NotNull(message = "LAB 값은 필수입니다")
        private LabValues labValues;

        @NotNull(message = "어울리는 색상은 필수입니다")
        private MatchingColors matchingColors;

        @NotNull(message = "안 어울리는 색상은 필수입니다")
        private NonMatchingColors nonMatchingColors;

        private String contouringGuideUrl;

        @NotNull(message = "화장팁은 필수입니다")
        private List<String> makeupTips;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LabValues {
            @NotNull(message = "L 값은 필수입니다")
            private BigDecimal l;

            @NotNull(message = "A 값은 필수입니다")
            private BigDecimal a;

            @NotNull(message = "B 값은 필수입니다")
            private BigDecimal b;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class MatchingColors {
            @NotBlank(message = "제목은 필수입니다")
            private String title;

            @NotBlank(message = "설명은 필수입니다")
            private String description;

            @NotNull(message = "색상 ID 배열은 필수입니다")
            private List<Long> colorIds;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class NonMatchingColors {
            @NotBlank(message = "제목은 필수입니다")
            private String title;

            @NotBlank(message = "설명은 필수입니다")
            private String description;

            @NotNull(message = "색상 ID 배열은 필수입니다")
            private List<Long> colorIds;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateDiagnosis {
        private String personalColor;
        private Integer typePercentage;
        private List<String> typeDescriptions;
        private LabValues labValues;
        private MatchingColors matchingColors;
        private NonMatchingColors nonMatchingColors;
        private String contouringGuideUrl;
        private List<String> makeupTips;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class LabValues {
            private BigDecimal l;
            private BigDecimal a;
            private BigDecimal b;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class MatchingColors {
            private String title;
            private String description;
            private List<Long> colorIds;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class NonMatchingColors {
            private String title;
            private String description;
            private List<Long> colorIds;
        }
    }
}

