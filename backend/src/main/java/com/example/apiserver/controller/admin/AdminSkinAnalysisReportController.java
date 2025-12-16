package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.skin.SkinAnalysisReportRequest;
import com.example.apiserver.dto.skin.SkinAnalysisReportResponse;
import com.example.apiserver.service.SkinAnalysisReportService;
import com.example.apiserver.util.AdminUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ApiResponse는 전체 경로로 사용
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<SkinAnalysisReportResponse>>> getReports(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<SkinAnalysisReportResponse> reports = reportService.getReportsForAdmin(page, limit, search, userId, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(reports.getNumber() + 1)
                .limit(reports.getSize())
                .total(reports.getTotalElements())
                .totalPages(reports.getTotalPages())
                .build();
        
        PaginatedResponse<SkinAnalysisReportResponse> response = PaginatedResponse.<SkinAnalysisReportResponse>builder()
                .items(reports.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "피부분석 보고서 상세 조회", description = "관리자가 피부분석 보고서 상세 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> getReport(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        SkinAnalysisReportResponse report = reportService.getReport(id);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @Operation(summary = "피부분석 보고서 생성", description = "관리자가 피부분석 보고서 생성")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping
    public ResponseEntity<ApiResponse<SkinAnalysisReportResponse>> createReport(
            Authentication authentication,
            @Valid @RequestBody SkinAnalysisReportRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        SkinAnalysisReportResponse report = reportService.createReportForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(report));
    }

    @Operation(summary = "피부분석 보고서 수정", description = "관리자가 피부분석 보고서 수정")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteReport(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        reportService.deleteReportForAdmin(id);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("피부분석 보고서가 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("피부분석 보고서가 삭제되었습니다.", messageResponse));
    }
}

