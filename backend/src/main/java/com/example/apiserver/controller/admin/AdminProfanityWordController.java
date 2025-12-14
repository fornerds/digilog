package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.entity.ProfanityWord;
import com.example.apiserver.service.ProfanityWordService;
import com.example.apiserver.util.AdminUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWords(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<ProfanityWord> words = profanityWordService.getWords(page, limit);
        
        Map<String, Object> data = new HashMap<>();
        data.put("words", words.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", words.getNumber() + 1);
        pagination.put("limit", words.getSize());
        pagination.put("total", words.getTotalElements());
        pagination.put("totalPages", words.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "비속어 단어 상세 조회", description = "관리자가 비속어 단어 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfanityWord>> getWord(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        ProfanityWord word = profanityWordService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(word));
    }

    @Operation(summary = "비속어 단어 생성", description = "관리자가 비속어 단어 생성")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteWord(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        profanityWordService.deleteWord(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "비속어 단어가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("비속어 단어가 삭제되었습니다.", data));
    }

    @Operation(summary = "비속어 단어 일괄 추가", description = "관리자가 비속어 단어 일괄 추가")
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<Map<String, Object>>> batchCreateWords(
            Authentication authentication,
            @RequestBody Map<String, List<String>> request) {
        AdminUtil.checkAdminRole(authentication);
        
        List<String> words = request.get("words");
        ProfanityWordService.BatchResult result = profanityWordService.batchCreateWords(words);
        
        Map<String, Object> data = new HashMap<>();
        data.put("created", result.getCreated());
        data.put("skipped", result.getSkipped());
        data.put("total", result.getTotal());
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data));
    }
}

