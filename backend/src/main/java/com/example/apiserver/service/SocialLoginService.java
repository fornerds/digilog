package com.example.apiserver.service;

import com.example.apiserver.config.SocialLoginProperties;
import com.example.apiserver.entity.Provider;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final SocialLoginProperties socialLoginProperties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public SocialUserInfo getUserInfo(Provider provider, String accessToken) {
        switch (provider) {
            case NAVER:
                return getNaverUserInfo(accessToken);
            case KAKAO:
                return getKakaoUserInfo(accessToken);
            default:
                throw new BadRequestException("지원하지 않는 소셜 로그인 제공자입니다");
        }
    }

    private SocialUserInfo getNaverUserInfo(String accessToken) {
        SocialLoginProperties.Naver naver = socialLoginProperties.getNaver();
        
        // 설정값이 없으면 예외 발생
        if (naver.getClientId().startsWith("${") || naver.getClientSecret().startsWith("${")) {
            log.warn("네이버 로그인 설정값이 없습니다. 환경변수를 설정해주세요.");
            throw new BadRequestException("네이버 로그인 설정이 완료되지 않았습니다");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    naver.getApiUrl(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                log.error("네이버 응답 파싱 실패: {}", e.getMessage());
                throw new UnauthorizedException("네이버 응답 파싱 실패: " + e.getMessage());
            }
            
            if (jsonNode.has("resultcode") && !"00".equals(jsonNode.get("resultcode").asText())) {
                throw new UnauthorizedException("유효하지 않은 네이버 로그인 토큰입니다");
            }

            JsonNode responseNode = jsonNode.get("response");
            if (responseNode == null) {
                throw new UnauthorizedException("네이버 사용자 정보를 가져올 수 없습니다");
            }

            String email = responseNode.has("email") ? responseNode.get("email").asText() : null;
            String name = responseNode.has("name") ? responseNode.get("name").asText() : null;
            String profileImage = responseNode.has("profile_image") ? responseNode.get("profile_image").asText() : null;
            String birthYear = responseNode.has("birthyear") ? responseNode.get("birthyear").asText() : null;
            String birthDay = responseNode.has("birthday") ? responseNode.get("birthday").asText() : null;
            String gender = responseNode.has("gender") ? responseNode.get("gender").asText() : null;
            String mobile = responseNode.has("mobile") ? responseNode.get("mobile").asText() : null;

            return SocialUserInfo.builder()
                    .email(email)
                    .name(name)
                    .profileImage(profileImage)
                    .birthYear(birthYear)
                    .birthDay(birthDay)
                    .gender(gender)
                    .phone(mobile)
                    .build();

        } catch (Exception e) {
            log.error("네이버 사용자 정보 조회 실패: {}", e.getMessage(), e);
            if (e instanceof UnauthorizedException) {
                throw e;
            }
            throw new UnauthorizedException("네이버 사용자 정보를 가져올 수 없습니다: " + e.getMessage());
        }
    }

    private SocialUserInfo getKakaoUserInfo(String accessToken) {
        SocialLoginProperties.Kakao kakao = socialLoginProperties.getKakao();
        
        // 설정값이 없으면 예외 발생
        if (kakao.getClientId().startsWith("${")) {
            log.warn("카카오 로그인 설정값이 없습니다. 환경변수를 설정해주세요.");
            throw new BadRequestException("카카오 로그인 설정이 완료되지 않았습니다");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    kakao.getApiUrl(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                log.error("카카오 응답 파싱 실패: {}", e.getMessage());
                throw new UnauthorizedException("카카오 응답 파싱 실패: " + e.getMessage());
            }
            
            if (jsonNode.has("code")) {
                throw new UnauthorizedException("유효하지 않은 카카오 로그인 토큰입니다");
            }

            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            if (kakaoAccount == null) {
                throw new UnauthorizedException("카카오 사용자 정보를 가져올 수 없습니다");
            }

            String email = kakaoAccount.has("email") ? kakaoAccount.get("email").asText() : null;
            JsonNode profile = kakaoAccount.get("profile");
            String name = profile != null && profile.has("nickname") ? profile.get("nickname").asText() : null;
            String profileImage = profile != null && profile.has("profile_image_url") ? 
                    profile.get("profile_image_url").asText() : null;
            
            String birthYear = null;
            String birthDay = null;
            if (kakaoAccount.has("birthyear")) {
                birthYear = kakaoAccount.get("birthyear").asText();
            }
            if (kakaoAccount.has("birthday")) {
                birthDay = kakaoAccount.get("birthday").asText();
            }
            
            String gender = kakaoAccount.has("gender") ? kakaoAccount.get("gender").asText() : null;
            String phone = kakaoAccount.has("phone_number") ? kakaoAccount.get("phone_number").asText() : null;

            return SocialUserInfo.builder()
                    .email(email)
                    .name(name)
                    .profileImage(profileImage)
                    .birthYear(birthYear)
                    .birthDay(birthDay)
                    .gender(gender)
                    .phone(phone)
                    .build();

        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패: {}", e.getMessage(), e);
            if (e instanceof UnauthorizedException) {
                throw e;
            }
            throw new UnauthorizedException("카카오 사용자 정보를 가져올 수 없습니다: " + e.getMessage());
        }
    }

    @lombok.Getter
    @lombok.Builder
    public static class SocialUserInfo {
        private String email;
        private String name;
        private String profileImage;
        private String birthYear;
        private String birthDay;
        private String gender; // "M" or "F" (네이버), "male" or "female" (카카오)
        private String phone;
    }
}

