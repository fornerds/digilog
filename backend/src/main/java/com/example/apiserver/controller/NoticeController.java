package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.notice.NoticeResponse;
import com.example.apiserver.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ApiResponse는 전체 경로로 사용
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "공지/이벤트", description = "공지/이벤트 관련 API")
@RestController
@RequestMapping("/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지/이벤트 목록 조회", description = "공지/이벤트 목록 조회 (페이지네이션 지원)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<NoticeResponse>>> getNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Page<NoticeResponse> notices = noticeService.getNotices(page, limit, type, null, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(notices.getNumber() + 1)
                .limit(notices.getSize())
                .total(notices.getTotalElements())
                .totalPages(notices.getTotalPages())
                .build();
        
        PaginatedResponse<NoticeResponse> response = PaginatedResponse.<NoticeResponse>builder()
                .items(notices.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "공지/이벤트 상세 조회", description = "특정 공지/이벤트 상세 정보 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공지/이벤트를 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNotice(@PathVariable Long id) {
        NoticeResponse notice = noticeService.getNotice(id);
        return ResponseEntity.ok(ApiResponse.success(notice));
    }
}

