package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "personal_color_diagnosis_colors")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalColorDiagnosisColor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnosis_id", nullable = false)
    private PersonalColorDiagnosis diagnosis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", nullable = false)
    private PersonalColorColor color;

    @Column(nullable = false, length = 20)
    private String type; // "matching" or "nonMatching"

    @Builder
    public PersonalColorDiagnosisColor(PersonalColorDiagnosis diagnosis, PersonalColorColor color, String type) {
        this.diagnosis = diagnosis;
        this.color = color;
        this.type = type;
    }
}

