package com.example.apiserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "프로필 이미지 응답")
public class ProfileImageResponse {
    @Schema(description = "프로필 이미지 URL", example = "https://s3.amazonaws.com/bucket/profile.jpg")
    private String profileImageUrl;
}
