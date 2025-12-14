package com.example.apiserver.service;

import com.example.apiserver.entity.ProfanityWord;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.ProfanityWordRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfanityWordService extends BaseService<ProfanityWord, Long> {

    private final ProfanityWordRepository profanityWordRepository;
    private final ProfanityService profanityService;

    @Override
    protected BaseRepository<ProfanityWord, Long> getRepository() {
        return profanityWordRepository;
    }

    public Page<ProfanityWord> getWords(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "word"));
        return profanityWordRepository.findAllByIsDeletedFalse(pageable);
    }

    @Transactional
    public ProfanityWord createWord(String word) {
        if (word == null || word.isBlank()) {
            throw new BadRequestException("비속어 단어는 필수입니다");
        }

        // 중복 체크
        List<String> existingWords = profanityWordRepository.findAllWords();
        if (existingWords.contains(word)) {
            throw new BadRequestException("이미 등록된 비속어 단어입니다");
        }

        ProfanityWord profanityWord = ProfanityWord.builder()
                .word(word)
                .build();

        ProfanityWord saved = profanityWordRepository.save(profanityWord);
        profanityService.clearCache(); // 캐시 초기화

        return saved;
    }

    @Transactional
    public ProfanityWord updateWord(Long id, String newWord) {
        if (newWord == null || newWord.isBlank()) {
            throw new BadRequestException("비속어 단어는 필수입니다");
        }

        ProfanityWord word = findById(id);

        // 동일한 단어로 변경하는 경우는 허용
        if (!word.getWord().equals(newWord)) {
            // 중복 체크
            List<String> existingWords = profanityWordRepository.findAllWords();
            if (existingWords.contains(newWord)) {
                throw new BadRequestException("이미 등록된 비속어 단어입니다");
            }
        }

        word.updateWord(newWord);
        profanityService.clearCache(); // 캐시 초기화

        return word;
    }

    @Transactional
    public void deleteWord(Long id) {
        ProfanityWord word = findById(id);
        word.softDelete();
        profanityService.clearCache(); // 캐시 초기화
    }

    @Transactional
    public BatchResult batchCreateWords(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new BadRequestException("비속어 단어 배열은 필수입니다");
        }

        List<String> existingWords = profanityWordRepository.findAllWords();
        int created = 0;
        int skipped = 0;

        for (String word : words) {
            if (word == null || word.isBlank()) {
                skipped++;
                continue;
            }

            if (existingWords.contains(word)) {
                skipped++;
                continue;
            }

            ProfanityWord profanityWord = ProfanityWord.builder()
                    .word(word)
                    .build();
            profanityWordRepository.save(profanityWord);
            created++;
        }

        profanityService.clearCache(); // 캐시 초기화

        return new BatchResult(created, skipped, words.size());
    }

    @Getter
    @RequiredArgsConstructor
    public static class BatchResult {
        private final int created;
        private final int skipped;
        private final int total;
    }
}

