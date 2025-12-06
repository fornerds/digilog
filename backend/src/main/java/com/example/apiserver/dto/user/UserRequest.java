package com.example.apiserver.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {

    @Getter
    @NoArgsConstructor
    public static class SignUp {
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 50, message = "이름은 50자 이하여야 합니다")
        private String name;

        @NotBlank(message = "휴대폰 번호는 필수입니다")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다")
        @Schema(description = "휴대폰 번호", example = "010-1234-5678", pattern = "^010-?\\d{4}-?\\d{4}$")
        private String phone;

        @NotBlank(message = "생년월일은 필수입니다")
        @Schema(description = "생년월일", example = "1990-01-15", pattern = "yyyy-MM-dd")
        private String birthDate;

        @NotBlank(message = "성별은 필수입니다")
        @Schema(description = "성별", example = "male", allowableValues = {"male", "female", "other"})
        private String gender;

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
        private String password;

        @NotBlank(message = "비밀번호 확인은 필수입니다")
        private String passwordConfirm;
    }

    @Getter
    @NoArgsConstructor
    public static class SignIn {
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class SocialLogin {
        @NotBlank(message = "소셜 로그인 제공자는 필수입니다")
        private String provider;

        @NotBlank(message = "Access Token은 필수입니다")
        private String accessToken;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateProfile {
        @Size(max = 50, message = "이름은 50자 이하여야 합니다")
        private String name;

        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다")
        @Schema(description = "휴대폰 번호", example = "010-1234-5678", pattern = "^010-?\\d{4}-?\\d{4}$")
        private String phone;

        private String birthDate;

        private String gender;

        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangePassword {
        @NotBlank(message = "현재 비밀번호는 필수입니다")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호는 필수입니다")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
        private String newPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class DeleteUser {
        private String password;
    }

    @Getter
    @NoArgsConstructor
    public static class CompleteSocialProfile {
        @NotBlank(message = "휴대폰 번호는 필수입니다")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다")
        @Schema(description = "휴대폰 번호", example = "010-1234-5678", pattern = "^010-?\\d{4}-?\\d{4}$")
        private String phone;

        @NotBlank(message = "생년월일은 필수입니다")
        @Schema(description = "생년월일", example = "1990-01-15", pattern = "yyyy-MM-dd")
        private String birthDate;

        @NotBlank(message = "성별은 필수입니다")
        @Schema(description = "성별", example = "male", allowableValues = {"male", "female", "other"})
        private String gender;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateAdmin {
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @NotBlank(message = "비밀번호는 필수입니다")
        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
        private String password;

        @NotBlank(message = "이름은 필수입니다")
        @Size(max = 50, message = "이름은 50자 이하여야 합니다")
        private String name;

        @NotBlank(message = "휴대폰 번호는 필수입니다")
        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다")
        private String phone;

        @NotBlank(message = "생년월일은 필수입니다")
        private String birthDate;

        @NotBlank(message = "성별은 필수입니다")
        private String gender;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAdmin {
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        @Size(max = 50, message = "이름은 50자 이하여야 합니다")
        private String name;

        @Pattern(regexp = "^010-?\\d{4}-?\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다")
        private String phone;

        private String birthDate;

        private String gender;
    }
}

