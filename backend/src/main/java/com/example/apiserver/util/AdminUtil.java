package com.example.apiserver.util;

import com.example.apiserver.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class AdminUtil {
    
    public static void checkAdminRole(Authentication authentication) {
        if (authentication == null) {
            throw new ForbiddenException("인증이 필요합니다");
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN") || authority.equals("ADMIN"));
        
        if (!isAdmin) {
            throw new ForbiddenException("관리자 권한이 필요합니다");
        }
    }
}

