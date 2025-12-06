package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "JSON")
    private String tags; // JSON 문자열로 저장

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductWish> productWishes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSkinCode> productSkinCodes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPersonalColor> productPersonalColors = new ArrayList<>();

    @Builder
    public Product(String name, String brand, Long price, Image image, String url, 
                   String category, String tags) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.image = image;
        this.url = url;
        this.category = category;
        this.tags = tags;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateBrand(String brand) {
        this.brand = brand;
    }

    public void updatePrice(Long price) {
        this.price = price;
    }

    public void updateImage(Image image) {
        this.image = image;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

    public void updateCategory(String category) {
        this.category = category;
    }

    public void updateTags(String tags) {
        this.tags = tags;
    }
}

