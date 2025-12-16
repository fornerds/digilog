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
@Schema(description = "페이지네이션된 목록 응답")
public class PaginatedResponse<T> {
    @Schema(description = "항목 목록")
    private List<T> items;
    
    @Schema(description = "페이지네이션 정보")
    private PaginationInfo pagination;
}
