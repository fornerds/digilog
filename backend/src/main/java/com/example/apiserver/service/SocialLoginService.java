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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final SocialLoginProperties socialLoginProperties;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * OAuth 인증 코드를 액세스 토큰으로 교환
     */
    public String exchangeCodeForToken(Provider provider, String code, String state) {
        switch (provider) {
            case NAVER:
                return exchangeNaverCodeForToken(code, state);
            case KAKAO:
                return exchangeKakaoCodeForToken(code);
            default:
                throw new BadRequestException("지원하지 않는 소셜 로그인 제공자입니다");
        }
    }

    /**
     * 액세스 토큰으로 사용자 정보 가져오기
     */
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

    /**
     * 네이버 OAuth 인증 코드를 액세스 토큰으로 교환
     */
    private String exchangeNaverCodeForToken(String code, String state) {
        SocialLoginProperties.Naver naver = socialLoginProperties.getNaver();
        
        if (naver.getClientId().startsWith("${") || naver.getClientSecret().startsWith("${")) {
            log.warn("네이버 로그인 설정값이 없습니다. 환경변수를 설정해주세요.");
            throw new BadRequestException("네이버 로그인 설정이 완료되지 않았습니다");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", naver.getClientId());
            params.add("client_secret", naver.getClientSecret());
            params.add("code", code);
            params.add("state", state);
            params.add("redirect_uri", naver.getRedirectUri());

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    naver.getTokenUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                log.error("네이버 토큰 응답 파싱 실패: body={}, error={}", response.getBody(), e.getMessage());
                throw new UnauthorizedException("네이버 토큰 응답 파싱 실패: " + e.getMessage());
            }

            if (jsonNode.has("error")) {
                String error = jsonNode.get("error").asText();
                String errorDescription = jsonNode.has("error_description") ? 
                        jsonNode.get("error_description").asText() : "";
                log.error("네이버 토큰 교환 실패: error={}, description={}, response={}", 
                        error, errorDescription, response.getBody());
                throw new UnauthorizedException("네이버 토큰 교환 실패: " + errorDescription);
            }

            if (!jsonNode.has("access_token")) {
                log.error("네이버 액세스 토큰 없음: response={}", response.getBody());
                throw new UnauthorizedException("네이버 액세스 토큰을 받을 수 없습니다");
            }

            return jsonNode.get("access_token").asText();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            log.error("네이버 토큰 교환 HTTP 에러: status={}, body={}, message={}", 
                    e.getStatusCode(), responseBody, e.getMessage());
            
            try {
                JsonNode errorNode = objectMapper.readTree(responseBody);
                String errorDescription = errorNode.has("error_description") ? 
                        errorNode.get("error_description").asText() : 
                        (errorNode.has("error") ? errorNode.get("error").asText() : responseBody);
                throw new UnauthorizedException("네이버 토큰 교환 실패: " + errorDescription);
            } catch (JsonProcessingException jsonEx) {
                throw new UnauthorizedException("네이버 토큰 교환 실패: " + responseBody);
            }
        } catch (Exception e) {
            log.error("네이버 토큰 교환 실패: {}", e.getMessage(), e);
            if (e instanceof UnauthorizedException || e instanceof BadRequestException) {
                throw e;
            }
            throw new UnauthorizedException("네이버 토큰 교환 실패: " + e.getMessage());
        }
    }

    /**
     * 카카오 OAuth 인증 코드를 액세스 토큰으로 교환
     */
    private String exchangeKakaoCodeForToken(String code) {
        SocialLoginProperties.Kakao kakao = socialLoginProperties.getKakao();
        
        if (kakao.getClientId().startsWith("${") || kakao.getClientSecret().startsWith("${")) {
            log.warn("카카오 로그인 설정값이 없습니다. 환경변수를 설정해주세요.");
            throw new BadRequestException("카카오 로그인 설정이 완료되지 않았습니다");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakao.getClientId());
            params.add("client_secret", kakao.getClientSecret());
            params.add("code", code);
            params.add("redirect_uri", kakao.getRedirectUri());

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);


            ResponseEntity<String> response = restTemplate.exchange(
                    kakao.getTokenUrl(),
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            JsonNode jsonNode;
            try {
                jsonNode = objectMapper.readTree(response.getBody());
            } catch (JsonProcessingException e) {
                log.error("카카오 토큰 응답 파싱 실패: body={}, error={}", response.getBody(), e.getMessage());
                throw new UnauthorizedException("카카오 토큰 응답 파싱 실패: " + e.getMessage());
            }

            if (jsonNode.has("error")) {
                String error = jsonNode.get("error").asText();
                String errorDescription = jsonNode.has("error_description") ? 
                        jsonNode.get("error_description").asText() : "";
                log.error("카카오 토큰 교환 실패: error={}, description={}, response={}", 
                        error, errorDescription, response.getBody());
                throw new UnauthorizedException("카카오 토큰 교환 실패: " + errorDescription);
            }

            if (!jsonNode.has("access_token")) {
                log.error("카카오 액세스 토큰 없음: response={}", response.getBody());
                throw new UnauthorizedException("카카오 액세스 토큰을 받을 수 없습니다");
            }

            return jsonNode.get("access_token").asText();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            
            // WWW-Authenticate 헤더에서 에러 정보 추출
            String wwwAuthenticate = e.getResponseHeaders().getFirst("WWW-Authenticate");
            String errorDescription = null;
            
            if (wwwAuthenticate != null && !wwwAuthenticate.isEmpty()) {
                // WWW-Authenticate: Bearer realm="oauth", error="invalid_client", error_description="Bad client credentials"
                if (wwwAuthenticate.contains("error_description=")) {
                    int start = wwwAuthenticate.indexOf("error_description=\"") + "error_description=\"".length();
                    int end = wwwAuthenticate.indexOf("\"", start);
                    if (end > start) {
                        errorDescription = wwwAuthenticate.substring(start, end);
                    }
                }
            }
            
            log.error("카카오 토큰 교환 HTTP 에러: status={}, body={}, WWW-Authenticate={}, message={}", 
                    e.getStatusCode(), 
                    responseBody != null && !responseBody.isEmpty() ? responseBody : "[empty body]",
                    wwwAuthenticate,
                    e.getMessage());
            
            // 응답 body에서 에러 정보 추출 시도
            if (responseBody != null && !responseBody.isEmpty()) {
                try {
                    JsonNode errorNode = objectMapper.readTree(responseBody);
                    String error = errorNode.has("error") ? errorNode.get("error").asText() : "";
                    String desc = errorNode.has("error_description") ? 
                            errorNode.get("error_description").asText() : 
                            (errorNode.has("error") ? errorNode.get("error").asText() : responseBody);
                    if (desc != null && !desc.isEmpty()) {
                        errorDescription = desc;
                    }
                    log.error("카카오 OAuth 에러 상세: error={}, description={}", error, desc);
                } catch (JsonProcessingException jsonEx) {
                    // JSON 파싱 실패 시 무시
                }
            }
            
            // 에러 메시지 결정
            if (errorDescription != null && !errorDescription.isEmpty()) {
                // invalid_grant 에러는 인증 코드 문제
                if (errorDescription.contains("authorization code not found") || 
                    errorDescription.contains("invalid_grant") ||
                    (responseBody != null && responseBody.contains("\"error\":\"invalid_grant\""))) {
                    throw new BadRequestException("인증 코드가 만료되었거나 이미 사용되었습니다. 다시 로그인해주세요.");
                }
                throw new UnauthorizedException("카카오 토큰 교환 실패: " + errorDescription);
            } else if (wwwAuthenticate != null && wwwAuthenticate.contains("invalid_client")) {
                throw new UnauthorizedException("카카오 토큰 교환 실패: client_id 또는 client_secret이 잘못되었습니다. 카카오 개발자 센터에서 확인해주세요");
            } else {
                throw new UnauthorizedException("카카오 토큰 교환 실패: redirect_uri가 카카오 개발자 센터에 등록된 값과 일치하는지 확인해주세요");
            }
        } catch (Exception e) {
            log.error("카카오 토큰 교환 실패: {}", e.getMessage(), e);
            if (e instanceof UnauthorizedException || e instanceof BadRequestException) {
                throw e;
            }
            throw new UnauthorizedException("카카오 토큰 교환 실패: " + e.getMessage());
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

            // 네이버 고유 ID 추출
            String providerId = responseNode.has("id") ? responseNode.get("id").asText() : null;
            String email = responseNode.has("email") ? responseNode.get("email").asText() : null;
            String name = responseNode.has("name") ? responseNode.get("name").asText() : null;
            String profileImage = responseNode.has("profile_image") ? responseNode.get("profile_image").asText() : null;
            String birthYear = responseNode.has("birthyear") ? responseNode.get("birthyear").asText() : null;
            String birthDay = responseNode.has("birthday") ? responseNode.get("birthday").asText() : null;
            String gender = responseNode.has("gender") ? responseNode.get("gender").asText() : null;
            String mobile = responseNode.has("mobile") ? responseNode.get("mobile").asText() : null;

            return SocialUserInfo.builder()
                    .providerId(providerId)
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
                log.error("카카오 응답 파싱 실패: {}, body: {}", e.getMessage(), response.getBody());
                throw new UnauthorizedException("카카오 응답 파싱 실패: " + e.getMessage());
            }
            
            if (jsonNode.has("code")) {
                log.error("카카오 사용자 정보 조회 에러 코드 발견: {}", jsonNode.get("code"));
                throw new UnauthorizedException("유효하지 않은 카카오 로그인 토큰입니다");
            }

            JsonNode kakaoAccount = jsonNode.get("kakao_account");
            if (kakaoAccount == null) {
                log.warn("kakao_account가 null입니다. 카카오 개발자 센터에서 동의 항목을 설정했는지 확인하세요. 전체 응답: {}", jsonNode.toString());
            }

            // 카카오 고유 ID 추출 (최상위 id 필드)
            String providerId = jsonNode.has("id") ? jsonNode.get("id").asText() : null;
            String email = null;
            String name = null;
            String profileImage = null;
            String birthYear = null;
            String birthDay = null;
            String gender = null;
            String phone = null;
            
            // kakao_account가 있는 경우에만 정보 추출
            if (kakaoAccount != null) {
                email = kakaoAccount.has("email") ? kakaoAccount.get("email").asText() : null;
                
                JsonNode profile = kakaoAccount.get("profile");
                
                if (profile != null) {
                    name = profile.has("nickname") ? profile.get("nickname").asText() : null;
                    profileImage = profile.has("profile_image_url") ? 
                            profile.get("profile_image_url").asText() : null;
                }
                
                if (kakaoAccount.has("birthyear")) {
                    birthYear = kakaoAccount.get("birthyear").asText();
                }
                if (kakaoAccount.has("birthday")) {
                    birthDay = kakaoAccount.get("birthday").asText();
                }
                
                gender = kakaoAccount.has("gender") ? kakaoAccount.get("gender").asText() : null;
                phone = kakaoAccount.has("phone_number") ? kakaoAccount.get("phone_number").asText() : null;
            }

            return SocialUserInfo.builder()
                    .providerId(providerId)
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
        private String providerId; // 네이버/카카오 고유 ID
        private String email;
        private String name;
        private String profileImage;
        private String birthYear;
        private String birthDay;
        private String gender; // "M" or "F" (네이버), "male" or "female" (카카오)
        private String phone;
    }
}

