package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.product.ProductRequest;
import com.example.apiserver.dto.product.ProductResponse;
import com.example.apiserver.service.ProductService;
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
@Tag(name = "관리자 - 제품 관리", description = "관리자 제품 관리 API")
@RestController
@RequestMapping("/v1/admin/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    private final ProductService productService;

    @Operation(summary = "제품 목록 조회", description = "관리자가 제품 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProducts(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String skinCode,
            @RequestParam(required = false) String personalColor,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<ProductResponse> products = productService.getProductsForAdmin(page, limit, search, skinCode, personalColor, sortBy, order);
        
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

    @Operation(summary = "제품 상세 조회", description = "관리자가 제품 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        ProductResponse product = productService.getProductForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "제품 생성", description = "관리자가 제품 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            Authentication authentication,
            @Valid @RequestBody ProductRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        ProductResponse product = productService.createProductForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(product));
    }

    @Operation(summary = "제품 수정", description = "관리자가 제품 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest.UpdateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        ProductResponse product = productService.updateProductForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "제품 삭제", description = "관리자가 제품 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteProduct(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        productService.deleteProductForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "제품이 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("제품이 삭제되었습니다.", data));
    }
}

