package com.example.apiserver.repository;

import com.example.apiserver.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {
    
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findAllWithSearch(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "ORDER BY p.updatedAt DESC")
    Page<Post> findAllWithSearchOrderByUpdatedAt(@Param("search") String search, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "ORDER BY p.title ASC")
    Page<Post> findAllWithSearchOrderByTitle(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "AND (:authorId IS NULL OR p.author.id = :authorId) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findAllWithFilters(@Param("search") String search, @Param("authorId") Long authorId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "AND (:authorId IS NULL OR p.author.id = :authorId) " +
           "ORDER BY p.updatedAt DESC")
    Page<Post> findAllWithFiltersOrderByUpdatedAt(@Param("search") String search, @Param("authorId") Long authorId, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.title LIKE %:search% OR p.content LIKE %:search%) " +
           "AND (:authorId IS NULL OR p.author.id = :authorId) " +
           "ORDER BY p.title ASC")
    Page<Post> findAllWithFiltersOrderByTitle(@Param("search") String search, @Param("authorId") Long authorId, Pageable pageable);
}

