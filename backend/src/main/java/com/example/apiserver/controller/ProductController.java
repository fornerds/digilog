package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.product.ProductRequest;
import com.example.apiserver.dto.product.ProductResponse;
import com.example.apiserver.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping("/recommended-by-skin-code")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductsBySkinCode(
            @RequestParam String skinCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        Page<ProductResponse> products = productService.getProductsBySkinCode(skinCode, page, limit, userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("products", products.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", products.getNumber() + 1);
        pagination.put("limit", products.getSize());
        pagination.put("total", products.getTotalElements());
        pagination.put("totalPages", products.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "퍼스널컬러 기반 제품 추천 조회", description = "퍼스널컬러에 따른 제품 추천 목록 조회")
    @GetMapping("/recommended-by-personal-color")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProductsByPersonalColor(
            @RequestParam String personalColor,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        
        Long userId = authentication != null ? (Long) authentication.getPrincipal() : null;
        Page<ProductResponse> products = productService.getProductsByPersonalColor(personalColor, page, limit, userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("products", products.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", products.getNumber() + 1);
        pagination.put("limit", products.getSize());
        pagination.put("total", products.getTotalElements());
        pagination.put("totalPages", products.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "제품 찜 추가/취소", description = "제품 찜 추가 또는 취소")
    @PutMapping("/{id}/wish")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleWish(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest.ToggleWish request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ProductResponse product = productService.toggleWish(id, userId, request.getIsWished());
        
        Map<String, Object> data = new HashMap<>();
        data.put("productId", product.getId());
        data.put("isWished", product.getIsWished());
        data.put("message", product.getIsWished() ? 
                "제품이 찜 목록에 추가되었습니다." : "제품이 찜 목록에서 제거되었습니다.");
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

