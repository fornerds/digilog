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

-- 더미 게시글 이미지 연결 데이터 삽입
INSERT INTO post_images (post_id, image_id, created_at, deleted_at) VALUES
(1, 1, NOW(), NULL),
(1, 2, NOW(), NULL),
(2, 2, NOW(), NULL),
(3, 1, NOW(), NULL),
(4, 1, NOW(), NULL),
(5, 2, NOW(), NULL);

-- 더미 공지 이미지 연결 데이터 삽입
INSERT INTO notice_images (notice_id, image_id, created_at, deleted_at) VALUES
(1, 3, NOW(), NULL),
(2, 3, NOW(), NULL),
(3, 3, NOW(), NULL);

-- 더미 피부분석 보고서 데이터 삽입
INSERT INTO skin_analysis_reports (user_id, user_age, skin_age, skin_condition, skin_condition_description, skin_code, skin_type, score_pores, score_blackheads, score_pigmentation, score_wrinkles, score_porphyrin, score_sensitivity, score_dark_circles, skin_tags, skin_code_description, care_tips, created_at, updated_at, deleted_at) VALUES
(1, 34, 32, '건성', '건조한 피부 상태입니다.', 'DS', '건성 민감성', 45, 30, 25, 20, 35, 60, 40, '["건조", "민감", "주름"]', 'DS 타입은 건성 민감성 피부로, 수분 공급과 진정 관리가 중요합니다.', '["수분 크림을 매일 사용하세요", "자극이 적은 제품을 선택하세요", "자외선 차단제를 꼭 사용하세요"]', NOW(), NOW(), NULL),
(2, 29, 28, '중성', '균형잡힌 피부 상태입니다.', 'NS', '중성', 50, 45, 40, 30, 25, 35, 30, '["균형", "건강"]', 'NS 타입은 중성 피부로, 기본적인 관리만으로도 건강한 피부를 유지할 수 있습니다.', '["기본 스킨케어 루틴을 유지하세요", "적절한 수분과 유분 밸런스를 유지하세요", "규칙적인 각질 제거를 하세요"]', NOW(), NOW(), NULL),
(3, 32, 35, '지성', '유분이 많은 피부 상태입니다.', 'OR', '지성', 70, 65, 50, 35, 40, 30, 25, '["유분", "모공", "블랙헤드"]', 'OR 타입은 지성 피부로, 유분 조절과 모공 관리가 중요합니다.', '["오일 프리 제품을 사용하세요", "정기적인 클렌징을 하세요", "수분은 충분히 공급하세요"]', NOW(), NOW(), NULL);

-- 더미 퍼스널컬러 색상 데이터 삽입
INSERT INTO personal_color_colors (name, hex_code, category, created_at, updated_at, deleted_at) VALUES
-- 봄웜톤 색상
('코랄 핑크', '#FF6B9D', '봄웜톤', NOW(), NOW(), NULL),
('피치', '#FFCBA4', '봄웜톤', NOW(), NOW(), NULL),
('골드 옐로우', '#FFD700', '봄웜톤', NOW(), NOW(), NULL),
-- 여름쿨톤 색상
('로즈 핑크', '#FFB6C1', '여름쿨톤', NOW(), NOW(), NULL),
('라벤더', '#E6E6FA', '여름쿨톤', NOW(), NOW(), NULL),
('스카이 블루', '#87CEEB', '여름쿨톤', NOW(), NOW(), NULL),
-- 가을웜톤 색상
('터키쉬 오렌지', '#FF8C42', '가을웜톤', NOW(), NOW(), NULL),
('머스타드', '#FFDB58', '가을웜톤', NOW(), NOW(), NULL),
('올리브 그린', '#808000', '가을웜톤', NOW(), NOW(), NULL),
-- 겨울쿨톤 색상
('로얄 블루', '#4169E1', '겨울쿨톤', NOW(), NOW(), NULL),
('에메랄드 그린', '#50C878', '겨울쿨톤', NOW(), NOW(), NULL),
('딥 레드', '#8B0000', '겨울쿨톤', NOW(), NOW(), NULL);

-- 더미 퍼스널컬러 진단 데이터 삽입
INSERT INTO personal_color_diagnoses (user_id, personal_color, type_percentage, type_descriptions, lab_l, lab_a, lab_b, matching_colors_title, matching_colors_description, non_matching_colors_title, non_matching_colors_description, contouring_guide_image_id, makeup_tips, created_at, updated_at, deleted_at) VALUES
(1, '봄웜톤', 85, '["밝고 따뜻한 톤", "황금빛이 도는 피부", "따뜻한 색상이 잘 어울림"]', 65.5, 12.3, 18.7, '어울리는 색상', '봄웜톤에 어울리는 따뜻하고 밝은 색상들입니다.', '피해야 할 색상', '차갑고 어두운 색상은 피하는 것이 좋습니다.', NULL, '["코랄 계열 립스틱 사용", "골드 하이라이터 추천", "따뜻한 톤의 블러셔 사용"]', NOW(), NOW(), NULL),
(2, '여름쿨톤', 80, '["차갑고 부드러운 톤", "핑크빛이 도는 피부", "파스텔 색상이 잘 어울림"]', 70.2, 8.5, 10.2, '어울리는 색상', '여름쿨톤에 어울리는 차갑고 부드러운 색상들입니다.', '피해야 할 색상', '따뜻하고 선명한 색상은 피하는 것이 좋습니다.', NULL, '["로즈 계열 립스틱 사용", "실버 하이라이터 추천", "차가운 톤의 블러셔 사용"]', NOW(), NOW(), NULL),
(3, '가을웜톤', 75, '["따뜻하고 깊은 톤", "황금빛이 도는 피부", "어스톤 색상이 잘 어울림"]', 58.3, 15.8, 22.1, '어울리는 색상', '가을웜톤에 어울리는 따뜻하고 깊은 색상들입니다.', '피해야 할 색상', '밝고 선명한 색상은 피하는 것이 좋습니다.', NULL, '["터키쉬 오렌지 계열 립스틱 사용", "골드 하이라이터 추천", "어스톤 블러셔 사용"]', NOW(), NOW(), NULL);

