package com.example.apiserver.repository;

import com.example.apiserver.entity.ProfanityWord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfanityWordRepository extends BaseRepository<ProfanityWord, Long> {
    
    @Query("SELECT pw.word FROM ProfanityWord pw WHERE pw.deletedAt IS NULL")
    List<String> findAllWords();
}

