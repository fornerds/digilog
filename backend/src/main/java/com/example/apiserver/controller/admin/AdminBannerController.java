package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.banner.BannerRequest;
import com.example.apiserver.dto.banner.BannerResponse;
import com.example.apiserver.service.BannerService;
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
@Tag(name = "관리자 - 배너 관리", description = "관리자 배너 관리 API")
@RestController
@RequestMapping("/v1/admin/banners")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminBannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 목록 조회", description = "관리자가 배너 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBanners(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<BannerResponse> banners = bannerService.getBannersForAdmin(page, limit, search, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("banners", banners.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", banners.getNumber() + 1);
        pagination.put("limit", banners.getSize());
        pagination.put("total", banners.getTotalElements());
        pagination.put("totalPages", banners.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "배너 상세 조회", description = "관리자가 배너 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> getBanner(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        BannerResponse banner = bannerService.getBanner(id);
        return ResponseEntity.ok(ApiResponse.success(banner));
    }

    @Operation(summary = "배너 생성", description = "관리자가 배너 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<BannerResponse>> createBanner(
            Authentication authentication,
            @Valid @RequestBody BannerRequest.Create request) {
        AdminUtil.checkAdminRole(authentication);
        
        BannerResponse banner = bannerService.createBanner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(banner));
    }

    @Operation(summary = "배너 수정", description = "관리자가 배너 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResponse>> updateBanner(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody BannerRequest.Update request) {
        AdminUtil.checkAdminRole(authentication);
        
        BannerResponse banner = bannerService.updateBanner(id, request);
        return ResponseEntity.ok(ApiResponse.success(banner));
    }

    @Operation(summary = "배너 삭제", description = "관리자가 배너 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteBanner(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        bannerService.deleteBanner(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "배너가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("배너가 삭제되었습니다.", data));
    }
}

