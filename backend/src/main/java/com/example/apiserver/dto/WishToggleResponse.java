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
@Schema(description = "제품 찜 토글 응답")
public class WishToggleResponse {
    @Schema(description = "제품 ID", example = "1")
    private Long productId;
    
    @Schema(description = "찜 여부", example = "true")
    private boolean isWished;
    
    @Schema(description = "응답 메시지", example = "제품이 찜 목록에 추가되었습니다.")
    private String message;
}
