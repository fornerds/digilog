package com.example.apiserver.service;

import com.example.apiserver.dto.banner.BannerRequest;
import com.example.apiserver.dto.banner.BannerResponse;
import com.example.apiserver.entity.Banner;
import com.example.apiserver.entity.Image;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.BannerRepository;
import com.example.apiserver.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService extends BaseService<Banner, Long> {

    private final BannerRepository bannerRepository;
    private final ImageRepository imageRepository;

    @Override
    protected BaseRepository<Banner, Long> getRepository() {
        return bannerRepository;
    }

    public List<BannerResponse> getAllBanners() {
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Banner> banners = bannerRepository.findAllActive(pageable);
        return banners.getContent().stream()
                .map(BannerResponse::from)
                .collect(Collectors.toList());
    }

    public BannerResponse getBanner(Long bannerId) {
        Banner banner = findById(bannerId);
        return BannerResponse.from(banner);
    }

    @Transactional
    public BannerResponse createBanner(BannerRequest.Create request) {
        Image image = imageRepository.findByUrlAndIsDeletedFalse(request.getImageUrl())
                .orElseThrow(() -> new ResourceNotFoundException("이미지를 찾을 수 없습니다"));

        Banner banner = Banner.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .image(image)
                .build();

        Banner savedBanner = bannerRepository.save(banner);
        return BannerResponse.from(savedBanner);
    }

    @Transactional
    public BannerResponse updateBanner(Long bannerId, BannerRequest.Update request) {
        Banner banner = findById(bannerId);

        if (request.getTitle() != null) {
            banner.updateTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            banner.updateDescription(request.getDescription());
        }
        if (request.getImageUrl() != null) {
            Image image = imageRepository.findByUrlAndIsDeletedFalse(request.getImageUrl())
                    .orElseThrow(() -> new ResourceNotFoundException("이미지를 찾을 수 없습니다"));
            banner.updateImage(image);
        }

        return BannerResponse.from(banner);
    }

    @Transactional
    public void deleteBanner(Long bannerId) {
        Banner banner = findById(bannerId);
        
        // 배너 소프트 삭제
        banner.softDelete();
        bannerRepository.save(banner); // soft delete 후 저장
        
        // TODO: 이미지 삭제 정책 결정 필요
        // 현재는 즉시 삭제하지 않음 (다른 곳에서 사용할 수 있으므로)
    }

    // 관리자용 메서드들
    public Page<BannerResponse> getBannersForAdmin(int page, int limit, String search, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Banner> banners;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                banners = bannerRepository.findAllWithSearchOrderByUpdatedAt(search, pageable);
                break;
            case "title":
                banners = bannerRepository.findAllWithSearchOrderByTitle(search, pageable);
                break;
            default:
                banners = bannerRepository.findAllWithSearch(search, pageable);
        }
        
        return banners.map(BannerResponse::from);
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

