package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_skin_codes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "skin_code"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSkinCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "skin_code", nullable = false, length = 10)
    private String skinCode;

    @Builder
    public ProductSkinCode(Product product, String skinCode) {
        this.product = product;
        this.skinCode = skinCode;
    }

    public void updateSkinCode(String skinCode) {
        this.skinCode = skinCode;
    }
}

