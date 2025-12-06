package com.example.apiserver.dto.notice;

import com.example.apiserver.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponse {
    private Long id;
    private String type;
    private String title;
    private String content;
    private List<String> images;
    private List<String> links;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeResponse from(Notice notice, List<String> imageUrls) {
        List<String> links = parseLinks(notice.getLinks());

        return NoticeResponse.builder()
                .id(notice.getId())
                .type(notice.getType())
                .title(notice.getTitle())
                .content(notice.getContent())
                .images(imageUrls != null ? imageUrls : List.of())
                .links(links)
                .startDate(notice.getStartDate())
                .endDate(notice.getEndDate())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }

    private static List<String> parseLinks(String linksJson) {
        if (linksJson == null || linksJson.isBlank()) {
            return List.of();
        }
        try {
            String cleaned = linksJson.trim();
            if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
                cleaned = cleaned.substring(1, cleaned.length() - 1);
                if (cleaned.isBlank()) {
                    return List.of();
                }
                return List.of(cleaned.split(","))
                        .stream()
                        .map(s -> s.trim().replaceAll("\"", ""))
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // 파싱 실패 시 빈 리스트 반환
        }
        return List.of();
    }
}

