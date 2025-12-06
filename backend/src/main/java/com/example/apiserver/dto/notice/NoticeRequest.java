package com.example.apiserver.dto.notice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @NotBlank(message = "타입은 필수입니다")
        private String type; // "notice" or "event"

        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private List<String> imageUrls;
        private List<String> links;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private String type;
        private String title;
        private String content;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private List<String> imageUrls;
        private List<String> links;
    }
}

