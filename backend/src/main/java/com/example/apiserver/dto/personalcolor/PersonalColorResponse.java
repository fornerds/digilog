package com.example.apiserver.dto.personalcolor;

import com.example.apiserver.entity.PersonalColorColor;
import com.example.apiserver.entity.PersonalColorDiagnosis;
import com.example.apiserver.service.JsonConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalColorResponse {
    private Long id;
    private Long userId;
    private String personalColor;
    private Integer typePercentage;
    private List<String> typeDescriptions;
    private LabValues labValues;
    private MatchingColors matchingColors;
    private NonMatchingColors nonMatchingColors;
    private String contouringGuideUrl;
    private List<String> makeupTips;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
        private List<ColorInfo> colors;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NonMatchingColors {
        private String title;
        private String description;
        private List<ColorInfo> colors;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ColorInfo {
        private Long id;
        private String name;
        private String hexCode;
    }

    public static PersonalColorResponse from(PersonalColorDiagnosis diagnosis, 
                                             List<PersonalColorColor> matchingColorsList,
                                             List<PersonalColorColor> nonMatchingColorsList,
                                             JsonConverter jsonConverter) {
        LabValues labValues = LabValues.builder()
                .l(diagnosis.getLabL())
                .a(diagnosis.getLabA())
                .b(diagnosis.getLabB())
                .build();

        List<ColorInfo> matchingColorsInfo = matchingColorsList.stream()
                .map(c -> ColorInfo.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .hexCode(c.getHexCode())
                        .build())
                .collect(Collectors.toList());

        List<ColorInfo> nonMatchingColorsInfo = nonMatchingColorsList.stream()
                .map(c -> ColorInfo.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .hexCode(c.getHexCode())
                        .build())
                .collect(Collectors.toList());

        MatchingColors matchingColors = MatchingColors.builder()
                .title(diagnosis.getMatchingColorsTitle())
                .description(diagnosis.getMatchingColorsDescription())
                .colors(matchingColorsInfo)
                .build();

        NonMatchingColors nonMatchingColors = NonMatchingColors.builder()
                .title(diagnosis.getNonMatchingColorsTitle())
                .description(diagnosis.getNonMatchingColorsDescription())
                .colors(nonMatchingColorsInfo)
                .build();

        List<String> typeDescriptions = parseJsonArray(diagnosis.getTypeDescriptions(), jsonConverter);
        List<String> makeupTips = parseJsonArray(diagnosis.getMakeupTips(), jsonConverter);

        return PersonalColorResponse.builder()
                .id(diagnosis.getId())
                .userId(diagnosis.getUser() != null ? diagnosis.getUser().getId() : null)
                .personalColor(diagnosis.getPersonalColor())
                .typePercentage(diagnosis.getTypePercentage())
                .typeDescriptions(typeDescriptions)
                .labValues(labValues)
                .matchingColors(matchingColors)
                .nonMatchingColors(nonMatchingColors)
                .contouringGuideUrl(diagnosis.getContouringGuideImage() != null ? 
                        diagnosis.getContouringGuideImage().getUrl() : null)
                .makeupTips(makeupTips)
                .createdAt(diagnosis.getCreatedAt())
                .updatedAt(diagnosis.getUpdatedAt())
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

