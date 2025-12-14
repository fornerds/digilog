package com.example.apiserver.repository;

import com.example.apiserver.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends BaseRepository<Notice, Long> {
    
    @Query("SELECT n FROM Notice n WHERE n.isDeleted = false " +
           "AND (:type IS NULL OR :type = '' OR n.type = :type) " +
           "AND (:search IS NULL OR :search = '' OR n.title LIKE %:search% OR n.content LIKE %:search%) " +
           "ORDER BY n.createdAt DESC")
    Page<Notice> findAllWithFilters(@Param("type") String type, @Param("search") String search, Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.isDeleted = false " +
           "AND (:type IS NULL OR :type = '' OR n.type = :type) " +
           "AND (:search IS NULL OR :search = '' OR n.title LIKE %:search% OR n.content LIKE %:search%) " +
           "ORDER BY n.updatedAt DESC")
    Page<Notice> findAllWithFiltersOrderByUpdatedAt(@Param("type") String type, @Param("search") String search, Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.isDeleted = false " +
           "AND (:type IS NULL OR :type = '' OR n.type = :type) " +
           "AND (:search IS NULL OR :search = '' OR n.title LIKE %:search% OR n.content LIKE %:search%) " +
           "ORDER BY n.startDate DESC")
    Page<Notice> findAllWithFiltersOrderByStartDate(@Param("type") String type, @Param("search") String search, Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.isDeleted = false " +
           "AND (:type IS NULL OR :type = '' OR n.type = :type) " +
           "AND (:search IS NULL OR :search = '' OR n.title LIKE %:search% OR n.content LIKE %:search%) " +
           "ORDER BY n.endDate DESC")
    Page<Notice> findAllWithFiltersOrderByEndDate(@Param("type") String type, @Param("search") String search, Pageable pageable);

    @Query("SELECT n FROM Notice n WHERE n.isDeleted = false " +
           "AND (:type IS NULL OR :type = '' OR n.type = :type) " +
           "AND (:search IS NULL OR :search = '' OR n.title LIKE %:search% OR n.content LIKE %:search%) " +
           "ORDER BY n.title ASC")
    Page<Notice> findAllWithFiltersOrderByTitle(@Param("type") String type, @Param("search") String search, Pageable pageable);
}

