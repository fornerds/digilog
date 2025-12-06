package com.example.apiserver.repository;

import com.example.apiserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    
    boolean existsByEmailAndDeletedAtIsNull(String email);
    
    boolean existsByPhoneAndDeletedAtIsNull(String phone);
    
    User findByPhoneAndDeletedAtIsNull(String phone);
    
    Optional<User> findByEmailAndProviderAndDeletedAtIsNull(String email, com.example.apiserver.entity.Provider provider);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR u.email LIKE %:search% OR u.name LIKE %:search%) " +
           "ORDER BY u.createdAt DESC")
    Page<User> findAllWithSearch(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR u.email LIKE %:search% OR u.name LIKE %:search%) " +
           "ORDER BY u.updatedAt DESC")
    Page<User> findAllWithSearchOrderByUpdatedAt(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR u.email LIKE %:search% OR u.name LIKE %:search%) " +
           "ORDER BY u.name ASC")
    Page<User> findAllWithSearchOrderByName(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR u.email LIKE %:search% OR u.name LIKE %:search%) " +
           "ORDER BY u.email ASC")
    Page<User> findAllWithSearchOrderByEmail(@Param("search") String search, Pageable pageable);
}

