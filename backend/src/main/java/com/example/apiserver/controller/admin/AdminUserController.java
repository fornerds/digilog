package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.MessageResponse;
import com.example.apiserver.dto.PaginatedResponse;
import com.example.apiserver.dto.PaginationInfo;
import com.example.apiserver.dto.TokenResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.UserService;
import com.example.apiserver.util.AdminUtil;
import com.example.apiserver.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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

@Slf4j
@Tag(name = "관리자 - 사용자 관리", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "사용자 목록 조회", description = "관리자가 사용자 목록 조회")
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
    public ResponseEntity<ApiResponse<PaginatedResponse<UserResponse>>> getUsers(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<UserResponse> users = userService.getUsersForAdmin(page, limit, search, sortBy, order);
        
        PaginationInfo pagination = PaginationInfo.builder()
                .page(users.getNumber() + 1)
                .limit(users.getSize())
                .total(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .build();
        
        PaginatedResponse<UserResponse> response = PaginatedResponse.<UserResponse>builder()
                .items(users.getContent())
                .pagination(pagination)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "사용자 상세 조회", description = "관리자가 사용자 상세 정보 조회")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.getProfileForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "사용자 생성", description = "관리자가 사용자 생성")
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
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            Authentication authentication,
            @Valid @RequestBody UserRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.createUserForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(user));
    }

    @Operation(summary = "사용자 수정", description = "관리자가 사용자 정보 수정")
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
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody UserRequest.UpdateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.updateUserForAdmin(id, request);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "사용자 삭제", description = "관리자가 사용자 삭제")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MessageResponse>> deleteUser(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        userService.deleteUserForAdmin(id);
        
        MessageResponse messageResponse = MessageResponse.builder()
                .message("사용자가 삭제되었습니다.")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(messageResponse));
    }

    @Operation(summary = "테스트용 토큰 생성", description = "관리자가 특정 사용자 ID로 테스트용 JWT 토큰 생성 (테스트 전용)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "토큰 생성 성공",
            content = @Content()
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다",
            content = @Content()),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
            content = @Content())
    })
    @PostMapping("/{id}/test-token")
    public ResponseEntity<ApiResponse<TokenResponse>> generateTestToken(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.getProfileForAdmin(id);
        // UserResponse에서 id, email, role 가져오기
        Long userId = user.getId();
        String email = user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : "";
        String role = user.getRole() != null ? user.getRole().name() : "USER";
        
        String token = jwtUtil.generateToken(userId, email, role);
        
        TokenResponse tokenResponse = TokenResponse.builder()
                .token(token)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(tokenResponse));
    }
}
