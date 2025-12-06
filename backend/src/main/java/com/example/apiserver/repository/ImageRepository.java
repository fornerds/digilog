package com.example.apiserver.repository;

import com.example.apiserver.entity.Image;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends BaseRepository<Image, Long> {
    Optional<Image> findByUrlAndDeletedAtIsNull(String url);
}

