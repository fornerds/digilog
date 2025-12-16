package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.ListResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorColorResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorResponse;
import com.example.apiserver.service.PersonalColorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ApiResponse는 전체 경로로 사용
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "퍼스널컬러", description = "퍼스널컬러 관련 API")
@RestController
@RequestMapping("/v1/personal-colors")
@RequiredArgsConstructor
public class PersonalColorController {

    private final PersonalColorService personalColorService;

    @Operation(summary = "퍼스널컬러 색상 조회", description = "퍼스널컬러 색상 목록 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<ListResponse<PersonalColorColorResponse>>> getColors() {
        List<PersonalColorColorResponse> colors = personalColorService.getColors();
        
        ListResponse<PersonalColorColorResponse> response = ListResponse.<PersonalColorColorResponse>builder()
                .items(colors)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "퍼스널컬러 상세 진단 조회", description = "퍼스널컬러 상세 진단 정보 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "진단 정보를 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"Resource not found\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalColorResponse>> getDiagnosis(@PathVariable Long id) {
        PersonalColorResponse diagnosis = personalColorService.getDiagnosis(id);
        return ResponseEntity.ok(ApiResponse.success(diagnosis));
    }
}

