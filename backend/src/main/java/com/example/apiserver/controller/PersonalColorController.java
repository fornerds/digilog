package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorColorResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorResponse;
import com.example.apiserver.service.PersonalColorService;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getColors() {
        List<PersonalColorColorResponse> colors = personalColorService.getColors();
        
        Map<String, Object> data = new HashMap<>();
        data.put("colors", colors);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "퍼스널컬러 상세 진단 조회", description = "퍼스널컬러 상세 진단 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalColorResponse>> getDiagnosis(@PathVariable Long id) {
        PersonalColorResponse diagnosis = personalColorService.getDiagnosis(id);
        return ResponseEntity.ok(ApiResponse.success(diagnosis));
    }
}

