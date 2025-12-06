package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.notice.NoticeResponse;
import com.example.apiserver.service.NoticeService;
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
@Tag(name = "공지/이벤트", description = "공지/이벤트 관련 API")
@RestController
@RequestMapping("/v1/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지/이벤트 목록 조회", description = "공지/이벤트 목록 조회 (페이지네이션 지원)")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Page<NoticeResponse> notices = noticeService.getNotices(page, limit, type, null, sortBy, order);
        
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

    @Operation(summary = "공지/이벤트 상세 조회", description = "특정 공지/이벤트 상세 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNotice(@PathVariable Long id) {
        NoticeResponse notice = noticeService.getNotice(id);
        return ResponseEntity.ok(ApiResponse.success(notice));
    }
}

