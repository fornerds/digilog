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
@Schema(description = "일괄 생성 응답")
public class BatchCreateResponse {
    @Schema(description = "생성된 항목 수", example = "10")
    private int created;
    
    @Schema(description = "건너뛴 항목 수", example = "2")
    private int skipped;
    
    @Schema(description = "전체 항목 수", example = "12")
    private int total;
}
