package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_personal_colors", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "personal_color"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductPersonalColor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "personal_color", nullable = false, length = 20)
    private String personalColor;

    @Builder
    public ProductPersonalColor(Product product, String personalColor) {
        this.product = product;
        this.personalColor = personalColor;
    }

    public void updatePersonalColor(String personalColor) {
        this.personalColor = personalColor;
    }
}

