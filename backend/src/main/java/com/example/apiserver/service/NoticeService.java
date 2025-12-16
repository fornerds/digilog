package com.example.apiserver.service;

import com.example.apiserver.dto.notice.NoticeRequest;
import com.example.apiserver.dto.notice.NoticeResponse;
import com.example.apiserver.entity.Image;
import com.example.apiserver.entity.Notice;
import com.example.apiserver.entity.NoticeImage;
import com.example.apiserver.repository.BaseRepository;
import com.example.apiserver.repository.ImageRepository;
import com.example.apiserver.repository.NoticeRepository;
import com.example.apiserver.service.sort.NoticeSortStrategyFactory;
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
public class NoticeService extends BaseService<Notice, Long> {

    private final NoticeRepository noticeRepository;
    private final ImageRepository imageRepository;
    private final NoticeSortStrategyFactory sortStrategyFactory;
    private final JsonConverter jsonConverter;

    @Override
    protected BaseRepository<Notice, Long> getRepository() {
        return noticeRepository;
    }

    public Page<NoticeResponse> getNotices(int page, int limit, String type, String search, 
                                            String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Notice> notices = sortStrategyFactory.sort(sortBy != null ? sortBy : "createdAt", type, search, pageable);

        return notices.map(notice -> {
            List<String> imageUrls = getNoticeImageUrls(notice.getId());
            return NoticeResponse.from(notice, imageUrls);
        });
    }

    public NoticeResponse getNotice(Long noticeId) {
        Notice notice = findById(noticeId);
        List<String> imageUrls = getNoticeImageUrls(noticeId);
        return NoticeResponse.from(notice, imageUrls);
    }

    @Transactional
    public NoticeResponse createNotice(NoticeRequest.Create request) {
        String linksJson = convertLinksToJson(request.getLinks());

        Notice notice = Notice.builder()
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .links(linksJson)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Notice savedNotice = noticeRepository.save(notice);

        // 이미지 연결
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    NoticeImage noticeImage = NoticeImage.builder()
                            .notice(savedNotice)
                            .image(image)
                            .build();
                    savedNotice.getNoticeImages().add(noticeImage);
                }
            }
        }

        return NoticeResponse.from(savedNotice, getNoticeImageUrls(savedNotice.getId()));
    }

    @Transactional
    public NoticeResponse updateNotice(Long noticeId, NoticeRequest.Update request) {
        Notice notice = findById(noticeId);

        if (request.getType() != null) {
            notice.updateType(request.getType());
        }
        if (request.getTitle() != null) {
            notice.updateTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            notice.updateContent(request.getContent());
        }
        if (request.getLinks() != null) {
            notice.updateLinks(convertLinksToJson(request.getLinks()));
        }
        if (request.getStartDate() != null) {
            notice.updateStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            notice.updateEndDate(request.getEndDate());
        }

        // 이미지 업데이트
        if (request.getImageUrls() != null) {
            // 기존 이미지 삭제
            notice.getNoticeImages().clear();

            // 새 이미지 연결
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    NoticeImage noticeImage = NoticeImage.builder()
                            .notice(notice)
                            .image(image)
                            .build();
                    notice.getNoticeImages().add(noticeImage);
                }
            }
        }

        return NoticeResponse.from(notice, getNoticeImageUrls(noticeId));
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = findById(noticeId);
        notice.softDelete();
        noticeRepository.save(notice); // soft delete 후 저장
    }

    private List<String> getNoticeImageUrls(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElse(null);
        if (notice == null) {
            return List.of();
        }
        return notice.getNoticeImages().stream()
                .filter(ni -> !ni.isDeleted())
                .map(ni -> ni.getImage().getUrl())
                .collect(Collectors.toList());
    }

    private String convertLinksToJson(List<String> links) {
        if (links == null || links.isEmpty()) {
            return null;
        }
        try {
            return jsonConverter.toJson(links);
        } catch (Exception e) {
            return "[]";
        }
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

