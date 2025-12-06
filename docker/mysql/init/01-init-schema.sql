-- users 테이블 생성
-- JPA가 자동으로 생성하는 스키마와 일치하도록 작성
-- 한글 지원을 위해 utf8mb4 문자셋 사용

-- 데이터베이스 문자셋 확인 및 설정
ALTER DATABASE digilog_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE digilog_dev;

-- users 테이블이 없으면 생성
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) DEFAULT NULL,
    updated_at DATETIME(6) DEFAULT NULL,
    email VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(255) DEFAULT NULL,
    profile_image_url VARCHAR(500) DEFAULT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    birth_date DATE DEFAULT NULL,
    gender ENUM('FEMALE', 'MALE', 'OTHER') DEFAULT NULL,
    phone VARCHAR(20) DEFAULT NULL,
    provider ENUM('KAKAO', 'LOCAL', 'NAVER') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY UK_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
