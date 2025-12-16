package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.BatchCreateResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.entity.ProfanityWord;
import com.example.apiserver.service.ProfanityWordService;
import com.example.apiserver.util.AdminUtil;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "관리자 - 비속어 관리", description = "관리자 비속어 관리 API")
@RestController
@RequestMapping("/v1/admin/profanity-words")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminProfanityWordController {

    private final ProfanityWordService profanityWordService;

    @Operation(summary = "비속어 단어 목록 조회", description = "관리자가 비속어 단어 목록 조회")
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
    public ResponseEntity<ApiResponse<PaginatedResponse<ProfanityWord>>> getWords(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<ProfanityWord> words = profanityWordService.getWords(page, limit);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(words.getNumber() + 1)
                .limit(words.getSize())
                .total(words.getTotalElements())
                .totalPages(words.getTotalPages())
                .build();
        
        PaginatedResponse<ProfanityWord> response = PaginatedResponse.<ProfanityWord>builder()
                .items(words.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "비속어 단어 상세 조회", description = "관리자가 비속어 단어 상세 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "비속어 단어를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfanityWord>> getWord(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        ProfanityWord word = profanityWordService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(word));
    }

    @Operation(summary = "비속어 단어 생성", description = "관리자가 비속어 단어 생성")
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
    public ResponseEntity<ApiResponse<ProfanityWord>> createWord(
            Authentication authentication,
            @RequestBody Map<String, String> request) {
        AdminUtil.checkAdminRole(authentication);
        
        String word = request.get("word");
        ProfanityWord saved = profanityWordService.createWord(word);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(saved));
    }

    @Operation(summary = "비속어 단어 수정", description = "관리자가 비속어 단어 수정")
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "비속어 단어를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfanityWord>> updateWord(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        AdminUtil.checkAdminRole(authentication);
        
        String word = request.get("word");
        ProfanityWord updated = profanityWordService.updateWord(id, word);
        
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @Operation(summary = "비속어 단어 삭제", description = "관리자가 비속어 단어 삭제")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "비속어 단어를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteWord(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        profanityWordService.deleteWord(id);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("비속어 단어가 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("비속어 단어가 삭제되었습니다.", messageResponse));
    }

    @Operation(summary = "비속어 단어 일괄 추가", description = "관리자가 비속어 단어 일괄 추가")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "일괄 추가 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<BatchCreateResponse>> batchCreateWords(
            Authentication authentication,
            @RequestBody Map<String, List<String>> request) {
        AdminUtil.checkAdminRole(authentication);
        
        List<String> words = request.get("words");
        ProfanityWordService.BatchResult result = profanityWordService.batchCreateWords(words);
        
        BatchCreateResponse batchCreateResponse = BatchCreateResponse.builder()
                .created(result.getCreated())
                .skipped(result.getSkipped())
                .total(result.getTotal())
                .build();
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(batchCreateResponse));
    }
}

