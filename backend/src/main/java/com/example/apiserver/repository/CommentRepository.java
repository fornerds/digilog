package com.example.apiserver.repository;

import com.example.apiserver.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {
    
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.deletedAt IS NULL " +
           "ORDER BY c.createdAt ASC")
    Page<Comment> findByPostIdOrderByCreatedAtAsc(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.deletedAt IS NULL " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findByPostIdOrderByCreatedAtDesc(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.deletedAt IS NULL " +
           "ORDER BY c.updatedAt DESC")
    Page<Comment> findByPostIdOrderByUpdatedAtDesc(@Param("postId") Long postId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId AND c.deletedAt IS NULL")
    long countByPostId(@Param("postId") Long postId);
    
    @Query("SELECT c FROM Comment c WHERE c.deletedAt IS NULL " +
           "AND (:postId IS NULL OR c.post.id = :postId) " +
           "AND (:userId IS NULL OR c.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR c.content LIKE %:search% OR c.user.name LIKE %:search% OR c.user.email LIKE %:search%) " +
           "ORDER BY c.createdAt DESC")
    Page<Comment> findAllWithFilters(@Param("postId") Long postId, @Param("userId") Long userId, 
                                     @Param("search") String search, Pageable pageable);
    
    @Query("SELECT c FROM Comment c WHERE c.deletedAt IS NULL " +
           "AND (:postId IS NULL OR c.post.id = :postId) " +
           "AND (:userId IS NULL OR c.user.id = :userId) " +
           "AND (:search IS NULL OR :search = '' OR c.content LIKE %:search% OR c.user.name LIKE %:search% OR c.user.email LIKE %:search%) " +
           "ORDER BY c.updatedAt DESC")
    Page<Comment> findAllWithFiltersOrderByUpdatedAt(@Param("postId") Long postId, @Param("userId") Long userId, 
                                                      @Param("search") String search, Pageable pageable);
}

