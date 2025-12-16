package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.comment.CommentRequest;
import com.example.apiserver.dto.comment.CommentResponse;
import com.example.apiserver.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ApiResponse는 전체 경로로 사용
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<CommentResponse>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        
        Page<CommentResponse> comments = commentService.getComments(postId, page, limit, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(comments.getNumber() + 1)
                .limit(comments.getSize())
                .total(comments.getTotalElements())
                .totalPages(comments.getTotalPages())
                .build();
        
        PaginatedResponse<CommentResponse> response = PaginatedResponse.<CommentResponse>builder()
                .items(comments.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "댓글 생성", description = "댓글 생성")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "댓글 생성 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"댓글 내용은 필수입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "댓글을 수정할 권한이 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"댓글을 수정할 권한이 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "댓글을 삭제할 권한이 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"댓글을 삭제할 권한이 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        commentService.deleteComment(id, userId);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("댓글이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", messageResponse));
    }
}

