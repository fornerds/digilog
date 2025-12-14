package com.example.apiserver.repository;

import com.example.apiserver.entity.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends BaseRepository<Banner, Long> {
    
    @Query("SELECT b FROM Banner b WHERE b.isDeleted = false " +
           "ORDER BY b.createdAt DESC")
    Page<Banner> findAllActive(Pageable pageable);
    
    @Query("SELECT b FROM Banner b WHERE b.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR b.title LIKE %:search% OR b.description LIKE %:search%) " +
           "ORDER BY b.createdAt DESC")
    Page<Banner> findAllWithSearch(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT b FROM Banner b WHERE b.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR b.title LIKE %:search% OR b.description LIKE %:search%) " +
           "ORDER BY b.updatedAt DESC")
    Page<Banner> findAllWithSearchOrderByUpdatedAt(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT b FROM Banner b WHERE b.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR b.title LIKE %:search% OR b.description LIKE %:search%) " +
           "ORDER BY b.title ASC")
    Page<Banner> findAllWithSearchOrderByTitle(@Param("search") String search, Pageable pageable);
}

