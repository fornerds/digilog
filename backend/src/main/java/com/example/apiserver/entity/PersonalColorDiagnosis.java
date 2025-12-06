package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_color_diagnoses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalColorDiagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "personal_color", nullable = false, length = 20)
    private String personalColor; // "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤"

    @Column(name = "type_percentage", nullable = false)
    private Integer typePercentage; // 0-100

    @Column(name = "type_descriptions", nullable = false, columnDefinition = "JSON")
    private String typeDescriptions; // JSON 문자열로 저장

    @Column(name = "lab_l", nullable = false, precision = 5, scale = 2)
    private BigDecimal labL;

    @Column(name = "lab_a", nullable = false, precision = 5, scale = 2)
    private BigDecimal labA;

    @Column(name = "lab_b", nullable = false, precision = 5, scale = 2)
    private BigDecimal labB;

    @Column(name = "matching_colors_title", nullable = false, length = 255)
    private String matchingColorsTitle;

    @Column(name = "matching_colors_description", nullable = false, columnDefinition = "TEXT")
    private String matchingColorsDescription;

    @Column(name = "non_matching_colors_title", nullable = false, length = 255)
    private String nonMatchingColorsTitle;

    @Column(name = "non_matching_colors_description", nullable = false, columnDefinition = "TEXT")
    private String nonMatchingColorsDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contouring_guide_image_id")
    private Image contouringGuideImage;

    @Column(name = "makeup_tips", nullable = false, columnDefinition = "JSON")
    private String makeupTips; // JSON 문자열로 저장

    @OneToMany(mappedBy = "diagnosis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalColorDiagnosisColor> diagnosisColors = new ArrayList<>();

    @Builder
    public PersonalColorDiagnosis(User user, String personalColor, Integer typePercentage,
                                  String typeDescriptions, BigDecimal labL, BigDecimal labA, BigDecimal labB,
                                  String matchingColorsTitle, String matchingColorsDescription,
                                  String nonMatchingColorsTitle, String nonMatchingColorsDescription,
                                  Image contouringGuideImage, String makeupTips) {
        this.user = user;
        this.personalColor = personalColor;
        this.typePercentage = typePercentage;
        this.typeDescriptions = typeDescriptions;
        this.labL = labL;
        this.labA = labA;
        this.labB = labB;
        this.matchingColorsTitle = matchingColorsTitle;
        this.matchingColorsDescription = matchingColorsDescription;
        this.nonMatchingColorsTitle = nonMatchingColorsTitle;
        this.nonMatchingColorsDescription = nonMatchingColorsDescription;
        this.contouringGuideImage = contouringGuideImage;
        this.makeupTips = makeupTips;
    }

    public void updatePersonalColor(String personalColor) {
        this.personalColor = personalColor;
    }

    public void updateTypePercentage(Integer typePercentage) {
        this.typePercentage = typePercentage;
    }

    public void updateTypeDescriptions(String typeDescriptions) {
        this.typeDescriptions = typeDescriptions;
    }

    public void updateLabValues(BigDecimal labL, BigDecimal labA, BigDecimal labB) {
        this.labL = labL;
        this.labA = labA;
        this.labB = labB;
    }

    public void updateMatchingColors(String title, String description) {
        this.matchingColorsTitle = title;
        this.matchingColorsDescription = description;
    }

    public void updateNonMatchingColors(String title, String description) {
        this.nonMatchingColorsTitle = title;
        this.nonMatchingColorsDescription = description;
    }

    public void updateContouringGuideImage(Image image) {
        this.contouringGuideImage = image;
    }

    public void updateMakeupTips(String makeupTips) {
        this.makeupTips = makeupTips;
    }
}

