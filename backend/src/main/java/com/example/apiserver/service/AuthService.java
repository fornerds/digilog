package com.example.apiserver.service;

import com.example.apiserver.dto.user.UserRequest;
import com.example.apiserver.dto.user.UserResponse;
import com.example.apiserver.entity.User;
import com.example.apiserver.util.CookieUtil;
import com.example.apiserver.util.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Transactional(readOnly = true)
    public LoginResult login(String email, String password, HttpServletResponse response) {
        User user = userService.validateLogin(email, password);

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // Cookie에 토큰 저장
        cookieUtil.setCookie(response, "accessToken", accessToken, (int) (jwtUtil.getExpiration() / 1000));
        cookieUtil.setCookie(response, "refreshToken", refreshToken, (int) (jwtUtil.getRefreshExpiration() / 1000));

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());

        return new LoginResult(accessToken, userData);
    }

    @Transactional(readOnly = true)
    public SocialLoginResult socialLogin(UserRequest.SocialLogin request, HttpServletResponse response) {
        UserService.SocialLoginResult result = userService.socialLogin(request);
        UserResponse userResponse = result.getUserResponse();
        User user = userService.findById(userResponse.getId());

        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

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

