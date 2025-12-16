package com.example.apiserver.controller.admin;

import com.example.apiserver.dto.ApiResponse;
import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.service.UserService;
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
@Tag(name = "관리자 - 사용자 관리", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/v1/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "사용자 목록 조회", description = "전체 사용자 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUsers(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        AdminUtil.checkAdminRole(authentication);
        
        Page<UserResponse> users = userService.getUsersForAdmin(page, limit, search, sortBy, order);
        
        Map<String, Object> data = new HashMap<>();
        data.put("users", users.getContent());
        
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", users.getNumber() + 1);
        pagination.put("limit", users.getSize());
        pagination.put("total", users.getTotalElements());
        pagination.put("totalPages", users.getTotalPages());
        data.put("pagination", pagination);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @Operation(summary = "사용자 상세 조회", description = "특정 사용자 상세 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.getProfileForAdmin(id);  // 관리자용: id 포함
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @Operation(summary = "사용자 생성", description = "관리자가 사용자 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            Authentication authentication,
            @Valid @RequestBody UserRequest.CreateAdmin request) {
        AdminUtil.checkAdminRole(authentication);
        
        UserResponse user = userService.createUserForAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(user));
    }

    @Operation(summary = "사용자 수정", description = "관리자가 사용자 정보 수정")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteUser(
            Authentication authentication,
            @PathVariable Long id) {
        AdminUtil.checkAdminRole(authentication);
        
        userService.deleteUserForAdmin(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "사용자가 삭제되었습니다.");
        return ResponseEntity.ok(ApiResponse.success("사용자가 삭제되었습니다.", data));
    }
}
