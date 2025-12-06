package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "skin_analysis_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkinAnalysisReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @Column(name = "skin_age", nullable = false)
    private Integer skinAge;

    @Column(name = "skin_condition", nullable = false, length = 50)
    private String skinCondition;

    @Column(name = "skin_condition_description", nullable = false, columnDefinition = "TEXT")
    private String skinConditionDescription;

    @Column(name = "skin_code", nullable = false, length = 10)
    private String skinCode;

    @Column(name = "skin_type", nullable = false, length = 50)
    private String skinType;

    @Column(name = "score_pores", nullable = false)
    private Integer scorePores;

    @Column(name = "score_blackheads", nullable = false)
    private Integer scoreBlackheads;

    @Column(name = "score_pigmentation", nullable = false)
    private Integer scorePigmentation;

    @Column(name = "score_wrinkles", nullable = false)
    private Integer scoreWrinkles;

    @Column(name = "score_porphyrin", nullable = false)
    private Integer scorePorphyrin;

    @Column(name = "score_sensitivity", nullable = false)
    private Integer scoreSensitivity;

    @Column(name = "score_dark_circles", nullable = false)
    private Integer scoreDarkCircles;

    @Column(name = "skin_tags", columnDefinition = "JSON")
    private String skinTags; // JSON 문자열로 저장

    @Column(name = "skin_code_description", nullable = false, columnDefinition = "TEXT")
    private String skinCodeDescription;

    @Column(name = "care_tips", nullable = false, columnDefinition = "JSON")
    private String careTips; // JSON 문자열로 저장

    @Builder
    public SkinAnalysisReport(User user, Integer userAge, Integer skinAge, String skinCondition,
                              String skinConditionDescription, String skinCode, String skinType,
                              Integer scorePores, Integer scoreBlackheads, Integer scorePigmentation,
                              Integer scoreWrinkles, Integer scorePorphyrin, Integer scoreSensitivity,
                              Integer scoreDarkCircles, String skinTags, String skinCodeDescription,
                              String careTips) {
        this.user = user;
        this.userAge = userAge;
        this.skinAge = skinAge;
        this.skinCondition = skinCondition;
        this.skinConditionDescription = skinConditionDescription;
        this.skinCode = skinCode;
        this.skinType = skinType;
        this.scorePores = scorePores;
        this.scoreBlackheads = scoreBlackheads;
        this.scorePigmentation = scorePigmentation;
        this.scoreWrinkles = scoreWrinkles;
        this.scorePorphyrin = scorePorphyrin;
        this.scoreSensitivity = scoreSensitivity;
        this.scoreDarkCircles = scoreDarkCircles;
        this.skinTags = skinTags;
        this.skinCodeDescription = skinCodeDescription;
        this.careTips = careTips;
    }

    public void updateUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public void updateSkinAge(Integer skinAge) {
        this.skinAge = skinAge;
    }

    public void updateSkinCondition(String skinCondition) {
        this.skinCondition = skinCondition;
    }

    public void updateSkinConditionDescription(String skinConditionDescription) {
        this.skinConditionDescription = skinConditionDescription;
    }

    public void updateSkinCode(String skinCode) {
        this.skinCode = skinCode;
    }

    public void updateSkinType(String skinType) {
        this.skinType = skinType;
    }

    public void updateScores(Integer scorePores, Integer scoreBlackheads, Integer scorePigmentation,
                            Integer scoreWrinkles, Integer scorePorphyrin, Integer scoreSensitivity,
                            Integer scoreDarkCircles) {
        this.scorePores = scorePores;
        this.scoreBlackheads = scoreBlackheads;
        this.scorePigmentation = scorePigmentation;
        this.scoreWrinkles = scoreWrinkles;
        this.scorePorphyrin = scorePorphyrin;
        this.scoreSensitivity = scoreSensitivity;
        this.scoreDarkCircles = scoreDarkCircles;
    }

    public void updateSkinTags(String skinTags) {
        this.skinTags = skinTags;
    }

    public void updateSkinCodeDescription(String skinCodeDescription) {
        this.skinCodeDescription = skinCodeDescription;
    }

    public void updateCareTips(String careTips) {
        this.careTips = careTips;
    }
}

