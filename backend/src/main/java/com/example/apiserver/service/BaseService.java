package com.example.apiserver.service;

import com.example.apiserver.entity.BaseEntity;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;

import java.util.List;

public abstract class BaseService<T extends BaseEntity, ID> {
    
    protected abstract BaseRepository<T, ID> getRepository();
    
    public T findById(ID id) {
        return getRepository()
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }
    
    public List<T> findAll() {
        return getRepository().findAllByDeletedAtIsNull();
    }
    
    public T save(T entity) {
        return getRepository().save(entity);
    }
    
    public void deleteById(ID id) {
        T entity = findById(id);
        entity.softDelete();
        getRepository().save(entity);
    }
    
    public boolean existsById(ID id) {
        return getRepository().existsByIdAndDeletedAtIsNull(id);
    }
}