-- 더미 퍼스널컬러 진단-색상 연결 데이터 삽입
INSERT INTO personal_color_diagnosis_colors (diagnosis_id, color_id, type, created_at, deleted_at) VALUES
-- 봄웜톤 진단 (user_id=1) - 어울리는 색상
(1, 1, 'matching', NOW(), NULL),
(1, 2, 'matching', NOW(), NULL),
(1, 3, 'matching', NOW(), NULL),
-- 봄웜톤 진단 - 안 어울리는 색상
(1, 10, 'nonMatching', NOW(), NULL),
(1, 11, 'nonMatching', NOW(), NULL),
-- 여름쿨톤 진단 (user_id=2) - 어울리는 색상
(2, 4, 'matching', NOW(), NULL),
(2, 5, 'matching', NOW(), NULL),
(2, 6, 'matching', NOW(), NULL),
-- 여름쿨톤 진단 - 안 어울리는 색상
(2, 7, 'nonMatching', NOW(), NULL),
(2, 8, 'nonMatching', NOW(), NULL),
-- 가을웜톤 진단 (user_id=3) - 어울리는 색상
(3, 7, 'matching', NOW(), NULL),
(3, 8, 'matching', NOW(), NULL),
(3, 9, 'matching', NOW(), NULL),
-- 가을웜톤 진단 - 안 어울리는 색상
(3, 4, 'nonMatching', NOW(), NULL),
(3, 5, 'nonMatching', NOW(), NULL);

-- 더미 제품 데이터 삽입
INSERT INTO products (name, brand, price, image_id, url, category, tags, created_at, updated_at, deleted_at) VALUES
('수분 크림', '브랜드A', 35000, 4, 'https://example.com/products/1', '스킨케어', '["수분", "크림", "건성"]', NOW(), NOW(), NULL),
('세럼', '브랜드B', 45000, 4, 'https://example.com/products/2', '스킨케어', '["세럼", "안티에이징"]', NOW(), NOW(), NULL),
('선크림', '브랜드C', 28000, 5, 'https://example.com/products/3', '스킨케어', '["선크림", "자외선차단"]', NOW(), NOW(), NULL),
('립스틱', '브랜드D', 25000, 5, 'https://example.com/products/4', '메이크업', '["립스틱", "립"]', NOW(), NOW(), NULL),
('파운데이션', '브랜드E', 55000, 4, 'https://example.com/products/5', '메이크업', '["파운데이션", "베이스"]', NOW(), NOW(), NULL);

-- 더미 제품-피부코드 연결 데이터 삽입
INSERT INTO product_skin_codes (product_id, skin_code, created_at, deleted_at) VALUES
(1, 'DS', NOW(), NULL),
(1, 'NS', NOW(), NULL),
(2, 'DS', NOW(), NULL),
(2, 'OR', NOW(), NULL),
(3, 'DS', NOW(), NULL),
(3, 'NS', NOW(), NULL),
(3, 'OR', NOW(), NULL),
(4, 'NS', NOW(), NULL),
(4, 'OR', NOW(), NULL),
(5, 'DS', NOW(), NULL),
(5, 'NS', NOW(), NULL);

-- 더미 제품-퍼스널컬러 연결 데이터 삽입
INSERT INTO product_personal_colors (product_id, personal_color, created_at, deleted_at) VALUES
(1, '봄웜톤', NOW(), NULL),
(1, '가을웜톤', NOW(), NULL),
(2, '봄웜톤', NOW(), NULL),
(2, '여름쿨톤', NOW(), NULL),
(3, '봄웜톤', NOW(), NULL),
(3, '여름쿨톤', NOW(), NULL),
(3, '가을웜톤', NOW(), NULL),
(3, '겨울쿨톤', NOW(), NULL),
(4, '봄웜톤', NOW(), NULL),
(4, '가을웜톤', NOW(), NULL),
(5, '여름쿨톤', NOW(), NULL),
(5, '겨울쿨톤', NOW(), NULL);

-- 더미 제품 찜 데이터 삽입
INSERT INTO product_wishes (product_id, user_id, created_at, deleted_at) VALUES
(1, 1, NOW(), NULL),
(1, 2, NOW(), NULL),
(2, 1, NOW(), NULL),
(3, 2, NOW(), NULL),
(4, 3, NOW(), NULL),
(5, 1, NOW(), NULL),
(5, 3, NOW(), NULL);

-- 완료 메시지
SELECT '샘플 데이터 삽입이 완료되었습니다!' as message;
