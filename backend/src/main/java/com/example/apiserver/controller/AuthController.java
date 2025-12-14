package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.AuthService;
import com.example.apiserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "인증", description = "회원가입, 로그인, 소셜 로그인 API")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자 회원가입")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRequest.SignUp request) {
        UserResponse userResponse = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userResponse));
    }

    @Operation(summary = "로그인", description = "사용자 로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody UserRequest.SignIn request,
            HttpServletResponse response) {
        AuthService.LoginResult result = authService.login(request.getEmail(), request.getPassword(), response);

        Map<String, Object> data = new HashMap<>();
        data.put("token", result.getAccessToken());
        data.put("user", result.getUser());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 (네이버, 카카오)")
    @PostMapping("/social")
    public ResponseEntity<ApiResponse<Map<String, Object>>> socialLogin(
            @Valid @RequestBody UserRequest.SocialLogin request,
            HttpServletResponse response) {
        AuthService.SocialLoginResult result = authService.socialLogin(request, response);

        Map<String, Object> data = new HashMap<>();
        data.put("token", result.getAccessToken());
        data.put("user", result.getUser());
        data.put("isNewUser", result.isNewUser());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access Token 발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response) {
        String newAccessToken = authService.refreshToken(request, response);

        Map<String, Object> data = new HashMap<>();
        data.put("token", newAccessToken);

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "이메일 중복 확인", description = "회원가입 전 이메일 중복 여부 확인")
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkEmail(
            @RequestParam(required = false) String email) {
        boolean available = authService.checkEmailAvailable(email);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email != null ? email : "");
        data.put("available", available);

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

