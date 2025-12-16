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
@Schema(description = "이메일 중복 확인 응답")
public class EmailCheckResponse {
    @Schema(description = "확인한 이메일 주소", example = "user@example.com")
    private String email;
    
    @Schema(description = "사용 가능 여부", example = "true")
    private boolean available;
}
