package com.example.apiserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비속어 검사 응답")
public class ProfanityCheckResponse {
    @Schema(description = "비속어 감지 여부", example = "false")
    private boolean hasProfanity;
    
    @Schema(description = "감지된 비속어 목록", example = "[]")
    private List<String> detectedWords;
    
    @Schema(description = "검사 결과 메시지", example = "비속어가 감지되지 않았습니다.")
    private String message;
}
