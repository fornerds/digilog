package com.example.apiserver.service;

import com.example.apiserver.repository.ProfanityWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfanityService {

    private final ProfanityWordRepository profanityWordRepository;
    private List<String> cachedWords = null;

    public boolean hasProfanity(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }

        List<String> words = getProfanityWords();
        String lowerText = text.toLowerCase();

        for (String word : words) {
            if (lowerText.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public List<String> detectProfanity(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> words = getProfanityWords();
        List<String> detected = new ArrayList<>();
        String lowerText = text.toLowerCase();

        for (String word : words) {
            if (lowerText.contains(word.toLowerCase())) {
                detected.add(word);
            }
        }
        return detected;
    }

    public List<String> detectProfanityInTexts(String... texts) {
        List<String> allDetected = new ArrayList<>();
        for (String text : texts) {
            if (text != null && !text.isBlank()) {
                allDetected.addAll(detectProfanity(text));
            }
        }
        return allDetected.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getProfanityWords() {
        if (cachedWords == null) {
            cachedWords = profanityWordRepository.findAllWords();
        }
        return cachedWords;
    }

    @Transactional
    public void clearCache() {
        this.cachedWords = null;
    }
}

