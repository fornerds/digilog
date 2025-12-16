-- 모든 테이블 생성
-- JPA가 자동으로 생성하는 스키마와 일치하도록 작성
-- 한글 지원을 위해 utf8mb4 문자셋 사용
-- (문자셋 설정은 00-set-charset.sql에서 이미 처리됨)

-- 1. images 테이블 (다른 테이블에서 참조하므로 먼저 생성)
CREATE TABLE IF NOT EXISTS images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    url VARCHAR(500) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. users 테이블
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    name VARCHAR(50) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    profile_image_url VARCHAR(500) DEFAULT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    birth_date DATE DEFAULT NULL,
    gender ENUM('FEMALE', 'MALE', 'OTHER') DEFAULT NULL,
    phone VARCHAR(20) DEFAULT NULL,
    provider ENUM('KAKAO', 'LOCAL', 'NAVER') NOT NULL,
    provider_id VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_email (email),
    UNIQUE KEY UK_provider_provider_id (provider, provider_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2-1. refresh_tokens 테이블
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    token VARCHAR(500) NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_refresh_tokens_token (token),
    KEY idx_refresh_tokens_user_id (user_id),
    KEY idx_refresh_tokens_token (token),
    CONSTRAINT FK_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. posts 테이블
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    hashtags JSON DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_posts_author (author_id),
    CONSTRAINT FK_posts_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. comments 테이블
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    content TEXT NOT NULL,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY FK_comments_post (post_id),
    KEY FK_comments_user (user_id),
    CONSTRAINT FK_comments_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT FK_comments_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. post_likes 테이블
CREATE TABLE IF NOT EXISTS post_likes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_post_likes_post_user (post_id, user_id),
    KEY FK_post_likes_post (post_id),
    KEY FK_post_likes_user (user_id),
    CONSTRAINT FK_post_likes_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT FK_post_likes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. notices 테이블
CREATE TABLE IF NOT EXISTS notices (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    links JSON DEFAULT NULL,
    start_date DATETIME(6) DEFAULT NULL,
    end_date DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. banners 테이블
CREATE TABLE IF NOT EXISTS banners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT NULL,
    image_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY FK_banners_image (image_id),
    CONSTRAINT FK_banners_image FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. skin_analysis_reports 테이블
CREATE TABLE IF NOT EXISTS skin_analysis_reports (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    user_id BIGINT NOT NULL,
    user_age INT NOT NULL,
    skin_age INT NOT NULL,
    skin_condition VARCHAR(50) NOT NULL,
    skin_condition_description TEXT NOT NULL,
    skin_code VARCHAR(10) NOT NULL,
    skin_type VARCHAR(50) NOT NULL,
    score_pores INT NOT NULL,
    score_blackheads INT NOT NULL,
    score_pigmentation INT NOT NULL,
    score_wrinkles INT NOT NULL,
    score_porphyrin INT NOT NULL,
    score_sensitivity INT NOT NULL,
    score_dark_circles INT NOT NULL,
    skin_tags JSON DEFAULT NULL,
    skin_code_description TEXT NOT NULL,
    care_tips JSON NOT NULL,
    PRIMARY KEY (id),
    KEY FK_skin_reports_user (user_id),
    CONSTRAINT FK_skin_reports_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. personal_color_colors 테이블
CREATE TABLE IF NOT EXISTS personal_color_colors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    name VARCHAR(100) NOT NULL,
    hex_code VARCHAR(7) NOT NULL,
    category VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. personal_color_diagnoses 테이블
CREATE TABLE IF NOT EXISTS personal_color_diagnoses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    user_id BIGINT NOT NULL,
    personal_color VARCHAR(20) NOT NULL,
    type_percentage INT NOT NULL,
    type_descriptions JSON NOT NULL,
    lab_l DECIMAL(5,2) NOT NULL,
    lab_a DECIMAL(5,2) NOT NULL,
    lab_b DECIMAL(5,2) NOT NULL,
    matching_colors_title VARCHAR(255) NOT NULL,
    matching_colors_description TEXT NOT NULL,
    non_matching_colors_title VARCHAR(255) NOT NULL,
    non_matching_colors_description TEXT NOT NULL,
    contouring_guide_image_id BIGINT DEFAULT NULL,
    makeup_tips JSON NOT NULL,
    PRIMARY KEY (id),
    KEY FK_pc_diagnoses_user (user_id),
    KEY FK_pc_diagnoses_image (contouring_guide_image_id),
    CONSTRAINT FK_pc_diagnoses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_pc_diagnoses_image FOREIGN KEY (contouring_guide_image_id) REFERENCES images (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 11. personal_color_diagnosis_colors 테이블
CREATE TABLE IF NOT EXISTS personal_color_diagnosis_colors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    diagnosis_id BIGINT NOT NULL,
    color_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    KEY FK_pc_diag_colors_diagnosis (diagnosis_id),
    KEY FK_pc_diag_colors_color (color_id),
    CONSTRAINT FK_pc_diag_colors_diagnosis FOREIGN KEY (diagnosis_id) REFERENCES personal_color_diagnoses (id) ON DELETE CASCADE,
    CONSTRAINT FK_pc_diag_colors_color FOREIGN KEY (color_id) REFERENCES personal_color_colors (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. products 테이블
CREATE TABLE IF NOT EXISTS products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    price BIGINT NOT NULL,
    image_id BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    category VARCHAR(50) DEFAULT NULL,
    tags JSON DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FK_products_image (image_id),
    CONSTRAINT FK_products_image FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 13. product_wishes 테이블
CREATE TABLE IF NOT EXISTS product_wishes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_product_wishes_product_user (product_id, user_id),
    KEY FK_product_wishes_product (product_id),
    KEY FK_product_wishes_user (user_id),
    CONSTRAINT FK_product_wishes_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT FK_product_wishes_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 14. product_skin_codes 테이블
CREATE TABLE IF NOT EXISTS product_skin_codes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    product_id BIGINT NOT NULL,
    skin_code VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_product_skin_codes_product_skin (product_id, skin_code),
    KEY FK_product_skin_codes_product (product_id),
    CONSTRAINT FK_product_skin_codes_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 15. product_personal_colors 테이블
CREATE TABLE IF NOT EXISTS product_personal_colors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    product_id BIGINT NOT NULL,
    personal_color VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_product_personal_colors_product_color (product_id, personal_color),
    KEY FK_product_personal_colors_product (product_id),
    CONSTRAINT FK_product_personal_colors_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 16. profanity_words 테이블
CREATE TABLE IF NOT EXISTS profanity_words (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    word VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_profanity_words_word (word)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 17. post_images 테이블
CREATE TABLE IF NOT EXISTS post_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    post_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY FK_post_images_post (post_id),
    KEY FK_post_images_image (image_id),
    CONSTRAINT FK_post_images_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT FK_post_images_image FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 18. notice_images 테이블
CREATE TABLE IF NOT EXISTS notice_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at DATETIME(6) DEFAULT NULL,
    notice_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    KEY FK_notice_images_notice (notice_id),
    KEY FK_notice_images_image (image_id),
    CONSTRAINT FK_notice_images_notice FOREIGN KEY (notice_id) REFERENCES notices (id) ON DELETE CASCADE,
    CONSTRAINT FK_notice_images_image FOREIGN KEY (image_id) REFERENCES images (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
