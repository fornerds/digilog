package com.example.apiserver.repository;

import com.example.apiserver.entity.PersonalColorColor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalColorColorRepository extends BaseRepository<PersonalColorColor, Long> {
    
    @Query("SELECT c FROM PersonalColorColor c WHERE c.deletedAt IS NULL " +
           "AND (:category IS NULL OR :category = '' OR c.category = :category) " +
           "ORDER BY c.id ASC")
    Page<PersonalColorColor> findAllActive(@Param("category") String category, Pageable pageable);
    
    @Query("SELECT c FROM PersonalColorColor c WHERE c.deletedAt IS NULL " +
           "AND (:category IS NULL OR :category = '' OR c.category = :category) " +
           "ORDER BY c.name ASC")
    Page<PersonalColorColor> findAllActiveOrderByName(@Param("category") String category, Pageable pageable);
    
    @Query("SELECT c FROM PersonalColorColor c WHERE c.deletedAt IS NULL " +
           "AND (:category IS NULL OR :category = '' OR c.category = :category) " +
           "ORDER BY c.category ASC")
    Page<PersonalColorColor> findAllActiveOrderByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT c FROM PersonalColorColor c WHERE c.deletedAt IS NULL")
    List<PersonalColorColor> findAllActive();
}

