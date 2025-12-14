package com.example.apiserver.repository;

import com.example.apiserver.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {
    
    Optional<T> findByIdAndIsDeletedFalse(ID id);
    
    List<T> findAllByIsDeletedFalse();
    
    Page<T> findAllByIsDeletedFalse(Pageable pageable);
    
    boolean existsByIdAndIsDeletedFalse(ID id);
}

