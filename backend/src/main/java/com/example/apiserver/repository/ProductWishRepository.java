package com.example.apiserver.repository;

import com.example.apiserver.entity.ProductWish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductWishRepository extends BaseRepository<ProductWish, Long> {
    
    @Query("SELECT pw FROM ProductWish pw WHERE pw.product.id = :productId " +
           "AND pw.user.id = :userId AND pw.isDeleted = false")
    Optional<ProductWish> findByProductIdAndUserId(@Param("productId") Long productId, 
                                                    @Param("userId") Long userId);
}

