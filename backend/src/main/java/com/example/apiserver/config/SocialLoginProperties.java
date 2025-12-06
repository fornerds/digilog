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
    }

    @Getter
    @Setter
    public static class Kakao {
        private String clientId = "${KAKAO_CLIENT_ID:}";
        private String clientSecret = "${KAKAO_CLIENT_SECRET:}";
        private String apiUrl = "https://kapi.kakao.com/v2/user/me";
    }
}

