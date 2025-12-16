package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.WishToggleResponse;
import com.example.apiserver.dto.product.ProductRequest;
import com.example.apiserver.dto.product.ProductResponse;
import com.example.apiserver.service.ProductService;
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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "제품", description = "제품 관련 API")
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "피부코드 기반 제품 추천 조회", description = "피부코드에 따른 제품 추천 목록 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/recommended-by-skin-code")
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProductsBySkinCode(
            @RequestParam String skinCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        Page<ProductResponse> products = productService.getProductsBySkinCode(skinCode, page, limit, userId);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(products.getNumber() + 1)
                .limit(products.getSize())
                .total(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
        
        PaginatedResponse<ProductResponse> response = PaginatedResponse.<ProductResponse>builder()
                .items(products.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "퍼스널컬러 기반 제품 추천 조회", description = "퍼스널컬러에 따른 제품 추천 목록 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/recommended-by-personal-color")
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProductsByPersonalColor(
            @RequestParam String personalColor,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        Page<ProductResponse> products = productService.getProductsByPersonalColor(personalColor, page, limit, userId);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(products.getNumber() + 1)
                .limit(products.getSize())
                .total(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build();
        
        PaginatedResponse<ProductResponse> response = PaginatedResponse.<ProductResponse>builder()
                .items(products.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "제품 찜 추가/취소", description = "제품 찜 추가 또는 취소")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "찜 추가/취소 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "제품을 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"제품을 찾을 수 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PutMapping("/{id}/wish")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<WishToggleResponse>> toggleWish(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest.ToggleWish request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ProductResponse product = productService.toggleWish(id, userId, request.getIsWished());
        
        WishToggleResponse wishToggleResponse = WishToggleResponse.builder()
                .productId(product.getId())
                .isWished(product.getIsWished())
                .message(product.getIsWished() ? 
                        "제품이 찜 목록에 추가되었습니다." : "제품이 찜 목록에서 제거되었습니다.")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(wishToggleResponse));
    }
}

