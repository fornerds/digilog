package com.example.apiserver.controller;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.ProfileImageResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.UserService;
import com.example.apiserver.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
// Swagger ApiResponse는 전체 경로로 사용
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "사용자 정보 수정", description = "현재 로그인한 사용자 정보 수정")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패 또는 중복된 이메일/휴대폰 번호",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"이미 사용 중인 이메일입니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UserRequest.UpdateProfile request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자 계정 삭제")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "회원 탈퇴 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"비밀번호가 일치하지 않습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteUser(
            @RequestBody(required = false) UserRequest.DeleteUser request,
            Authentication authentication,
            HttpServletResponse response) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteUser(userId, request != null ? request : new UserRequest.DeleteUser());

        // Cookie 삭제
        cookieUtil.deleteCookie(response, "accessToken");
        cookieUtil.deleteCookie(response, "refreshToken");

        MessageResponse messageResponse = MessageResponse.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다.", messageResponse));
    }

    @Operation(summary = "프로필 이미지 업로드/수정", description = "프로필 이미지 업로드 및 수정 (S3 사용)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "이미지 업로드 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미지 파일이 없거나 잘못된 형식",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류 또는 S3 업로드 실패",
            content = @Content())
    })
    @PutMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<ProfileImageResponse>> uploadProfileImage(
            @RequestParam("image") MultipartFile image,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        String profileImageUrl = userService.uploadProfileImage(userId, image);

        ProfileImageResponse profileImageResponse = ProfileImageResponse.builder()
                .profileImageUrl(profileImageUrl)
                .build();
        return ResponseEntity.ok(ApiResponse.success(profileImageResponse));
    }

    @Operation(summary = "프로필 이미지 삭제", description = "프로필 이미지 삭제")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "이미지 삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "인증되지 않음 또는 권한 없음",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/me/profile-image")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteProfileImage(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        userService.deleteProfileImage(userId);

        MessageResponse messageResponse = MessageResponse.builder()
                .message("프로필 이미지가 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(ApiResponse.success("프로필 이미지가 삭제되었습니다.", messageResponse));
    }

    @Operation(summary = "소셜 로그인 후 추가 정보 입력", description = "소셜 로그인 사용자가 필수 정보(휴대폰 번호, 생년월일, 성별)를 입력")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "정보 입력 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 데이터 검증 실패",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "일반 회원가입 사용자는 이 API를 사용할 수 없습니다",
            content = @Content(
                examples = @ExampleObject(value = "{\"success\": false, \"message\": \"일반 회원가입 사용자는 이 API를 사용할 수 없습니다\", \"data\": null, \"timestamp\": \"2025-12-16T10:00:00\"}"))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/me/complete-social-profile")
    public ResponseEntity<ApiResponse<UserResponse>> completeSocialProfile(
            @Valid @RequestBody UserRequest.CompleteSocialProfile request,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserResponse userResponse = userService.completeSocialProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}

