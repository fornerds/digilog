package com.example.apiserver.dto.post;

import com.example.apiserver.entity.Post;
import com.example.apiserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    private List<String> hashtags;
    private List<String> images;
    private Long commentCount;
    private Long likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostResponse from(Post post, User author, List<String> imageUrls, 
                                   Long commentCount, Long likeCount) {
        List<String> hashtags = parseHashtags(post.getHashtags());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorId(author != null ? author.getId() : null)
                .authorName(author != null ? author.getName() : null)
                .hashtags(hashtags)
                .images(imageUrls != null ? imageUrls : List.of())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private static List<String> parseHashtags(String hashtagsJson) {
        if (hashtagsJson == null || hashtagsJson.isBlank()) {
            return List.of();
        }
        // 간단한 JSON 파싱 (실제로는 Jackson ObjectMapper 사용 권장)
        try {
            String cleaned = hashtagsJson.trim();
            if (cleaned.startsWith("[") && cleaned.endsWith("]")) {
                cleaned = cleaned.substring(1, cleaned.length() - 1);
                if (cleaned.isBlank()) {
                    return List.of();
                }
                return List.of(cleaned.split(","))
                        .stream()
                        .map(s -> s.trim().replaceAll("\"", ""))
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            // 파싱 실패 시 빈 리스트 반환
        }
        return List.of();
    }
}

