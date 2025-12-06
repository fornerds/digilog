package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.post.PostRequest;
import com.example.apiserver.dto.post.PostResponse;
import com.example.apiserver.service.PostService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록 조회 (페이지네이션 지원)")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Page<PostResponse> posts = postService.getPosts(page, limit, search, sortBy, order);
        
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

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글 상세 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse post = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 사진 업로드", description = "게시글용 사진 업로드 (S3 사용)")
    @PostMapping("/images")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadImages(
            @RequestParam("images") List<MultipartFile> images,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<String> imageUrls = postService.uploadPostImages(userId, images);
        
        Map<String, Object> data = new HashMap<>();
        data.put("imageUrls", imageUrls);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "게시글 비속어 검사", description = "게시글 내용의 비속어 검사")
    @PostMapping("/check-profanity")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkProfanity(
            @Valid @RequestBody PostRequest.CheckProfanity request) {
        List<String> detectedWords = postService.checkProfanity(request);
        
        Map<String, Object> data = new HashMap<>();
        data.put("hasProfanity", !detectedWords.isEmpty());
        data.put("detectedWords", detectedWords);
        data.put("message", detectedWords.isEmpty() ? 
                "비속어가 감지되지 않았습니다." : "비속어가 감지되었습니다.");
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "게시글 생성", description = "새로운 게시글 생성")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest.Create request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        PostResponse post = postService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 수정 비속어 검사", description = "게시글 수정 내용의 비속어 검사")
    @PostMapping("/{id}/check-profanity")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkProfanityForUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest.CheckProfanity request) {
        List<String> detectedWords = postService.checkProfanity(request);
        
        Map<String, Object> data = new HashMap<>();
        data.put("hasProfanity", !detectedWords.isEmpty());
        data.put("detectedWords", detectedWords);
        data.put("message", detectedWords.isEmpty() ? 
                "비속어가 감지되지 않았습니다." : "비속어가 감지되었습니다.");
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest.Update request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        PostResponse post = postService.updatePost(id, userId, request);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, String>>> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        postService.deletePost(id, userId);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "게시글이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("게시글이 삭제되었습니다.", data));
    }
}

