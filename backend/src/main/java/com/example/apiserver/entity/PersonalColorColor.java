package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal_color_colors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalColorColor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "hex_code", nullable = false, length = 7)
    private String hexCode;

    @Column(nullable = false, length = 20)
    private String category; // "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤"

    @Builder
    public PersonalColorColor(String name, String hexCode, String category) {
        this.name = name;
        this.hexCode = hexCode;
        this.category = category;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public void updateCategory(String category) {
        this.category = category;
    }
}

