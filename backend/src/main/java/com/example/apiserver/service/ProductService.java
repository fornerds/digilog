package com.example.apiserver.service;

import com.example.apiserver.dto.product.ProductRequest;
import com.example.apiserver.dto.product.ProductResponse;
import com.example.apiserver.entity.Image;
import com.example.apiserver.entity.Product;
import com.example.apiserver.entity.ProductPersonalColor;
import com.example.apiserver.entity.ProductSkinCode;
import com.example.apiserver.entity.ProductWish;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.ImageRepository;
import com.example.apiserver.repository.ProductRepository;
import com.example.apiserver.repository.ProductWishRepository;
import com.example.apiserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService extends BaseService<Product, Long> {

    private final ProductRepository productRepository;
    private final ProductWishRepository productWishRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final JsonConverter jsonConverter;

    @Override
    protected BaseRepository<Product, Long> getRepository() {
        return productRepository;
    }

    public Page<ProductResponse> getProductsBySkinCode(String skinCode, int page, int limit, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> products = productRepository.findBySkinCode(skinCode, pageable);
        
        return products.map(product -> {
            Boolean isWished = userId != null ? checkWishStatus(product.getId(), userId) : false;
            return ProductResponse.from(product, isWished, jsonConverter);
        });
    }

    public Page<ProductResponse> getProductsByPersonalColor(String personalColor, int page, int limit, Long userId) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> products = productRepository.findByPersonalColor(personalColor, pageable);
        
        return products.map(product -> {
            Boolean isWished = userId != null ? checkWishStatus(product.getId(), userId) : false;
            return ProductResponse.from(product, isWished, jsonConverter);
        });
    }

    @Transactional
    public ProductResponse toggleWish(Long productId, Long userId, Boolean isWished) {
        Product product = findById(productId);
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        Optional<ProductWish> existingWish = productWishRepository.findByProductIdAndUserId(productId, userId);

        if (Boolean.TRUE.equals(isWished)) {
            // 찜 추가
            if (existingWish.isEmpty()) {
                ProductWish wish = ProductWish.builder()
                        .product(product)
                        .user(user)
                        .build();
                productWishRepository.save(wish);
            } else if (existingWish.get().isDeleted()) {
                // 삭제된 찜이 있으면 새로 생성
                ProductWish newWish = ProductWish.builder()
                        .product(product)
                        .user(user)
                        .build();
                productWishRepository.save(newWish);
            }
        } else {
            // 찜 취소
            existingWish.ifPresent(wish -> wish.softDelete());
        }

        Boolean currentWishStatus = checkWishStatus(productId, userId);
        return ProductResponse.from(product, currentWishStatus, jsonConverter);
    }

    private Boolean checkWishStatus(Long productId, Long userId) {
        Optional<ProductWish> wish = productWishRepository.findByProductIdAndUserId(productId, userId);
        return wish.isPresent() && !wish.get().isDeleted();
    }

    // 관리자용 메서드들
    public Page<ProductResponse> getProductsForAdmin(int page, int limit, String search, String skinCode, 
                                                     String personalColor, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Product> products;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                products = productRepository.findAllWithFiltersOrderByUpdatedAt(search, skinCode, personalColor, pageable);
                break;
            case "name":
                products = productRepository.findAllWithFiltersOrderByName(search, skinCode, personalColor, pageable);
                break;
            case "brand":
                products = productRepository.findAllWithFiltersOrderByBrand(search, skinCode, personalColor, pageable);
                break;
            case "price":
                if ("asc".equalsIgnoreCase(order)) {
                    products = productRepository.findAllWithFiltersOrderByPriceAsc(search, skinCode, personalColor, pageable);
                } else {
                    products = productRepository.findAllWithFiltersOrderByPriceDesc(search, skinCode, personalColor, pageable);
                }
                break;
            default:
                products = productRepository.findAllWithFilters(search, skinCode, personalColor, pageable);
        }
        
        return products.map(product -> ProductResponse.from(product, false, jsonConverter));
    }

    public ProductResponse getProductForAdmin(Long productId) {
        Product product = findById(productId);
        return ProductResponse.from(product, false, jsonConverter);
    }

    @Transactional
    public ProductResponse createProductForAdmin(ProductRequest.CreateAdmin request) {
        // 피부코드 또는 퍼스널컬러 중 하나 이상은 필수
        if ((request.getSkinCodes() == null || request.getSkinCodes().isEmpty()) &&
            (request.getPersonalColors() == null || request.getPersonalColors().isEmpty())) {
            throw new BadRequestException("피부코드 또는 퍼스널컬러 중 하나는 필수입니다");
        }

        Image image = imageRepository.findByUrlAndIsDeletedFalse(request.getImageUrl())
                .orElseThrow(() -> new ResourceNotFoundException("이미지를 찾을 수 없습니다"));

        String tagsJson = request.getTags() != null ? jsonConverter.toJson(request.getTags()) : null;

        Product product = Product.builder()
                .name(request.getName())
                .brand(request.getBrand())
                .price(request.getPrice())
                .image(image)
                .url(request.getUrl())
                .category(request.getCategory())
                .tags(tagsJson)
                .build();

        Product savedProduct = productRepository.save(product);

        // 피부코드 연결
        if (request.getSkinCodes() != null && !request.getSkinCodes().isEmpty()) {
            for (String skinCode : request.getSkinCodes()) {
                ProductSkinCode productSkinCode = ProductSkinCode.builder()
                        .product(savedProduct)
                        .skinCode(skinCode)
                        .build();
                savedProduct.getProductSkinCodes().add(productSkinCode);
            }
        }

        // 퍼스널컬러 연결
        if (request.getPersonalColors() != null && !request.getPersonalColors().isEmpty()) {
            for (String personalColor : request.getPersonalColors()) {
                ProductPersonalColor productPersonalColor = ProductPersonalColor.builder()
                        .product(savedProduct)
                        .personalColor(personalColor)
                        .build();
                savedProduct.getProductPersonalColors().add(productPersonalColor);
            }
        }

        return ProductResponse.from(savedProduct, false, jsonConverter);
    }

    @Transactional
    public ProductResponse updateProductForAdmin(Long productId, ProductRequest.UpdateAdmin request) {
        Product product = findById(productId);

        if (request.getName() != null) {
            product.updateName(request.getName());
        }
        if (request.getBrand() != null) {
            product.updateBrand(request.getBrand());
        }
        if (request.getPrice() != null) {
            product.updatePrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            Image image = imageRepository.findByUrlAndIsDeletedFalse(request.getImageUrl())
                    .orElseThrow(() -> new ResourceNotFoundException("이미지를 찾을 수 없습니다"));
            product.updateImage(image);
        }
        if (request.getUrl() != null) {
            product.updateUrl(request.getUrl());
        }
        if (request.getCategory() != null) {
            product.updateCategory(request.getCategory());
        }
        if (request.getTags() != null) {
            product.updateTags(jsonConverter.toJson(request.getTags()));
        }

        // 피부코드 업데이트
        if (request.getSkinCodes() != null) {
            // 기존 피부코드 삭제
            product.getProductSkinCodes().forEach(ProductSkinCode::softDelete);
            // 새 피부코드 연결
            for (String skinCode : request.getSkinCodes()) {
                ProductSkinCode productSkinCode = ProductSkinCode.builder()
                        .product(product)
                        .skinCode(skinCode)
                        .build();
                product.getProductSkinCodes().add(productSkinCode);
            }
        }

        // 퍼스널컬러 업데이트
        if (request.getPersonalColors() != null) {
            // 기존 퍼스널컬러 삭제
            product.getProductPersonalColors().forEach(ProductPersonalColor::softDelete);
            // 새 퍼스널컬러 연결
            for (String personalColor : request.getPersonalColors()) {
                ProductPersonalColor productPersonalColor = ProductPersonalColor.builder()
                        .product(product)
                        .personalColor(personalColor)
                        .build();
                product.getProductPersonalColors().add(productPersonalColor);
            }
        }

        return ProductResponse.from(product, false, jsonConverter);
    }

    @Transactional
    public void deleteProductForAdmin(Long productId) {
        Product product = findById(productId);
        
        // 관련 찜 목록에서도 제거 (소프트 삭제)
        product.getProductWishes().forEach(ProductWish::softDelete);
        
        product.softDelete();
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

