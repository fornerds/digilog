package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.personalcolor.PersonalColorColorResponse;
import com.example.apiserver.dto.personalcolor.PersonalColorRequest;
import com.example.apiserver.dto.personalcolor.PersonalColorResponse;
import com.example.apiserver.service.PersonalColorService;
import com.example.apiserver.util.AdminUtil;
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
    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<PaginatedResponse<PersonalColorColorResponse>>> getColors(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<PersonalColorColorResponse> colors = personalColorService.getColorsForAdmin(page, limit, category, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(colors.getNumber() + 1)
                .limit(colors.getSize())
                .total(colors.getTotalElements())
                .totalPages(colors.getTotalPages())
                .build();
        
        PaginatedResponse<PersonalColorColorResponse> response = PaginatedResponse.<PersonalColorColorResponse>builder()
                .items(colors.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "퍼스널컬러 색상 상세 조회", description = "관리자가 퍼스널컬러 색상 상세 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "색상을 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<PersonalColorColorResponse>> getColor(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorColorResponse color = personalColorService.getColorForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(color));
    }

    @Operation(summary = "퍼스널컬러 색상 생성", description = "관리자가 퍼스널컬러 색상 생성")
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
    @PostMapping("/colors")
    public ResponseEntity<ApiResponse<PersonalColorColorResponse>> createColor(
            Authentication authentication,
            @Valid @RequestBody PersonalColorRequest.CreateColor request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorColorResponse color = personalColorService.createColorForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(color));
    }

    @Operation(summary = "퍼스널컬러 색상 수정", description = "관리자가 퍼스널컬러 색상 수정")
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "색상을 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "색상을 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteColor(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        personalColorService.deleteColorForAdmin(id);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("색상이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("색상이 삭제되었습니다.", messageResponse));
    }

    @Operation(summary = "퍼스널컬러 진단 목록 조회", description = "관리자가 퍼스널컬러 진단 목록 조회")
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
    public ResponseEntity<ApiResponse<PaginatedResponse<PersonalColorResponse>>> getDiagnoses(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<PersonalColorResponse> diagnoses = personalColorService.getDiagnosesForAdmin(page, limit, search, userId, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(diagnoses.getNumber() + 1)
                .limit(diagnoses.getSize())
                .total(diagnoses.getTotalElements())
                .totalPages(diagnoses.getTotalPages())
                .build();
        
        PaginatedResponse<PersonalColorResponse> response = PaginatedResponse.<PersonalColorResponse>builder()
                .items(diagnoses.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "퍼스널컬러 진단 상세 조회", description = "관리자가 퍼스널컬러 진단 상세 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "진단 정보를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalColorResponse>> getDiagnosis(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorResponse diagnosis = personalColorService.getDiagnosis(id);
        return ResponseEntity.ok(ApiResponse.success(diagnosis));
    }

    @Operation(summary = "퍼스널컬러 진단 생성", description = "관리자가 퍼스널컬러 진단 생성")
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
    public ResponseEntity<ApiResponse<PersonalColorResponse>> createDiagnosis(
            Authentication authentication,
            @Valid @RequestBody PersonalColorRequest.CreateDiagnosis request) {
        AdminUtil.checkAdminRole(authentication);
        
        PersonalColorResponse diagnosis = personalColorService.createDiagnosisForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(diagnosis));
    }

    @Operation(summary = "퍼스널컬러 진단 수정", description = "관리자가 퍼스널컬러 진단 수정")
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "진단 정보를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "진단 정보를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteDiagnosis(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        personalColorService.deleteDiagnosisForAdmin(id);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("퍼스널컬러 진단 정보가 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("퍼스널컬러 진단 정보가 삭제되었습니다.", messageResponse));
    }
}

