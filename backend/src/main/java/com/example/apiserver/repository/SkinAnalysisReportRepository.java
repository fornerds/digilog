package com.example.apiserver.repository;

import com.example.apiserver.entity.SkinAnalysisReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinAnalysisReportRepository extends BaseRepository<SkinAnalysisReport, Long> {
    
    @Query("SELECT r FROM SkinAnalysisReport r WHERE r.isDeleted = false " +
           "ORDER BY r.createdAt DESC")
    Page<SkinAnalysisReport> findAllActive(Pageable pageable);

    @Query("SELECT r FROM SkinAnalysisReport r WHERE r.isDeleted = false " +
           "ORDER BY r.updatedAt DESC")
    Page<SkinAnalysisReport> findAllActiveOrderByUpdatedAt(Pageable pageable);
    
    @Query("SELECT r FROM SkinAnalysisReport r WHERE r.isDeleted = false " +
           "AND (:userId IS NULL OR r.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR r.user.name LIKE %:search% OR r.user.email LIKE %:search% OR r.skinCode LIKE %:search%) " +
           "ORDER BY r.createdAt DESC")
    Page<SkinAnalysisReport> findAllWithFilters(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
    
    @Query("SELECT r FROM SkinAnalysisReport r WHERE r.isDeleted = false " +
           "AND (:userId IS NULL OR r.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR r.user.name LIKE %:search% OR r.user.email LIKE %:search% OR r.skinCode LIKE %:search%) " +
           "ORDER BY r.updatedAt DESC")
    Page<SkinAnalysisReport> findAllWithFiltersOrderByUpdatedAt(@Param("userId") Long userId, @Param("search") String search, Pageable pageable);
}

