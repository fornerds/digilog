package com.example.apiserver.security;

import com.example.apiserver.config.CorsProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsProperties corsProperties;
    
    // 인증이 필요 없는 경로들 (auth만 허용)
    private static final String[] PUBLIC_PATHS = {
        "/v1/auth/**",                    // 인증 관련 (회원가입, 로그인)
        // "/v1/notices/**",                 // 공지/이벤트 조회
        // "/v1/banners/**",                 // 배너 조회
        // "/v1/posts/**",                   // 게시글 조회 (작성/수정/삭제는 인증 필요)
        // "/v1/skin-analysis-reports/**",   // 피부분석 보고서 조회
        // "/v1/personal-colors/**",         // 퍼스널컬러 조회
        // "/v1/products/**",                // 제품 조회
        "/swagger-ui/**",                 // Swagger UI
        "/swagger-ui.html",               // Swagger UI
        "/swagger-ui/index.html",         // Swagger UI
        "/v3/api-docs/**",                // API 문서
        "/v3/api-docs",                   // API 문서
        "/actuator/health",               // Health Check
        "/actuator/info",                 // 서버 정보
        "/error",                         // 에러 페이지
        "/favicon.ico"                    // 파비콘
    };
    
    // 관리자만 접근 가능한 경로들
    private static final String[] ADMIN_PATHS = {
        "/v1/admin/**"
    };
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(PUBLIC_PATHS).permitAll()
                .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // Swagger UI 정적 리소스 허용
            .headers(headers -> headers.frameOptions().sameOrigin());
        
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                               AuthenticationException authException) throws IOException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                String timestamp = now.toString();
                String jsonResponse = String.format(
                    "{\"success\":false,\"message\":\"인증이 필요합니다\",\"data\":null,\"timestamp\":\"%s\"}",
                    timestamp
                );
                response.getWriter().write(jsonResponse);
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                             AccessDeniedException accessDeniedException) throws IOException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                String timestamp = now.toString();
                String jsonResponse = String.format(
                    "{\"success\":false,\"message\":\"권한이 없습니다\",\"data\":null,\"timestamp\":\"%s\"}",
                    timestamp
                );
                response.getWriter().write(jsonResponse);
            }
        };
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // allowCredentials가 true일 때는 setAllowedOriginPatterns 사용 (Spring Boot 2.4+)
        // setAllowedOrigins는 allowCredentials와 함께 사용 시 제한이 있음
        if (corsProperties.getAllowedOrigins() != null && !corsProperties.getAllowedOrigins().isEmpty()) {
            configuration.setAllowedOriginPatterns(corsProperties.getAllowedOrigins());
        }
        if (corsProperties.getAllowedMethods() != null) {
            configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        }
        if (corsProperties.getAllowedHeaders() != null) {
            configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        }
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        configuration.setMaxAge(corsProperties.getMaxAge());
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

