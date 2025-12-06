package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.post.PostRequest;
import com.example.apiserver.dto.post.PostResponse;
import com.example.apiserver.service.PostService;
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
@Tag(name = "관리자 - 게시글 관리", description = "관리자 게시글 관리 API")
@RestController
@RequestMapping("/v1/admin/posts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminPostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "관리자가 전체 게시글 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPosts(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<PostResponse> posts = postService.getPostsForAdmin(page, limit, search, authorId, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("posts", posts.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", posts.getNumber() + 1);
        pagination.put("limit", posts.getSize());
        pagination.put("total", posts.getTotalElements());
        pagination.put("totalPages", posts.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "게시글 상세 조회", description = "관리자가 게시글 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        PostResponse post = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 생성", description = "관리자가 게시글 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            Authentication authentication,
            @Valid @RequestBody PostRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        PostResponse post = postService.createPostForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 수정", description = "관리자가 게시글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PostRequest.UpdateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        PostResponse post = postService.updatePostForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 삭제", description = "관리자가 게시글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deletePost(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        postService.deletePostForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "게시글이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("게시글이 삭제되었습니다.", data));
    }
}

