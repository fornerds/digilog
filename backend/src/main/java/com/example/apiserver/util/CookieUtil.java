package com.example.apiserver.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookieUtil {
    
    @Value("${server.servlet.session.cookie.secure:false}")
    private boolean cookieSecure;
    
    @Value("${server.servlet.session.cookie.same-site:lax}")
    private String cookieSameSite;
    
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    /**
     * RefreshToken 쿠키를 설정합니다.
     * 프로덕션과 개발 환경에 따라 다르게 설정됩니다.
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAgeSeconds) {
        boolean isProduction = "prod".equals(activeProfile) || "production".equals(activeProfile);
        
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .path("/api")  // /api 하위 모든 경로에서 쿠키 접근 가능
                .maxAge(Duration.ofSeconds(maxAgeSeconds));
        
        if (isProduction) {
            // 프로덕션 환경
            cookieBuilder
                    .secure(true)
                    .sameSite("Strict");
            // domain은 필요시 설정
            // .domain("yourdomain.com")
        } else {
            // 개발 환경
            cookieBuilder
                    .secure(false)
                    .sameSite("Lax");
        }
        
        ResponseCookie cookie = cookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    
    /**
     * RefreshToken 쿠키를 삭제합니다.
     */
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        boolean isProduction = "prod".equals(activeProfile) || "production".equals(activeProfile);
        
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/api")  // /api 하위 모든 경로에서 쿠키 접근 가능
                .maxAge(Duration.ofSeconds(0));
        
        if (isProduction) {
            cookieBuilder
                    .secure(true)
                    .sameSite("Strict");
        } else {
            cookieBuilder
                    .secure(false)
                    .sameSite("Lax");
        }
        
        ResponseCookie cookie = cookieBuilder.build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    
    /**
     * @deprecated refreshToken만 사용하도록 변경되었습니다. setRefreshTokenCookie를 사용하세요.
     */
    @Deprecated
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        // 하위 호환성을 위해 유지하지만 사용하지 않음
        if ("refreshToken".equals(name)) {
            setRefreshTokenCookie(response, value, maxAge);
        }
    }
    
    /**
     * @deprecated refreshToken만 사용하도록 변경되었습니다. deleteRefreshTokenCookie를 사용하세요.
     */
    @Deprecated
    public void deleteCookie(HttpServletResponse response, String name) {
        // 하위 호환성을 위해 유지하지만 사용하지 않음
        if ("refreshToken".equals(name)) {
            deleteRefreshTokenCookie(response);
        }
    }
}

