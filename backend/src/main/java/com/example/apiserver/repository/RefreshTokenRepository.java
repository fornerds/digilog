package com.example.apiserver.repository;

import com.example.apiserver.entity.RefreshToken;
import com.example.apiserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    
    Optional<RefreshToken> findByTokenAndIsDeletedFalse(String token);
    
    Optional<RefreshToken> findByUserAndIsDeletedFalse(User user);
    
    Optional<RefreshToken> findByUserIdAndIsDeletedFalse(Long userId);
    
    void deleteByUser(User user);
    
    void deleteByDeletedAtBefore(LocalDateTime dateTime);
}
