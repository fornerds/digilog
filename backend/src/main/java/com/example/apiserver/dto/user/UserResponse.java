package com.example.apiserver.dto.user;

import com.example.apiserver.entity.User;
import com.example.apiserver.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
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

    public static UserResponse from(User user) {
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

