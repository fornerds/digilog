-- 더미 사용자 데이터 삽입
-- password는 "password123"을 BCrypt로 암호화한 값입니다
-- BCrypt는 매번 다른 salt를 사용하므로 같은 비밀번호라도 해시가 다릅니다 (정상 동작)
-- 실제 생성된 해시: $2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6

USE digilog_dev;

-- 문자셋 설정 (한글 지원)
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 테이블이 존재하는지 확인 후 더미 데이터 삽입
-- 더미 사용자 데이터 삽입
INSERT INTO users (email, password, name, phone, birth_date, gender, role, provider, profile_image_url, created_at, updated_at, deleted_at) VALUES
-- 일반 사용자 (LOCAL)
-- 주의: BCrypt는 매번 다른 해시를 생성하므로, 실제 회원가입으로 생성된 해시를 사용합니다
('user1@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '홍길동', '01012345678', '1990-01-15', 'MALE', 'USER', 'LOCAL', NULL, NOW(), NOW(), NULL),
('user2@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '김영희', '01023456789', '1995-05-20', 'FEMALE', 'USER', 'LOCAL', NULL, NOW(), NOW(), NULL),
('user3@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '이철수', '01034567890', '1992-08-10', 'MALE', 'USER', 'LOCAL', NULL, NOW(), NOW(), NULL),
('user4@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '박민지', '01045678901', '1998-12-25', 'FEMALE', 'USER', 'LOCAL', NULL, NOW(), NOW(), NULL),
('user5@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '최수진', '01056789012', '1993-03-30', 'FEMALE', 'USER', 'LOCAL', NULL, NOW(), NOW(), NULL),

-- 소셜 로그인 사용자 (NAVER) - password는 NULL
('naver1@naver.com', NULL, '네이버사용자1', '01067890123', '1991-06-15', 'MALE', 'USER', 'NAVER', NULL, NOW(), NOW(), NULL),
('naver2@naver.com', NULL, '네이버사용자2', '01078901234', '1996-09-22', 'FEMALE', 'USER', 'NAVER', NULL, NOW(), NOW(), NULL),

-- 소셜 로그인 사용자 (KAKAO) - password는 NULL
('kakao1@kakao.com', NULL, '카카오사용자1', '01089012345', '1994-11-05', 'MALE', 'USER', 'KAKAO', NULL, NOW(), NOW(), NULL),
('kakao2@kakao.com', NULL, '카카오사용자2', '01090123456', '1997-02-18', 'FEMALE', 'USER', 'KAKAO', NULL, NOW(), NOW(), NULL),

-- 관리자 계정
('admin@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '관리자', '01000000000', '1985-01-01', 'MALE', 'ADMIN', 'LOCAL', NULL, NOW(), NOW(), NULL),

-- 프로필 이미지가 있는 사용자
('user6@example.com', '$2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6', '이미지테스트', '01011111111', '1990-07-07', 'FEMALE', 'USER', 'LOCAL', 'https://example.com/profile/user6.jpg', NOW(), NOW(), NULL);

-- 참고: 모든 사용자의 비밀번호는 "password123"입니다
-- BCrypt는 매번 다른 salt를 사용하므로 같은 비밀번호라도 해시가 다릅니다 (정상 동작)
-- 실제 생성된 해시: $2a$10$eoX42Af4a1eKEc/fV7PaxuXjW1kMLOkrHm5negDrYxOqHzOjjC8o6

-- 더미 이미지 데이터 삽입
INSERT INTO images (url, created_at, updated_at, deleted_at) VALUES
('https://example.com/images/post1.jpg', NOW(), NOW(), NULL),
('https://example.com/images/post2.jpg', NOW(), NOW(), NULL),
('https://example.com/images/banner1.jpg', NOW(), NOW(), NULL),
('https://example.com/images/product1.jpg', NOW(), NOW(), NULL),
('https://example.com/images/product2.jpg', NOW(), NOW(), NULL);

-- 더미 게시글 데이터 삽입
INSERT INTO posts (title, content, author_id, hashtags, created_at, updated_at, deleted_at) VALUES
('뷰티 제품 추천', '이 제품 정말 좋아요! 추천합니다.', 1, '["뷰티", "추천", "리뷰"]', NOW(), NOW(), NULL),
('메이크업 팁', '데일리 메이크업 꿀팁 공유합니다.', 2, '["메이크업", "팁"]', NOW(), NOW(), NULL),
('스킨케어 루틴', '제 스킨케어 루틴을 공유합니다.', 3, '["스킨케어", "루틴"]', NOW(), NOW(), NULL),
('새로운 제품 후기', '최근 구매한 제품 후기입니다.', 1, '["후기", "제품"]', NOW(), NOW(), NULL),
('피부 관리 노하우', '피부 관리에 도움이 되는 팁입니다.', 2, '["피부관리", "노하우"]', NOW(), NOW(), NULL);

-- 더미 댓글 데이터 삽입
INSERT INTO comments (post_id, user_id, content, created_at, updated_at, deleted_at) VALUES
(1, 2, '정말 좋은 정보네요!', NOW(), NOW(), NULL),
(1, 3, '저도 한번 써봐야겠어요.', NOW(), NOW(), NULL),
(2, 1, '도움이 많이 되었습니다.', NOW(), NOW(), NULL),
(2, 3, '감사합니다!', NOW(), NOW(), NULL),
(3, 1, '좋은 팁이네요.', NOW(), NOW(), NULL);

-- 더미 게시글 좋아요 데이터 삽입
INSERT INTO post_likes (post_id, user_id, created_at, deleted_at) VALUES
(1, 2, NOW(), NULL),
(1, 3, NOW(), NULL),
(2, 1, NOW(), NULL),
(2, 3, NOW(), NULL),
(3, 1, NOW(), NULL),
(3, 2, NOW(), NULL);

-- 더미 공지/이벤트 데이터 삽입
INSERT INTO notices (type, title, content, links, start_date, end_date, created_at, updated_at, deleted_at) VALUES
('notice', '서비스 이용 안내', '서비스 이용 시 주의사항을 안내드립니다.', '["https://example.com/notice1"]', NULL, NULL, NOW(), NOW(), NULL),
('event', '신규 회원 이벤트', '신규 회원을 위한 특별 이벤트입니다!', '["https://example.com/event1"]', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW(), NOW(), NULL),
('notice', '개인정보 처리방침 변경', '개인정보 처리방침이 변경되었습니다.', NULL, NULL, NULL, NOW(), NOW(), NULL);

-- 더미 배너 데이터 삽입
INSERT INTO banners (title, description, image_id, created_at, updated_at, deleted_at) VALUES
('신규 제품 출시', '새로운 뷰티 제품이 출시되었습니다!', 3, NOW(), NOW(), NULL),
('특별 할인 이벤트', '한정 기간 특별 할인 이벤트 진행 중', 3, NOW(), NOW(), NULL);

-- 더미 비속어 단어 데이터 삽입
INSERT INTO profanity_words (word, created_at, updated_at, deleted_at) VALUES
('비속어1', NOW(), NOW(), NULL),
('비속어2', NOW(), NOW(), NULL),
('비속어3', NOW(), NOW(), NULL);

-- 완료 메시지
SELECT '샘플 데이터 삽입이 완료되었습니다!' as message;
