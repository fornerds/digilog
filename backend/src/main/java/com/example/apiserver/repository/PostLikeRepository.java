package com.example.apiserver.repository;

import com.example.apiserver.entity.PostLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends BaseRepository<PostLike, Long> {
    
    @Query("SELECT pl FROM PostLike pl WHERE pl.post.id = :postId AND pl.user.id = :userId AND pl.isDeleted = false")
    Optional<PostLike> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.post.id = :postId AND pl.isDeleted = false")
    long countByPostId(@Param("postId") Long postId);
}

