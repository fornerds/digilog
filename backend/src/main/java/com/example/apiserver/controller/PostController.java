package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.ImageUploadResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.ProfanityCheckResponse;
import com.example.apiserver.dto.post.PostRequest;
import com.example.apiserver.dto.post.PostResponse;
import com.example.apiserver.service.PostService;
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
    public ResponseEntity<ApiResponse<PaginatedResponse<PostResponse>>> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        
        Page<PostResponse> posts = postService.getPosts(page, limit, search, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(posts.getNumber() + 1)
                .limit(posts.getSize())
                .total(posts.getTotalElements())
                .totalPages(posts.getTotalPages())
                .build();
        
        PaginatedResponse<PostResponse> response = PaginatedResponse.<PostResponse>builder()
                .items(posts.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글 상세 정보 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse post = postService.getPost(id);
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @Operation(summary = "게시글 사진 업로드", description = "게시글용 사진 업로드 (S3 사용)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "이미지 업로드 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미지 파일이 없거나 잘못된 형식",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류 또는 S3 업로드 실패",
            content = @Content())
    })
    @PostMapping("/images")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImages(
            @RequestParam("images") List<MultipartFile> images,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<String> imageUrls = postService.uploadPostImages(userId, images);
        
        ImageUploadResponse imageUploadResponse = ImageUploadResponse.builder()
                .imageUrls(imageUrls)
                .build();
        return ResponseEntity.ok(ApiResponse.success(imageUploadResponse));
    }

    @Operation(summary = "게시글 비속어 검사", description = "게시글 내용의 비속어 검사")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비속어 검사 완료",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"내용은 필수입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/check-profanity")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<ProfanityCheckResponse>> checkProfanity(
            @Valid @RequestBody PostRequest.CheckProfanity request) {
        List<String> detectedWords = postService.checkProfanity(request);
        
        ProfanityCheckResponse profanityCheckResponse = ProfanityCheckResponse.builder()
                .hasProfanity(!detectedWords.isEmpty())
                .detectedWords(detectedWords)
                .message(detectedWords.isEmpty() ? 
                        "비속어가 감지되지 않았습니다." : "비속어가 감지되었습니다.")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(profanityCheckResponse));
    }

    @Operation(summary = "게시글 생성", description = "새로운 게시글 생성")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "게시글 생성 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 비속어 감지",
            content = @Content(
                examples = {
                    @ExampleObject(name = "Validation failed", value = "{\"success\": false, \"message\": \"제목은 필수입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"),
                    @ExampleObject(name = "Profanity detected", value = "{\"success\": false, \"message\": \"비속어가 감지되었습니다\", \"data\": {\"code\": \"PROFANITY_DETECTED\", \"detectedWords\": [\"비속어1\", \"비속어2\"]}, \"timestamp\": \"2025-12-16T10:00:00\"}")
                })),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "비속어 검사 완료",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/{id}/check-profanity")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<ProfanityCheckResponse>> checkProfanityForUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest.CheckProfanity request) {
        List<String> detectedWords = postService.checkProfanity(request);
        
        ProfanityCheckResponse profanityCheckResponse = ProfanityCheckResponse.builder()
                .hasProfanity(!detectedWords.isEmpty())
                .detectedWords(detectedWords)
                .message(detectedWords.isEmpty() ? 
                        "비속어가 감지되지 않았습니다." : "비속어가 감지되었습니다.")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(profanityCheckResponse));
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 비속어 감지",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "게시글을 삭제할 권한이 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"게시글을 삭제할 권한이 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<MessageResponse>> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        postService.deletePost(id, userId);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("게시글이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("게시글이 삭제되었습니다.", messageResponse));
    }
}

