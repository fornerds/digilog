package com.example.apiserver.dto.product;

import com.example.apiserver.entity.Product;
import com.example.apiserver.service.JsonConverter;
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
public class ProductResponse {
    private Long id;
    private String name;
    private String brand;
    private Long price;
    private String imageUrl;
    private String url;
    private String category;
    private List<String> tags;
    private List<String> skinCodes;
    private List<String> personalColors;
    private Boolean isWished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse from(Product product, Boolean isWished, JsonConverter jsonConverter) {
        List<String> tags = parseJsonArray(product.getTags(), jsonConverter);
        List<String> skinCodes = product.getProductSkinCodes().stream()
                .filter(psc -> !psc.isDeleted())
                .map(psc -> psc.getSkinCode())
                .collect(Collectors.toList());
        List<String> personalColors = product.getProductPersonalColors().stream()
                .filter(ppc -> !ppc.isDeleted())
                .map(ppc -> ppc.getPersonalColor())
                .collect(Collectors.toList());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .price(product.getPrice())
                .imageUrl(product.getImage() != null ? product.getImage().getUrl() : null)
                .url(product.getUrl())
                .category(product.getCategory())
                .tags(tags)
                .skinCodes(skinCodes)
                .personalColors(personalColors)
                .isWished(isWished != null ? isWished : false)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private static List<String> parseJsonArray(String json, JsonConverter jsonConverter) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return jsonConverter.fromJsonArray(json, String.class);
        } catch (Exception e) {
            return List.of();
        }
    }
}

