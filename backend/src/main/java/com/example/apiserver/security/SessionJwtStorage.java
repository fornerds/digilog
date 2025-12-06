package com.example.apiserver.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Session에 JWT를 저장하는 유틸리티
 * 필요시 세션에 토큰을 저장하여 사용할 수 있습니다.
 */
@Slf4j
@Component
public class SessionJwtStorage {
    
    private static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";
    private static final String SESSION_REFRESH_TOKEN = "SESSION_REFRESH_TOKEN";
    private static final String SESSION_USER_ID = "SESSION_USER_ID";
    private static final String SESSION_USER_EMAIL = "SESSION_USER_EMAIL";
    private static final String SESSION_USER_ROLE = "SESSION_USER_ROLE";
    
    public void storeTokens(HttpServletRequest request, String accessToken, String refreshToken, 
                           Long userId, String email, String role) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_ACCESS_TOKEN, accessToken);
        session.setAttribute(SESSION_REFRESH_TOKEN, refreshToken);
        session.setAttribute(SESSION_USER_ID, userId);
        session.setAttribute(SESSION_USER_EMAIL, email);
        session.setAttribute(SESSION_USER_ROLE, role);
        log.debug("JWT 토큰을 세션에 저장했습니다. UserId: {}", userId);
    }
    
    public String getAccessToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SESSION_ACCESS_TOKEN);
        }
        return null;
    }
    
    public String getRefreshToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SESSION_REFRESH_TOKEN);
        }
        return null;
    }
    
    public Long getUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Long) session.getAttribute(SESSION_USER_ID);
        }
        return null;
    }
    
    public String getUserEmail(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SESSION_USER_EMAIL);
        }
        return null;
    }
    
    public String getUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SESSION_USER_ROLE);
        }
        return null;
    }
    
    public void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.debug("세션을 무효화했습니다.");
        }
    }
}

