package com.example.apiserver.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        private List<String> hashtags;
        private List<String> imageUrls;
        @Builder.Default
        private Boolean forcePublish = false;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private String title;
        private String content;
        private List<String> hashtags;
        private List<String> imageUrls;
        @Builder.Default
        private Boolean forcePublish = false;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CheckProfanity {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        private List<String> hashtags;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateAdmin {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "내용은 필수입니다")
        private String content;

        @NotBlank(message = "작성자 ID는 필수입니다")
        private Long authorId;

        private List<String> hashtags;
        private List<String> imageUrls;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateAdmin {
        private String title;
        private String content;
        private List<String> hashtags;
        private List<String> imageUrls;
    }
}

