package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.UserService;
import com.example.apiserver.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "사용자", description = "사용자 정보 조회/수정, 탈퇴, 프로필 이미지 API")
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 수정", description = "현재 로그인한 사용자 정보 수정")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserRequest.UpdateProfile request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자 계정 삭제")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteUser(
            @RequestBody(required = false) UserRequest.DeleteUser request,
            Authentication authentication,
            HttpServletResponse response) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteUser(userId, request != null ? request : new UserRequest.DeleteUser());

        // Cookie 삭제
        cookieUtil.deleteCookie(response, "accessToken");
        cookieUtil.deleteCookie(response, "refreshToken");

        Map<String, String> data = new HashMap<>();
        data.put("message", "회원 탈퇴가 완료되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다.", data));
    }

    @Operation(summary = "프로필 이미지 업로드/수정", description = "프로필 이미지 업로드 및 수정 (S3 사용)")
    @PutMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadProfileImage(
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String profileImageUrl = userService.uploadProfileImage(userId, image);

        Map<String, Object> data = new HashMap<>();
        data.put("profileImageUrl", profileImageUrl);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "프로필 이미지 삭제", description = "프로필 이미지 삭제")
    @DeleteMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteProfileImage(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteProfileImage(userId);

        Map<String, String> data = new HashMap<>();
        data.put("message", "프로필 이미지가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("프로필 이미지가 삭제되었습니다.", data));
    }

    @Operation(summary = "소셜 로그인 후 추가 정보 입력", description = "소셜 로그인 사용자가 필수 정보(휴대폰 번호, 생년월일, 성별)를 입력")
    @PostMapping("/me/complete-social-profile")
    public ResponseEntity<ApiResponse<UserResponse>> completeSocialProfile(
            @Valid @RequestBody UserRequest.CompleteSocialProfile request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.completeSocialProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}

