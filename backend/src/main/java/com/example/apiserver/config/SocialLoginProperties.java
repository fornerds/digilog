package com.example.apiserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.social")
public class SocialLoginProperties {
    private Naver naver = new Naver();
    private Kakao kakao = new Kakao();

    @Getter
    @Setter
    public static class Naver {
        private String clientId = "${NAVER_CLIENT_ID:}";
        private String clientSecret = "${NAVER_CLIENT_SECRET:}";
        private String apiUrl = "https://openapi.naver.com/v1/nid/me";
        private String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        private String redirectUri = "${NAVER_REDIRECT_URI:http://localhost:3000/auth/naver/callback}";
    }

    @Getter
    @Setter
    public static class Kakao {
        private String clientId = "${KAKAO_CLIENT_ID:}";
        private String clientSecret = "${KAKAO_CLIENT_SECRET:}";
        private String apiUrl = "https://kapi.kakao.com/v2/user/me";
        private String tokenUrl = "https://kauth.kakao.com/oauth/token";
        private String redirectUri = "${KAKAO_REDIRECT_URI:http://localhost:3000/auth/kakao/callback}";
    }
}

