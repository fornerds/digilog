package com.example.apiserver.service;

import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.entity.RefreshToken;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.UnauthorizedException;
import com.example.apiserver.repository.RefreshTokenRepository;
import com.example.apiserver.repository.UserRepository;
import com.example.apiserver.util.CookieUtil;
import com.example.apiserver.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResult login(String email, String password, HttpServletResponse response) {
        User user = userService.validateLogin(email, password);

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 기존 refresh token 삭제 (한 사용자당 하나의 refresh token만 유지)
        refreshTokenRepository.findByUserAndIsDeletedFalse(user)
                .ifPresent(existingToken -> existingToken.softDelete());

        // DB에 refresh token 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        // Cookie에 토큰 저장
        cookieUtil.setCookie(response, "accessToken", accessToken, (int) (jwtUtil.getExpiration() / 1000));
        cookieUtil.setCookie(response, "refreshToken", refreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());

        return new LoginResult(accessToken, userData);
    }

    @Transactional
    public SocialLoginResult socialLogin(UserRequest.SocialLogin request, HttpServletResponse response) {
        UserService.SocialLoginResult result = userService.socialLogin(request);
        UserResponse userResponse = result.getUserResponse();
        User user = userService.findById(userResponse.getId());

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 기존 refresh token 삭제 (한 사용자당 하나의 refresh token만 유지)
        refreshTokenRepository.findByUserAndIsDeletedFalse(user)
                .ifPresent(existingToken -> existingToken.softDelete());

        // DB에 refresh token 저장
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        // Cookie에 토큰 저장
        cookieUtil.setCookie(response, "accessToken", accessToken, (int) (jwtUtil.getExpiration() / 1000));
        cookieUtil.setCookie(response, "refreshToken", refreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userResponse.getId());
        userData.put("email", userResponse.getEmail());
        userData.put("name", userResponse.getName());
        userData.put("provider", userResponse.getProvider());

        return new SocialLoginResult(accessToken, userData, result.isNewUser());
    }

    @Transactional
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 refresh token 추출
        String refreshToken = extractRefreshTokenFromRequest(request);
        
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token이 없습니다");
        }

        // Refresh token 유효성 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UnauthorizedException("Refresh token이 만료되었거나 유효하지 않습니다");
        }

        // Refresh token 타입 확인
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new BadRequestException("유효하지 않은 refresh token입니다");
        }

        // JWT에서 userId 추출 (성능 최적화: userId로 먼저 조회)
        Long userId = jwtUtil.extractUserId(refreshToken);

        // DB에서 refresh token 조회 및 검증 (userId로 조회하여 인덱스 활용)
        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UnauthorizedException("유효하지 않은 refresh token입니다"));

        // 토큰 값 일치 확인 (보안: 실제 토큰 값이 일치하는지 검증)
        if (!refreshTokenEntity.getToken().equals(refreshToken)) {
            throw new UnauthorizedException("유효하지 않은 refresh token입니다");
        }

        // User 조회 및 검증
        User user = refreshTokenEntity.getUser();
        if (user.isDeleted()) {
            // 사용자가 삭제된 경우 refresh token도 삭제
            refreshTokenEntity.softDelete();
            throw new UnauthorizedException("사용자를 찾을 수 없습니다");
        }

        // 새 토큰 생성 (Rotation)
        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 기존 refresh token 삭제하고 새로 저장
        refreshTokenEntity.softDelete();
        RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(newRefreshTokenEntity);

        // Cookie에 새 토큰 저장
        cookieUtil.setCookie(response, "accessToken", newAccessToken, (int) (jwtUtil.getExpiration() / 1000));
        cookieUtil.setCookie(response, "refreshToken", newRefreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        return newAccessToken;
    }

    @Transactional
    public void logout(Long userId, HttpServletResponse response) {
        // DB에서 refresh token 삭제
        User user = userService.findById(userId);
        refreshTokenRepository.findByUserAndIsDeletedFalse(user)
                .ifPresent(RefreshToken::softDelete);

        // Cookie 삭제
        cookieUtil.deleteCookie(response, "accessToken");
        cookieUtil.deleteCookie(response, "refreshToken");
    }

    @Transactional(readOnly = true)
    public boolean checkEmailAvailable(String email) {
        // 이메일 형식 검증
        if (email == null || email.isBlank()) {
            log.warn("이메일 중복 확인 요청: 빈 이메일");
            throw new BadRequestException("이메일을 입력해주세요");
        }

        try {
            // 이메일 중복 확인 (삭제되지 않은 사용자만 확인)
            boolean exists = userRepository.existsByEmailAndIsDeletedFalse(email);
            boolean available = !exists;
            log.debug("이메일 중복 확인: email={}, exists={}, available={}", email, exists, available);
            return available;
        } catch (Exception e) {
            log.error("이메일 중복 확인 중 에러 발생: email={}", email, e);
            throw e;
        }
    }

    private String extractRefreshTokenFromRequest(HttpServletRequest request) {
        // 1. Cookie에서 refresh token 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoginResult {
        private final String accessToken;
        private final Map<String, Object> user;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SocialLoginResult {
        private final String accessToken;
        private final Map<String, Object> user;
        private final boolean isNewUser;
    }
}

