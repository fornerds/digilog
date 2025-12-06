package com.example.apiserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.s3")
public class S3Properties {
    private String accessKeyId = "${AWS_ACCESS_KEY_ID:}";
    private String secretAccessKey = "${AWS_SECRET_ACCESS_KEY:}";
    private String region = "${AWS_REGION:ap-northeast-2}";
    private String bucketName = "${AWS_S3_BUCKET_NAME:}";
    private String baseUrl = "${AWS_S3_BASE_URL:}";
    
    // 이미지 저장 경로 구조
    private String profileImagePath = "profile";
    private String postImagePath = "posts";
    private String noticeImagePath = "notices";
    private String bannerImagePath = "banners";
    private String contouringGuidePath = "personal-colors";
}

