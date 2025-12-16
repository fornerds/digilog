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
@Schema(description = "페이지네이션 정보")
public class PaginationInfo {
    @Schema(description = "현재 페이지 번호 (1부터 시작)", example = "1")
    private int page;
    
    @Schema(description = "페이지당 항목 수", example = "10")
    private int limit;
    
    @Schema(description = "전체 항목 수", example = "100")
    private long total;
    
    @Schema(description = "전체 페이지 수", example = "10")
    private int totalPages;
}
