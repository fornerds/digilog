package com.example.apiserver.repository;

import com.example.apiserver.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    
    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productSkinCodes psc " +
           "WHERE p.isDeleted = false AND psc.isDeleted = false " +
           "AND psc.skinCode = :skinCode " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findBySkinCode(@Param("skinCode") String skinCode, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
           "JOIN p.productPersonalColors ppc " +
           "WHERE p.isDeleted = false AND ppc.isDeleted = false " +
           "AND ppc.personalColor = :personalColor " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findByPersonalColor(@Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findAllWithFilters(@Param("search") String search, @Param("skinCode") String skinCode, 
                                      @Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.updatedAt DESC")
    Page<Product> findAllWithFiltersOrderByUpdatedAt(@Param("search") String search, @Param("skinCode") String skinCode, 
                                                      @Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.name ASC")
    Page<Product> findAllWithFiltersOrderByName(@Param("search") String search, @Param("skinCode") String skinCode, 
                                                @Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.brand ASC")
    Page<Product> findAllWithFiltersOrderByBrand(@Param("search") String search, @Param("skinCode") String skinCode, 
                                                  @Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.price ASC")
    Page<Product> findAllWithFiltersOrderByPriceAsc(@Param("search") String search, @Param("skinCode") String skinCode, 
                                                    @Param("personalColor") String personalColor, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false " +
           "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.brand LIKE %:search%) " +
           "AND (:skinCode IS NULL OR :skinCode = '' OR EXISTS (SELECT 1 FROM ProductSkinCode psc WHERE psc.product.id = p.id AND psc.skinCode = :skinCode AND psc.isDeleted = false)) " +
           "AND (:personalColor IS NULL OR :personalColor = '' OR EXISTS (SELECT 1 FROM ProductPersonalColor ppc WHERE ppc.product.id = p.id AND ppc.personalColor = :personalColor AND ppc.isDeleted = false)) " +
           "ORDER BY p.price DESC")
    Page<Product> findAllWithFiltersOrderByPriceDesc(@Param("search") String search, @Param("skinCode") String skinCode, 
                                                      @Param("personalColor") String personalColor, Pageable pageable);
}

