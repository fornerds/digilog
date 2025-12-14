package com.example.apiserver.repository;

import com.example.apiserver.entity.PersonalColorDiagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalColorDiagnosisRepository extends BaseRepository<PersonalColorDiagnosis, Long> {
    
    @Query("SELECT d FROM PersonalColorDiagnosis d WHERE d.isDeleted = false " +
           "AND (:userId IS NULL OR d.user.id = :userId) " +
           "ORDER BY d.createdAt DESC")
    Page<PersonalColorDiagnosis> findAllActive(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT d FROM PersonalColorDiagnosis d WHERE d.isDeleted = false " +
           "AND (:userId IS NULL OR d.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR d.user.name LIKE %:search% OR d.user.email LIKE %:search% OR d.personalColor LIKE %:search%) " +
           "ORDER BY d.createdAt DESC")
    Page<PersonalColorDiagnosis> findAllWithFilters(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT d FROM PersonalColorDiagnosis d WHERE d.isDeleted = false " +
           "AND (:userId IS NULL OR d.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR d.user.name LIKE %:search% OR d.user.email LIKE %:search% OR d.personalColor LIKE %:search%) " +
           "ORDER BY d.updatedAt DESC")
    Page<PersonalColorDiagnosis> findAllWithFiltersOrderByUpdatedAt(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
}

