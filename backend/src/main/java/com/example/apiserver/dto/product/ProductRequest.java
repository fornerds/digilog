package com.example.apiserver.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @Getter
    @NoArgsConstructor
    public static class ToggleWish {
        @NotNull(message = "isWished는 필수입니다")
        @Schema(description = "찜 여부", example = "true")
        private Boolean isWished;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateAdmin {
        @NotBlank(message = "제품명은 필수입니다")
        private String name;

        @NotBlank(message = "브랜드명은 필수입니다")
        private String brand;

        @NotNull(message = "가격은 필수입니다")
        private Long price;

        @NotBlank(message = "이미지 URL은 필수입니다")
        private String imageUrl;

        @NotBlank(message = "제품 URL은 필수입니다")
        private String url;

        private String category;
        private List<String> tags;
        private List<String> skinCodes;
        private List<String> personalColors;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateAdmin {
        private String name;
        private String brand;
        private Long price;
        private String imageUrl;
        private String url;
        private String category;
        private List<String> tags;
        private List<String> skinCodes;
        private List<String> personalColors;
    }
}

