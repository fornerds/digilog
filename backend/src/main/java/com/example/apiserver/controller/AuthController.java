package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.EmailCheckResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.TokenResponse;
import com.example.apiserver.dto.user.LoginResponse;
import com.example.apiserver.dto.user.SocialLoginResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.AuthService;
import com.example.apiserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "인증", description = "회원가입, 로그인, 소셜 로그인 API")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "회원가입", description = "새로운 사용자 회원가입")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 중복된 이메일/휴대폰 번호",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"이미 등록된 이메일입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRequest.SignUp request) {
        UserResponse userResponse = userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(userResponse));
    }

    @Operation(summary = "로그인", description = "사용자 로그인")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 올바르지 않습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"이메일 또는 비밀번호가 올바르지 않습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"사용자를 찾을 수 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody UserRequest.SignIn request,
            HttpServletResponse response) {
        AuthService.LoginResult result = authService.login(request.getEmail(), request.getPassword(), response);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(result.getAccessToken())
                .user(result.getUser())
                .build();

        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 (네이버, 카카오)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "소셜 로그인 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 OAuth 인증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAuth 인증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/social")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> socialLogin(
            @Valid @RequestBody UserRequest.SocialLogin request,
            HttpServletResponse response) {
        AuthService.SocialLoginResult result = authService.socialLogin(request, response);

        SocialLoginResponse socialLoginResponse = SocialLoginResponse.builder()
                .token(result.getAccessToken())
                .user(result.getUser())
                .isNewUser(result.isNewUser())
                .build();

        return ResponseEntity.ok(ApiResponse.success(socialLoginResponse));
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token을 사용하여 새로운 Access Token 발급")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "토큰 재발급 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않거나 만료됨",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"유효하지 않은 토큰입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            jakarta.servlet.http.HttpServletRequest request,
            HttpServletResponse response) {
        String newAccessToken = authService.refreshToken(request, response);

        TokenResponse tokenResponse = TokenResponse.builder()
                .token(newAccessToken)
                .build();

        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }

    @Operation(summary = "이메일 중복 확인", description = "회원가입 전 이메일 중복 여부 확인")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일을 입력해주세요",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"이메일을 입력해주세요\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<EmailCheckResponse>> checkEmail(
            @RequestParam(required = false) String email) {
        boolean available = authService.checkEmailAvailable(email);

        EmailCheckResponse emailCheckResponse = EmailCheckResponse.builder()
                .email(email != null ? email : "")
                .available(available)
                .build();

        return ResponseEntity.ok(ApiResponse.success(emailCheckResponse));
    }

    @Operation(summary = "OAuth 콜백", description = "네이버/카카오 OAuth 인증 코드를 받아서 로그인 처리")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OAuth 로그인 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 인증 코드 만료",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"인증 코드가 만료되었거나 이미 사용되었습니다. 다시 로그인해주세요.\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "OAuth 인증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/oauth/callback")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> oauthCallback(
            @Valid @RequestBody UserRequest.OAuthCallback request,
            HttpServletResponse response) {
        AuthService.SocialLoginResult result = authService.oauthCallback(request, response);

        SocialLoginResponse socialLoginResponse = SocialLoginResponse.builder()
                .token(result.getAccessToken())
                .user(result.getUser())
                .isNewUser(result.isNewUser())
                .build();

        return ResponseEntity.ok(ApiResponse.success(socialLoginResponse));
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 (Refresh Token 쿠키 삭제)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "로그아웃 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<MessageResponse>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        authService.logout(request, response);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("로그아웃되었습니다.")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(messageResponse));
    }
}

