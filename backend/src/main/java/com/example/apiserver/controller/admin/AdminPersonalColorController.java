package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorColorResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorRequest;
import com.example.apiserver.dto.personalcolor.PersonalColorResponse;
import com.example.apiserver.service.PersonalColorService;
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
@Tag(name = "관리자 - 퍼스널컬러 관리", description = "관리자 퍼스널컬러 관리 API")
@RestController
@RequestMapping("/v1/admin/personal-colors")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminPersonalColorController {

    private final PersonalColorService personalColorService;

    @Operation(summary = "퍼스널컬러 색상 목록 조회", description = "관리자가 퍼스널컬러 색상 목록 조회")
    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getColors(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<PersonalColorColorResponse> colors = personalColorService.getColorsForAdmin(page, limit, category, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("colors", colors.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", colors.getNumber() + 1);
        pagination.put("limit", colors.getSize());
        pagination.put("total", colors.getTotalElements());
        pagination.put("totalPages", colors.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "퍼스널컬러 색상 상세 조회", description = "관리자가 퍼스널컬러 색상 상세 조회")
    @GetMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<PersonalColorColorResponse>> getColor(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorColorResponse color = personalColorService.getColorForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(color));
    }

    @Operation(summary = "퍼스널컬러 색상 생성", description = "관리자가 퍼스널컬러 색상 생성")
    @PostMapping("/colors")
    public ResponseEntity<ApiResponse<PersonalColorColorResponse>> createColor(
            Authentication authentication,
            @Valid @RequestBody PersonalColorRequest.CreateColor request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorColorResponse color = personalColorService.createColorForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(color));
    }

    @Operation(summary = "퍼스널컬러 색상 수정", description = "관리자가 퍼스널컬러 색상 수정")
    @PutMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<PersonalColorColorResponse>> updateColor(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PersonalColorRequest.UpdateColor request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorColorResponse color = personalColorService.updateColorForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(color));
    }

    @Operation(summary = "퍼스널컬러 색상 삭제", description = "관리자가 퍼스널컬러 색상 삭제")
    @DeleteMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteColor(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        personalColorService.deleteColorForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "색상이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("색상이 삭제되었습니다.", data));
    }

    @Operation(summary = "퍼스널컬러 진단 목록 조회", description = "관리자가 퍼스널컬러 진단 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDiagnoses(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<PersonalColorResponse> diagnoses = personalColorService.getDiagnosesForAdmin(page, limit, search, userId, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("diagnoses", diagnoses.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", diagnoses.getNumber() + 1);
        pagination.put("limit", diagnoses.getSize());
        pagination.put("total", diagnoses.getTotalElements());
        pagination.put("totalPages", diagnoses.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "퍼스널컬러 진단 상세 조회", description = "관리자가 퍼스널컬러 진단 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalColorResponse>> getDiagnosis(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorResponse diagnosis = personalColorService.getDiagnosis(id);
        return ResponseEntity.ok(ApiResponse.success(diagnosis));
    }

    @Operation(summary = "퍼스널컬러 진단 생성", description = "관리자가 퍼스널컬러 진단 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<PersonalColorResponse>> createDiagnosis(
            Authentication authentication,
            @Valid @RequestBody PersonalColorRequest.CreateDiagnosis request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorResponse diagnosis = personalColorService.createDiagnosisForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(diagnosis));
    }

    @Operation(summary = "퍼스널컬러 진단 수정", description = "관리자가 퍼스널컬러 진단 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalColorResponse>> updateDiagnosis(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody PersonalColorRequest.UpdateDiagnosis request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorResponse diagnosis = personalColorService.updateDiagnosisForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(diagnosis));
    }

    @Operation(summary = "퍼스널컬러 진단 삭제", description = "관리자가 퍼스널컬러 진단 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteDiagnosis(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        personalColorService.deleteDiagnosisForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "퍼스널컬러 진단 정보가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("퍼스널컬러 진단 정보가 삭제되었습니다.", data));
    }
}

