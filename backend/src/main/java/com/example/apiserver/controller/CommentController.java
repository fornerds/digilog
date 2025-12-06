package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.comment.CommentRequest;
import com.example.apiserver.dto.comment.CommentResponse;
import com.example.apiserver.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "댓글", description = "댓글 관련 API")
@RestController
@RequestMapping("/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 댓글 목록 조회 (페이지네이션 지원)")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        
        Page<CommentResponse> comments = commentService.getComments(postId, page, limit, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("comments", comments.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", comments.getNumber() + 1);
        pagination.put("limit", comments.getSize());
        pagination.put("total", comments.getTotalElements());
        pagination.put("totalPages", comments.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "댓글 생성", description = "댓글 생성")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest.Create request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        CommentResponse comment = commentService.createComment(postId, userId, request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest.Update request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        CommentResponse comment = commentService.updateComment(id, userId, request.getContent());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        commentService.deleteComment(id, userId);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "댓글이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", data));
    }
}

