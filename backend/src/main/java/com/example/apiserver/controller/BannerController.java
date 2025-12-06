package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.banner.BannerResponse;
import com.example.apiserver.service.BannerService;
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
@Tag(name = "배너", description = "배너 관련 API")
@RestController
@RequestMapping("/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 목록 조회", description = "배너 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBanners() {
        List<BannerResponse> banners = bannerService.getAllBanners();
        
        Map<String, Object> data = new HashMap<>();
        data.put("banners", banners);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

