package com.example.apiserver.dto.user;

import com.example.apiserver.entity.User;
import com.example.apiserver.entity.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String profileImageUrl;
    private String provider;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 일반 사용자용 - id를 제외한 UserResponse 생성 (기본)
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(null)  // 보안상 id 제외
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .gender(user.getGender() != null ? user.getGender().name().toLowerCase() : null)
                .profileImageUrl(user.getProfileImageUrl())
                .provider(user.getProvider() != null ? user.getProvider().name().toLowerCase() : "local")
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * 관리자용 - id를 포함한 UserResponse 생성
     */
    public static UserResponse fromWithId(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .gender(user.getGender() != null ? user.getGender().name().toLowerCase() : null)
                .profileImageUrl(user.getProfileImageUrl())
                .provider(user.getProvider() != null ? user.getProvider().name().toLowerCase() : "local")
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

