package com.example.apiserver.service;

import com.example.apiserver.config.S3Properties;
import com.example.apiserver.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Properties s3Properties;
    private S3Client s3Client;

    private S3Client getS3Client() {
        if (s3Client == null) {
            // 설정값이 없으면 null 반환 (설정값 추가 전까지)
            if (s3Properties.getAccessKeyId().startsWith("${") || 
                s3Properties.getSecretAccessKey().startsWith("${") ||
                s3Properties.getBucketName().startsWith("${")) {
                log.warn("S3 설정값이 없습니다. 환경변수를 설정해주세요.");
                return null;
            }

            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    s3Properties.getAccessKeyId(),
                    s3Properties.getSecretAccessKey()
            );

            s3Client = S3Client.builder()
                    .region(Region.of(s3Properties.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();
        }
        return s3Client;
    }

    public String uploadFile(MultipartFile file, String folderPath) {
        if (file.isEmpty()) {
            throw new BadRequestException("파일이 비어있습니다");
        }

        // 파일 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("이미지 파일만 업로드 가능합니다");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new BadRequestException("파일 크기는 5MB 이하여야 합니다");
        }

        S3Client client = getS3Client();
        if (client == null) {
            // 설정값이 없으면 임시 URL 반환 (나중에 실제 업로드로 교체)
            log.warn("S3 클라이언트가 초기화되지 않았습니다. 설정값을 확인해주세요.");
            return generateTemporaryUrl(folderPath, file.getOriginalFilename());
        }

        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String key = folderPath + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .contentType(contentType)
                    .build();

            try (InputStream inputStream = file.getInputStream()) {
                client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            }

            // S3 URL 생성
            String url = generateS3Url(key);
            log.info("S3 업로드 성공: {}", url);
            return url;

        } catch (S3Exception e) {
            log.error("S3 업로드 실패: {}", e.getMessage(), e);
            throw new BadRequestException("이미지 업로드에 실패했습니다: " + e.getMessage());
        } catch (IOException e) {
            log.error("파일 읽기 실패: {}", e.getMessage(), e);
            throw new BadRequestException("파일 처리 중 오류가 발생했습니다");
        }
    }

    public List<String> uploadFiles(List<MultipartFile> files, String folderPath) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls.add(uploadFile(file, folderPath));
            }
        }
        return urls;
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        S3Client client = getS3Client();
        if (client == null) {
            log.warn("S3 클라이언트가 초기화되지 않아 이미지 삭제를 건너뜁니다.");
            return;
        }

        try {
            // S3 URL에서 key 추출
            String key = extractKeyFromUrl(imageUrl);
            if (key == null) {
                log.warn("S3 URL에서 key를 추출할 수 없습니다: {}", imageUrl);
                return;
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .build();

            client.deleteObject(deleteObjectRequest);
            log.info("S3 삭제 성공: {}", key);

        } catch (S3Exception e) {
            log.error("S3 삭제 실패: {}", e.getMessage(), e);
            // 삭제 실패해도 예외를 던지지 않음 (이미 삭제된 경우 등)
        }
    }

    public void deleteFiles(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        for (String url : imageUrls) {
            deleteFile(url);
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    private String generateS3Url(String key) {
        if (s3Properties.getBaseUrl() != null && !s3Properties.getBaseUrl().isBlank() && 
            !s3Properties.getBaseUrl().startsWith("${")) {
            return s3Properties.getBaseUrl() + "/" + key;
        }
        // 기본 S3 URL 형식
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Properties.getBucketName(),
                s3Properties.getRegion(),
                key);
    }

    private String extractKeyFromUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        
        // S3 URL에서 key 추출
        // 형식: https://bucket.s3.region.amazonaws.com/key
        // 또는: https://baseUrl/key
        try {
            if (url.contains(".amazonaws.com/")) {
                int index = url.indexOf(".amazonaws.com/");
                return url.substring(index + ".amazonaws.com/".length());
            } else if (s3Properties.getBaseUrl() != null && 
                      !s3Properties.getBaseUrl().isBlank() && 
                      !s3Properties.getBaseUrl().startsWith("${") &&
                      url.startsWith(s3Properties.getBaseUrl())) {
                return url.substring(s3Properties.getBaseUrl().length() + 1);
            }
        } catch (Exception e) {
            log.warn("URL에서 key 추출 실패: {}", url);
        }
        return null;
    }

    private String generateTemporaryUrl(String folderPath, String originalFilename) {
        // 설정값이 없을 때 임시 URL 생성 (나중에 실제 업로드로 교체)
        String fileName = generateFileName(originalFilename);
        return "https://s3.example.com/" + folderPath + "/" + fileName;
    }
}

