package com.example.apiserver.dto.banner;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BannerRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        @NotBlank(message = "제목은 필수입니다")
        private String title;

        @NotBlank(message = "설명은 필수입니다")
        private String description;

        @NotBlank(message = "이미지 URL은 필수입니다")
        private String imageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        private String title;
        private String description;
        private String imageUrl;
    }
}

