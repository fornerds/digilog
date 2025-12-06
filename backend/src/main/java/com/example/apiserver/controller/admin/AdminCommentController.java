package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.comment.CommentRequest;
import com.example.apiserver.dto.comment.CommentResponse;
import com.example.apiserver.service.CommentService;
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
@Tag(name = "관리자 - 댓글 관리", description = "관리자 댓글 관리 API")
@RestController
@RequestMapping("/v1/admin/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminCommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "관리자가 전체 댓글 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<CommentResponse> comments = commentService.getCommentsForAdmin(page, limit, search, postId, userId, sortBy, order);
        
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

    @Operation(summary = "댓글 상세 조회", description = "관리자가 댓글 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        com.example.apiserver.entity.Comment comment = commentService.findById(id);
        com.example.apiserver.entity.User user = comment.getUser();
        CommentResponse response = CommentResponse.from(comment, user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "댓글 생성", description = "관리자가 댓글 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            Authentication authentication,
            @Valid @RequestBody CommentRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        CommentResponse comment = commentService.createCommentForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 수정", description = "관리자가 댓글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest.Update request) {
        AdminUtil.checkAdminRole(authentication);
        
        CommentResponse comment = commentService.updateCommentForAdmin(id, request.getContent());
        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @Operation(summary = "댓글 삭제", description = "관리자가 댓글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteComment(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        commentService.deleteCommentForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "댓글이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", data));
    }
}

