package com.example.apiserver.service;

import com.example.apiserver.dto.post.PostRequest;
import com.example.apiserver.dto.post.PostResponse;
import com.example.apiserver.entity.Comment;
import com.example.apiserver.entity.Image;
import com.example.apiserver.entity.Post;
import com.example.apiserver.entity.PostImage;
import com.example.apiserver.entity.User;
import com.example.apiserver.exception.BadRequestException;
import com.example.apiserver.exception.ForbiddenException;
import com.example.apiserver.exception.ResourceNotFoundException;
import com.example.apiserver.repository.*;
import com.example.apiserver.service.sort.PostSortStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService extends BaseService<Post, Long> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final ProfanityService profanityService;
    private final S3Service s3Service;
    private final PostSortStrategyFactory sortStrategyFactory;
    private final JsonConverter jsonConverter;

    @Override
    protected BaseRepository<Post, Long> getRepository() {
        return postRepository;
    }

    public Page<PostResponse> getPosts(int page, int limit, String search, String sortBy, String order) {
        // 인기순 정렬은 별도 처리 (좋아요 수 + 댓글 수 계산 필요)
        if ("popularity".equals(sortBy)) {
            return getPostsSortedByPopularity(page, limit, search);
        }
        
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Post> posts = sortStrategyFactory.sort(sortBy != null ? sortBy : "createdAt", search, pageable);

        return posts.map(post -> {
            User author = post.getAuthor();
            List<String> imageUrls = getPostImageUrls(post.getId());
            Long commentCount = commentRepository.countByPostId(post.getId());
            Long likeCount = postLikeRepository.countByPostId(post.getId());
            return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
        });
    }

    private Page<PostResponse> getPostsSortedByPopularity(int page, int limit, String search) {
        // 인기순: 좋아요 수 + 댓글 수 기준
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Post> posts = postRepository.findAllWithSearch(search, 
                PageRequest.of(0, 10000)); // 모든 게시글 가져와서 정렬
        
        // 인기 점수 계산 및 정렬
        List<PostResponse> sortedPosts = posts.getContent().stream()
                .map(post -> {
                    User author = post.getAuthor();
                    List<String> imageUrls = getPostImageUrls(post.getId());
                    Long commentCount = commentRepository.countByPostId(post.getId());
                    Long likeCount = postLikeRepository.countByPostId(post.getId());
                    return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
                })
                .sorted((p1, p2) -> {
                    // 인기 점수 = 좋아요 수 + 댓글 수
                    long score1 = (p1.getLikeCount() != null ? p1.getLikeCount() : 0) + 
                                 (p1.getCommentCount() != null ? p1.getCommentCount() : 0);
                    long score2 = (p2.getLikeCount() != null ? p2.getLikeCount() : 0) + 
                                 (p2.getCommentCount() != null ? p2.getCommentCount() : 0);
                    return Long.compare(score2, score1); // 내림차순
                })
                .skip((page - 1) * limit)
                .limit(limit)
                .collect(Collectors.toList());
        
        // Page 객체 생성
        return new org.springframework.data.domain.PageImpl<>(
                sortedPosts,
                pageable,
                posts.getTotalElements()
        );
    }

    public PostResponse getPost(Long postId) {
        Post post = findById(postId);
        User author = post.getAuthor();
        List<String> imageUrls = getPostImageUrls(postId);
        Long commentCount = commentRepository.countByPostId(postId);
        Long likeCount = postLikeRepository.countByPostId(postId);
        return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
    }

    @Transactional
    public List<String> uploadPostImages(Long userId, List<MultipartFile> images) {
        List<String> imageUrls = new ArrayList<>();
        
        for (MultipartFile image : images) {
            if (image.isEmpty()) {
                continue;
            }
            
            // S3 업로드
            String imageUrl = s3Service.uploadFile(image, "posts/" + userId);
            imageUrls.add(imageUrl);
            
            // Image 엔티티에 저장
            Image imageEntity = Image.builder()
                    .url(imageUrl)
                    .build();
            imageRepository.save(imageEntity);
        }
        
        return imageUrls;
    }

    @Transactional
    public PostResponse createPost(Long authorId, PostRequest.Create request) {
        User author = userRepository.findByIdAndIsDeletedFalse(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다"));

        // 비속어 검사
        if (!Boolean.TRUE.equals(request.getForcePublish())) {
            List<String> detectedWords = profanityService.detectProfanityInTexts(
                    request.getTitle(),
                    request.getContent(),
                    request.getHashtags() != null ? String.join(" ", request.getHashtags()) : ""
            );

            if (!detectedWords.isEmpty()) {
                throw new BadRequestException("비속어가 감지되었습니다.", detectedWords);
            }
        }

        String hashtagsJson = convertHashtagsToJson(request.getHashtags());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .hashtags(hashtagsJson)
                .build();

        Post savedPost = postRepository.save(post);

        // 이미지 연결
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    PostImage postImage = PostImage.builder()
                            .post(savedPost)
                            .image(image)
                            .build();
                    savedPost.getPostImages().add(postImage);
                }
            }
        }

        return PostResponse.from(savedPost, author, getPostImageUrls(savedPost.getId()), 0L, 0L);
    }

    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostRequest.Update request) {
        Post post = findById(postId);

        // 작성자 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("게시글을 수정할 권한이 없습니다");
        }

        // 비속어 검사
        if (!Boolean.TRUE.equals(request.getForcePublish())) {
            String title = request.getTitle() != null ? request.getTitle() : post.getTitle();
            String content = request.getContent() != null ? request.getContent() : post.getContent();
            List<String> hashtags = request.getHashtags() != null ? request.getHashtags() : 
                    parseHashtags(post.getHashtags());

            List<String> detectedWords = profanityService.detectProfanityInTexts(
                    title,
                    content,
                    String.join(" ", hashtags)
            );

            if (!detectedWords.isEmpty()) {
                throw new BadRequestException("비속어가 감지되었습니다.", detectedWords);
            }
        }

        if (request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            post.updateContent(request.getContent());
        }
        if (request.getHashtags() != null) {
            post.updateHashtags(convertHashtagsToJson(request.getHashtags()));
        }

        // 이미지 업데이트
        if (request.getImageUrls() != null) {
            // 기존 이미지 URL 수집 (삭제용)
            List<String> oldImageUrls = post.getPostImages().stream()
                    .filter(pi -> !pi.isDeleted())
                    .map(pi -> pi.getImage().getUrl())
                    .collect(Collectors.toList());

            // 기존 이미지 관계 삭제
            post.getPostImages().clear();

            // 새 이미지 연결
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    PostImage postImage = PostImage.builder()
                            .post(post)
                            .image(image)
                            .build();
                    post.getPostImages().add(postImage);
                }
            }

            // 기존 이미지 중 새 이미지에 없는 것 삭제
            List<String> newImageUrls = request.getImageUrls();
            List<String> imagesToDelete = oldImageUrls.stream()
                    .filter(url -> !newImageUrls.contains(url))
                    .collect(Collectors.toList());
            
            // TODO: 이미지 삭제 정책 결정 필요
            // 현재는 IMMEDIATE_DELETE 정책 사용 (즉시 삭제)
            // DELAYED_DELETE 정책으로 변경하려면 아래 주석 해제하고 위 코드 주석 처리
            deletePostImages(ImageDeletionPolicy.IMMEDIATE_DELETE, imagesToDelete);
            // deletePostImages(ImageDeletionPolicy.DELAYED_DELETE, imagesToDelete);
        }

        User author = post.getAuthor();
        List<String> imageUrls = getPostImageUrls(postId);
        Long commentCount = commentRepository.countByPostId(postId);
        Long likeCount = postLikeRepository.countByPostId(postId);
        return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findById(postId);

        // 작성자 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("게시글을 삭제할 권한이 없습니다");
        }

        // 게시글 이미지 URL 수집
        List<String> imageUrls = getPostImageUrls(postId);

        // 게시글 소프트 삭제
        post.softDelete();
        postRepository.save(post); // soft delete 후 저장

        // TODO: 이미지 삭제 정책 결정 필요
        // 현재는 IMMEDIATE_DELETE 정책 사용 (즉시 삭제)
        // DELAYED_DELETE 정책으로 변경하려면 아래 주석 해제하고 위 코드 주석 처리
        deletePostImages(ImageDeletionPolicy.IMMEDIATE_DELETE, imageUrls);
        // deletePostImages(ImageDeletionPolicy.DELAYED_DELETE, imageUrls);
    }

    // 관리자용 메서드들
    public Page<PostResponse> getPostsForAdmin(int page, int limit, String search, Long authorId, String sortBy, String order) {
        Pageable pageable = createPageable(page, limit, sortBy, order);
        Page<Post> posts;
        
        switch (sortBy != null ? sortBy : "createdAt") {
            case "updatedAt":
                posts = postRepository.findAllWithFiltersOrderByUpdatedAt(search, authorId, pageable);
                break;
            case "title":
                posts = postRepository.findAllWithFiltersOrderByTitle(search, authorId, pageable);
                break;
            default:
                posts = postRepository.findAllWithFilters(search, authorId, pageable);
        }
        
        return posts.map(post -> {
            User author = post.getAuthor();
            List<String> imageUrls = getPostImageUrls(post.getId());
            Long commentCount = commentRepository.countByPostId(post.getId());
            Long likeCount = postLikeRepository.countByPostId(post.getId());
            return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
        });
    }

    @Transactional
    public PostResponse createPostForAdmin(PostRequest.CreateAdmin request) {
        User author = userRepository.findByIdAndIsDeletedFalse(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("작성자를 찾을 수 없습니다"));

        // 관리자가 생성하는 게시글은 비속어 검사를 건너뜁니다
        String hashtagsJson = convertHashtagsToJson(request.getHashtags());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .hashtags(hashtagsJson)
                .build();

        Post savedPost = postRepository.save(post);

        // 이미지 연결
        List<String> imageUrls = new ArrayList<>();
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    PostImage postImage = PostImage.builder()
                            .post(savedPost)
                            .image(image)
                            .build();
                    postImageRepository.save(postImage);
                    savedPost.getPostImages().add(postImage);
                    imageUrls.add(image.getUrl());
                }
            }
        }

        return PostResponse.from(savedPost, author, imageUrls, 0L, 0L);
    }

    @Transactional
    public PostResponse updatePostForAdmin(Long postId, PostRequest.UpdateAdmin request) {
        Post post = findById(postId);

        if (request.getTitle() != null) {
            post.updateTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            post.updateContent(request.getContent());
        }
        if (request.getHashtags() != null) {
            post.updateHashtags(convertHashtagsToJson(request.getHashtags()));
        }

        // 이미지 업데이트
        if (request.getImageUrls() != null) {
            // 기존 이미지 URL 수집 (삭제용)
            List<String> oldImageUrls = post.getPostImages().stream()
                    .filter(pi -> !pi.isDeleted())
                    .map(pi -> pi.getImage().getUrl())
                    .collect(Collectors.toList());

            // 기존 이미지 관계 삭제
            post.getPostImages().clear();

            // 새 이미지 연결
            for (String imageUrl : request.getImageUrls()) {
                Image image = imageRepository.findByUrlAndIsDeletedFalse(imageUrl)
                        .orElse(null);
                if (image != null) {
                    PostImage postImage = PostImage.builder()
                            .post(post)
                            .image(image)
                            .build();
                    post.getPostImages().add(postImage);
                }
            }

            // 기존 이미지 중 새 이미지에 없는 것 삭제
            List<String> newImageUrls = request.getImageUrls();
            List<String> imagesToDelete = oldImageUrls.stream()
                    .filter(url -> !newImageUrls.contains(url))
                    .collect(Collectors.toList());
            
            deletePostImages(ImageDeletionPolicy.IMMEDIATE_DELETE, imagesToDelete);
        }

        User author = post.getAuthor();
        List<String> imageUrls = getPostImageUrls(postId);
        Long commentCount = commentRepository.countByPostId(postId);
        Long likeCount = postLikeRepository.countByPostId(postId);
        return PostResponse.from(post, author, imageUrls, commentCount, likeCount);
    }

    @Transactional
    public void deletePostForAdmin(Long postId) {
        Post post = findById(postId);
        
        // 게시글 이미지 URL 수집
        List<String> imageUrls = getPostImageUrls(postId);
        
        // 게시글 소프트 삭제
        post.softDelete();
        postRepository.save(post); // soft delete 후 저장
        
        // 관련 댓글도 함께 삭제
        List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalse(postId);
        for (Comment comment : comments) {
            comment.softDelete();
            commentRepository.save(comment);
        }
        
        // 이미지 삭제
        deletePostImages(ImageDeletionPolicy.IMMEDIATE_DELETE, imageUrls);
    }

    /**
     * 게시글 이미지 삭제 처리
     * 
     * TODO: 이미지 삭제 정책 결정 필요
     * - IMMEDIATE_DELETE: S3에서 즉시 삭제
     * - DELAYED_DELETE: DB에서만 삭제 표시, S3는 보관 (나중에 정리)
     */
    private void deletePostImages(ImageDeletionPolicy policy, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        switch (policy) {
            case IMMEDIATE_DELETE:
                // 즉시 S3에서 삭제
                s3Service.deleteFiles(imageUrls);
                // Image 엔티티도 소프트 삭제
                for (String url : imageUrls) {
                    imageRepository.findByUrlAndIsDeletedFalse(url)
                            .ifPresent(Image::softDelete);
                }
                break;
            case DELAYED_DELETE:
                // DB에서만 삭제 표시, S3는 보관
                // 나중에 주기적으로 사용하지 않는 이미지 정리 필요
                for (String url : imageUrls) {
                    imageRepository.findByUrlAndIsDeletedFalse(url)
                            .ifPresent(Image::softDelete);
                }
                // TODO: 스케줄러로 주기적으로 사용하지 않는 이미지 정리 구현 필요
                break;
        }
    }

    public List<String> checkProfanity(PostRequest.CheckProfanity request) {
        List<String> detectedWords = profanityService.detectProfanityInTexts(
                request.getTitle(),
                request.getContent(),
                request.getHashtags() != null ? String.join(" ", request.getHashtags()) : ""
        );
        return detectedWords;
    }

    private List<String> getPostImageUrls(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return List.of();
        }
        return post.getPostImages().stream()
                .filter(pi -> !pi.isDeleted())
                .map(pi -> pi.getImage().getUrl())
                .collect(Collectors.toList());
    }

    private String convertHashtagsToJson(List<String> hashtags) {
        if (hashtags == null || hashtags.isEmpty()) {
            return null;
        }
        try {
            return jsonConverter.toJson(hashtags);
        } catch (Exception e) {
            return "[]";
        }
    }

    private List<String> parseHashtags(String hashtagsJson) {
        if (hashtagsJson == null || hashtagsJson.isBlank()) {
            return List.of();
        }
        try {
            return jsonConverter.fromJsonArray(hashtagsJson, String.class);
        } catch (Exception e) {
            return List.of();
        }
    }

    private Pageable createPageable(int page, int limit, String sortBy, String order) {
        Sort sort = Sort.by("desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "createdAt");
        return PageRequest.of(page - 1, limit, sort);
    }
}

