package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.notice.NoticeRequest;
import com.example.apiserver.dto.notice.NoticeResponse;
import com.example.apiserver.service.NoticeService;
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
@Tag(name = "관리자 - 공지/이벤트 관리", description = "관리자 공지/이벤트 관리 API")
@RestController
@RequestMapping("/v1/admin/notices")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지/이벤트 목록 조회", description = "관리자가 공지/이벤트 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotices(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<NoticeResponse> notices = noticeService.getNotices(page, limit, type, search, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("notices", notices.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", notices.getNumber() + 1);
        pagination.put("limit", notices.getSize());
        pagination.put("total", notices.getTotalElements());
        pagination.put("totalPages", notices.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "공지/이벤트 상세 조회", description = "관리자가 공지/이벤트 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNotice(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        NoticeResponse notice = noticeService.getNotice(id);
        return ResponseEntity.ok(ApiResponse.success(notice));
    }

    @Operation(summary = "공지/이벤트 생성", description = "관리자가 공지/이벤트 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(
            Authentication authentication,
            @Valid @RequestBody NoticeRequest.Create request) {
        AdminUtil.checkAdminRole(authentication);
        
        NoticeResponse notice = noticeService.createNotice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(notice));
    }

    @Operation(summary = "공지/이벤트 수정", description = "관리자가 공지/이벤트 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> updateNotice(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody NoticeRequest.Update request) {
        AdminUtil.checkAdminRole(authentication);
        
        NoticeResponse notice = noticeService.updateNotice(id, request);
        return ResponseEntity.ok(ApiResponse.success(notice));
    }

    @Operation(summary = "공지/이벤트 삭제", description = "관리자가 공지/이벤트 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteNotice(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        noticeService.deleteNotice(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "공지/이벤트가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("공지/이벤트가 삭제되었습니다.", data));
    }
}

