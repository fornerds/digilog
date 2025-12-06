package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.skin.SkinAnalysisReportResponse;
import com.example.apiserver.service.SkinAnalysisReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "피부분석 보고서", description = "피부분석 보고서 관련 API")
@RestController
@RequestMapping("/v1/skin-analysis-reports")
@RequiredArgsConstructor
public class SkinAnalysisReportController {

    private final SkinAnalysisReportService reportService;

    @Operation(summary = "피부분석 보고서 목록 조회", description = "피부분석 보고서 목록 조회 (페이지네이션 지원)")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReports(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Page<SkinAnalysisReportResponse> reports = reportService.getReports(page, limit, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("reports", reports.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", reports.getNumber() + 1);
        pagination.put("limit", reports.getSize());
        pagination.put("total", reports.getTotalElements());
        pagination.put("totalPages", reports.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "피부분석 보고서 상세 조회", description = "특정 피부분석 보고서 상세 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> getReport(@PathVariable Long id) {
        SkinAnalysisReportResponse report = reportService.getReport(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}

