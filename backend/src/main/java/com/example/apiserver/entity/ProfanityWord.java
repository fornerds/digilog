package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profanity_words")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfanityWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String word;

    @Builder
    public ProfanityWord(String word) {
        this.word = word;
    }

    public void updateWord(String word) {
        this.word = word;
    }
}

