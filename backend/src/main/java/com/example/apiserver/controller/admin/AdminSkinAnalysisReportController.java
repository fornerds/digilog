package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.skin.SkinAnalysisReportRequest;
import com.example.apiserver.dto.skin.SkinAnalysisReportResponse;
import com.example.apiserver.service.SkinAnalysisReportService;
import com.example.apiserver.util.AdminUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "관리자 - 피부분석 보고서 관리", description = "관리자 피부분석 보고서 관리 API")
@RestController
@RequestMapping("/v1/admin/skin-analysis-reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminSkinAnalysisReportController {

    private final SkinAnalysisReportService reportService;

    @Operation(summary = "피부분석 보고서 목록 조회", description = "관리자가 피부분석 보고서 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReports(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<SkinAnalysisReportResponse> reports = reportService.getReportsForAdmin(page, limit, search, userId, sortBy, order);
        
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

    @Operation(summary = "피부분석 보고서 상세 조회", description = "관리자가 피부분석 보고서 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> getReport(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        SkinAnalysisReportResponse report = reportService.getReport(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @Operation(summary = "피부분석 보고서 생성", description = "관리자가 피부분석 보고서 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> createReport(
            Authentication authentication,
            @Valid @RequestBody SkinAnalysisReportRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        SkinAnalysisReportResponse report = reportService.createReportForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(report));
    }

    @Operation(summary = "피부분석 보고서 수정", description = "관리자가 피부분석 보고서 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> updateReport(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody SkinAnalysisReportRequest.UpdateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        SkinAnalysisReportResponse report = reportService.updateReportForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @Operation(summary = "피부분석 보고서 삭제", description = "관리자가 피부분석 보고서 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteReport(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        reportService.deleteReportForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "피부분석 보고서가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("피부분석 보고서가 삭제되었습니다.", data));
    }
}

