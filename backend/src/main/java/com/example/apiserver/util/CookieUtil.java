package com.example.apiserver.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    
    @Value("${server.servlet.session.cookie.secure:false}")
    private boolean cookieSecure;
    
    @Value("${server.servlet.session.cookie.same-site:lax}")
    private String cookieSameSite;
    
    /**
     * HttpOnly, Secure 설정이 적용된 Cookie를 설정합니다.
     * SameSite 설정은 Response Header에 직접 추가합니다.
     */
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        // 기본 Cookie 설정
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/api");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
        
        // SameSite 설정을 위해 Response Header에 직접 추가
        // (Servlet API의 Cookie는 SameSite를 직접 지원하지 않음)
        String sameSiteValue = "Lax";
        if ("none".equalsIgnoreCase(cookieSameSite)) {
            sameSiteValue = "None";
        } else if ("strict".equalsIgnoreCase(cookieSameSite)) {
            sameSiteValue = "Strict";
        }
        
        // Set-Cookie 헤더에 SameSite 추가
        String cookieHeader = String.format("%s=%s; Path=/api; HttpOnly; Max-Age=%d; SameSite=%s",
                name, value, maxAge, sameSiteValue);
        if (cookieSecure) {
            cookieHeader += "; Secure";
        }
        
        response.addHeader("Set-Cookie", cookieHeader);
    }
    
    /**
     * Cookie를 삭제합니다.
     */
    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/api");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        // SameSite 설정 포함하여 삭제
        String sameSiteValue = "Lax";
        if ("none".equalsIgnoreCase(cookieSameSite)) {
            sameSiteValue = "None";
        } else if ("strict".equalsIgnoreCase(cookieSameSite)) {
            sameSiteValue = "Strict";
        }
        
        String cookieHeader = String.format("%s=; Path=/api; HttpOnly; Max-Age=0; SameSite=%s",
                name, sameSiteValue);
        if (cookieSecure) {
            cookieHeader += "; Secure";
        }
        
        response.addHeader("Set-Cookie", cookieHeader);
    }
}

