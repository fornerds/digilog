# 데이터베이스 설계

## 개요
- **데이터베이스**: PostgreSQL (또는 MySQL)
- **마이그레이션 도구**: (예: Prisma, TypeORM, Sequelize 등)
- **설계 기준**: API 명세서 기반 설계

---

## ERD (Entity Relationship Diagram)

> **참고**: 테이블 간 관계는 외래키로 정의되어 있으며, 상세한 관계는 각 테이블 스키마의 외래키 섹션을 참조하세요.

```
┌─────────────┐
│    Users    │
├─────────────┤
│ id (PK)     │
│ email       │
│ password    │
│ name        │
│ phone       │
│ birthDate   │
│ gender      │
│ skinType    │
│ profileImageId(FK)│
│ provider    │
│ role        │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│    Posts    │
├─────────────┤
│ id (PK)     │
│ title       │
│ content     │
│ authorId(FK)│
│ hashtags    │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│  Comments   │
├─────────────┤
│ id (PK)     │
│ postId (FK) │
│ userId (FK) │
│ content     │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│ Post_Likes  │
├─────────────┤
│ id (PK)     │
│ postId (FK) │
│ userId (FK) │
│ isDeleted   │
│ deletedAt   │
│ createdAt   │
└─────────────┘

┌─────────────┐
│   Notices   │
├─────────────┤
│ id (PK)     │
│ type        │
│ title       │
│ content     │
│ links       │
│ startDate   │
│ endDate     │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│   Banners   │
├─────────────┤
│ id (PK)     │
│ title       │
│ description │
│ imageId (FK)│
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│ Skin_Reports│
├─────────────┤
│ id (PK)     │
│ userId (FK) │
│ userAge     │
│ skinAge     │
│ skinCondition│
│ skinCode    │
│ skinType    │
│ scores      │
│ skinTags    │
│ description │
│ careTips    │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│PC_Colors    │
├─────────────┤
│ id (PK)     │
│ name        │
│ hexCode     │
│ category    │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│PC_Diagnoses │
├─────────────┤
│ id (PK)     │
│ userId (FK) │
│ personalColor│
│ typePercentage│
│ typeDescriptions│
│ labValues   │
│ matchingColors│
│ nonMatchingColors│
│ contouringImageId(FK)│
│ makeupTips  │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────────────┐
│PC_Diagnosis_Colors   │
├─────────────────────┤
│ id (PK)             │
│ diagnosisId (FK)    │
│ colorId (FK)        │
│ type (matching/non) │
│ isDeleted           │
│ createdAt           │
└─────────────────────┘

┌─────────────┐
│  Products   │
├─────────────┤
│ id (PK)     │
│ name        │
│ brand       │
│ price       │
│ imageId (FK)│
│ url         │
│ skinCode    │
│ personalColor│
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│Product_Wishes│
├─────────────┤
│ id (PK)     │
│ productId(FK)│
│ userId (FK) │
│ isDeleted   │
│ createdAt   │
└─────────────┘

┌─────────────┐
│Profanity_Words│
├─────────────┤
│ id (PK)     │
│ word        │
│ isDeleted   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│   Images    │
├─────────────┤
│ id (PK)     │
│ url         │
│ isDeleted   │
│ deletedAt   │
│ createdAt   │
│ updatedAt   │
└─────────────┘

┌─────────────┐
│ Post_Images │
├─────────────┤
│ id (PK)     │
│ postId (FK) │
│ imageId (FK)│
│ isDeleted   │
│ deletedAt   │
│ createdAt   │
└─────────────┘

┌─────────────┐
│Notice_Images│
├─────────────┤
│ id (PK)     │
│ noticeId(FK)│
│ imageId (FK)│
│ isDeleted   │
│ deletedAt   │
│ createdAt   │
└─────────────┘
```

---

## 테이블 스키마

### 1. users (사용자)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 사용자 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| password | VARCHAR(255) | NULL | 비밀번호 (해시, 소셜 로그인 사용자는 NULL) |
| name | VARCHAR(100) | NOT NULL | 이름 |
| phone | VARCHAR(20) | UNIQUE, NOT NULL | 휴대폰 번호 |
| birthDate | DATE | NOT NULL | 생년월일 |
| gender | VARCHAR(10) | NOT NULL | 성별 (male, female, other) |
| skinType | VARCHAR(50) | NULL | 피부타입 (최신 피부분석 보고서의 skinType) |
| profileImageId | BIGINT | FOREIGN KEY, NULL | 프로필 이미지 ID |
| provider | VARCHAR(20) | NOT NULL, DEFAULT 'local' | 로그인 제공자 (local, naver, kakao) |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'user' | 사용자 권한 (user, admin) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**인덱스**:
- `idx_users_email`: `email` (UNIQUE)
- `idx_users_phone`: `phone` (UNIQUE)
- `idx_users_provider`: `provider` (소셜 로그인 조회 최적화)
- `idx_users_role`: `role` (관리자 조회 최적화)
- `idx_users_skinType`: `skinType` (피부타입별 조회 최적화)
- `idx_users_isDeleted`: `isDeleted` (소프트 삭제 필터링 최적화)

**외래키**:
- `profileImageId` → `images.id` (ON DELETE SET NULL)

---

### 2. posts (게시글)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 게시글 ID |
| title | VARCHAR(255) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| authorId | BIGINT | FOREIGN KEY, NOT NULL | 작성자 ID |
| hashtags | JSONB/TEXT | NULL | 해시태그 배열 (JSON 형식 또는 쉼표 구분 문자열) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `authorId` → `users.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_posts_authorId`: `authorId` (작성자별 조회 최적화)
- `idx_posts_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)
- `idx_posts_title_content`: `title`, `content` (검색 기능 최적화 - Full-Text Search 고려)

---

### 3. comments (댓글)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 댓글 ID |
| postId | BIGINT | FOREIGN KEY, NOT NULL | 게시글 ID |
| userId | BIGINT | FOREIGN KEY, NOT NULL | 작성자 ID |
| content | TEXT | NOT NULL | 댓글 내용 |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `postId` → `posts.id` (ON DELETE CASCADE)
- `userId` → `users.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_comments_postId`: `postId` (게시글별 댓글 조회 최적화)
- `idx_comments_userId`: `userId` (사용자별 댓글 조회 최적화)
- `idx_comments_createdAt`: `createdAt` (정렬 최적화)

---

### 4. post_likes (게시글 좋아요)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 좋아요 ID |
| postId | BIGINT | FOREIGN KEY, NOT NULL | 게시글 ID |
| userId | BIGINT | FOREIGN KEY, NOT NULL | 사용자 ID |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| deletedAt | TIMESTAMP | NULL | 삭제일시 |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `postId` → `posts.id` (ON DELETE CASCADE)
- `userId` → `users.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_post_likes_postId`: `postId` (게시글별 좋아요 수 집계 최적화)
- `idx_post_likes_userId`: `userId` (사용자별 좋아요 조회 최적화)
- `idx_post_likes_unique`: `postId`, `userId` (UNIQUE) (중복 좋아요 방지)

---

### 5. notices (공지/이벤트)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 공지/이벤트 ID |
| type | VARCHAR(20) | NOT NULL | 타입 (notice: 공지, event: 이벤트) |
| title | VARCHAR(255) | NOT NULL | 제목 |
| content | TEXT | NOT NULL | 내용 |
| links | JSONB/TEXT | NULL | 링크 URL 배열 (JSON 형식 또는 쉼표 구분 문자열) |
| startDate | TIMESTAMP | NULL | 시작일시 (이벤트용) |
| endDate | TIMESTAMP | NULL | 종료일시 (이벤트용) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**인덱스**:
- `idx_notices_type`: `type` (타입별 조회 최적화)
- `idx_notices_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)
- `idx_notices_title`: `title` (검색 최적화)

---

### 6. banners (배너)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 배너 ID |
| title | VARCHAR(255) | NOT NULL | 배너 제목 |
| description | TEXT | NULL | 배너 설명 |
| imageId | BIGINT | FOREIGN KEY, NOT NULL | 배너 이미지 ID |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `imageId` → `images.id` (ON DELETE SET NULL)

**인덱스**:
- `idx_banners_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)

---

### 7. skin_analysis_reports (피부분석 보고서)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 피부분석 보고서 ID |
| userId | BIGINT | FOREIGN KEY, NOT NULL | 사용자 ID |
| userAge | INTEGER | NOT NULL | 사용자 나이 |
| skinAge | INTEGER | NOT NULL | 피부나이 |
| skinCondition | VARCHAR(50) | NOT NULL | 피부상태 (예: "중성" 등) |
| skinConditionDescription | TEXT | NOT NULL | 피부 상태에 따른 설명 |
| skinCode | VARCHAR(10) | NOT NULL | 피부코드 (예: "DS", "DW" 등) |
| skinType | VARCHAR(50) | NOT NULL | 피부타입 (예: "건성 민감성" 등) |
| scorePores | INTEGER | NOT NULL | 모공 점수 (0-100) |
| scoreBlackheads | INTEGER | NOT NULL | 블랙헤드 점수 (0-100) |
| scorePigmentation | INTEGER | NOT NULL | 색소스팟 점수 (0-100) |
| scoreWrinkles | INTEGER | NOT NULL | 주름 점수 (0-100) |
| scorePorphyrin | INTEGER | NOT NULL | 포르피린 점수 (0-100) |
| scoreSensitivity | INTEGER | NOT NULL | 민감도 점수 (0-100) |
| scoreDarkCircles | INTEGER | NOT NULL | 다크서클 점수 (0-100) |
| skinTags | JSONB/TEXT | NULL | 피부 태그 배열 (JSON 형식 또는 쉼표 구분 문자열) |
| skinCodeDescription | TEXT | NOT NULL | 피부코드에 따른 설명요약 |
| careTips | JSONB/TEXT | NOT NULL | 피부코드에 따른 피부관리 팁 배열 (JSON 형식, 3개) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `userId` → `users.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_skin_reports_userId`: `userId` (사용자별 보고서 조회 최적화)
- `idx_skin_reports_skinCode`: `skinCode` (피부코드별 조회 최적화)
- `idx_skin_reports_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)

---

### 8. personal_color_colors (퍼스널컬러 색상)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 색상 ID |
| name | VARCHAR(100) | NOT NULL | 색상명 |
| hexCode | VARCHAR(7) | NOT NULL | 색상 HEX 코드 (예: "#FF5733") |
| category | VARCHAR(20) | NOT NULL | 색상 카테고리 (봄웜톤, 여름쿨톤, 가을웜톤, 겨울쿨톤) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**인덱스**:
- `idx_pc_colors_category`: `category` (카테고리별 조회 최적화)
- `idx_pc_colors_name`: `name` (색상명 검색 최적화)

---

### 9. personal_color_diagnoses (퍼스널컬러 진단)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 퍼스널컬러 진단 ID |
| userId | BIGINT | FOREIGN KEY, NOT NULL | 사용자 ID |
| personalColor | VARCHAR(20) | NOT NULL | 퍼스널컬러 타입 (봄웜톤, 여름쿨톤, 가을웜톤, 겨울쿨톤) |
| typePercentage | INTEGER | NOT NULL | 해당 타입일 확률 (0-100) |
| typeDescriptions | JSONB/TEXT | NOT NULL | 타입에 대한 설명 배열 (JSON 형식, 4가지) |
| labL | DECIMAL(5,2) | NOT NULL | LAB 색공간 L 값 (명도, 0-100) |
| labA | DECIMAL(5,2) | NOT NULL | LAB 색공간 A 값 (적-녹축, -128 ~ 127) |
| labB | DECIMAL(5,2) | NOT NULL | LAB 색공간 B 값 (황-청축, -128 ~ 127) |
| matchingColorsTitle | VARCHAR(255) | NOT NULL | 어울리는 색상 제목 |
| matchingColorsDescription | TEXT | NOT NULL | 어울리는 색상 설명 |
| nonMatchingColorsTitle | VARCHAR(255) | NOT NULL | 안 어울리는 색상 제목 |
| nonMatchingColorsDescription | TEXT | NOT NULL | 안 어울리는 색상 설명 |
| contouringGuideImageId | BIGINT | FOREIGN KEY, NULL | 컨투어링 가이드 이미지 ID |
| makeupTips | JSONB/TEXT | NOT NULL | 화장팁 배열 (JSON 형식, 3개) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `userId` → `users.id` (ON DELETE CASCADE)
- `contouringGuideImageId` → `images.id` (ON DELETE SET NULL)

**인덱스**:
- `idx_pc_diagnoses_userId`: `userId` (사용자별 진단 조회 최적화)
- `idx_pc_diagnoses_personalColor`: `personalColor` (퍼스널컬러 타입별 조회 최적화)
- `idx_pc_diagnoses_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)

---

### 10. personal_color_diagnosis_colors (퍼스널컬러 진단-색상 관계)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 관계 ID |
| diagnosisId | BIGINT | FOREIGN KEY, NOT NULL | 퍼스널컬러 진단 ID |
| colorId | BIGINT | FOREIGN KEY, NOT NULL | 색상 ID |
| type | VARCHAR(20) | NOT NULL | 타입 (matching: 어울리는 색상, nonMatching: 안 어울리는 색상) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `diagnosisId` → `personal_color_diagnoses.id` (ON DELETE CASCADE)
- `colorId` → `personal_color_colors.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_pc_diag_colors_diagnosisId`: `diagnosisId` (진단별 색상 조회 최적화)
- `idx_pc_diag_colors_colorId`: `colorId` (색상별 진단 조회 최적화)
- `idx_pc_diag_colors_type`: `type` (타입별 조회 최적화)

---

### 11. products (제품)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 제품 ID |
| name | VARCHAR(255) | NOT NULL | 제품명 |
| brand | VARCHAR(100) | NOT NULL | 제품 회사 (브랜드명) |
| price | BIGINT | NOT NULL | 가격 (원) |
| imageId | BIGINT | FOREIGN KEY, NOT NULL | 제품 이미지 ID |
| url | VARCHAR(500) | NOT NULL | 제품 URL (상세 페이지 링크) |
| category | VARCHAR(50) | NULL | 제품 카테고리 (예: "스킨케어", "메이크업", "선케어" 등) |
| tags | JSONB/TEXT | NULL | 제품 태그 배열 (JSON 형식 또는 쉼표 구분 문자열, 예: ["수분", "진정", "미백"]) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**외래키**:
- `imageId` → `images.id` (ON DELETE SET NULL)

**인덱스**:
- `idx_products_category`: `category` (카테고리별 제품 조회 최적화)
- `idx_products_name_brand`: `name`, `brand` (검색 최적화)
- `idx_products_createdAt`: `createdAt` (DESC) (최신순 정렬 최적화)

---

### 12. product_wishes (제품 찜)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 찜 ID |
| productId | BIGINT | FOREIGN KEY, NOT NULL | 제품 ID |
| userId | BIGINT | FOREIGN KEY, NOT NULL | 사용자 ID |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `productId` → `products.id` (ON DELETE CASCADE)
- `userId` → `users.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_product_wishes_productId`: `productId` (제품별 찜 조회 최적화)
- `idx_product_wishes_userId`: `userId` (사용자별 찜 목록 조회 최적화)
- `idx_product_wishes_unique`: `productId`, `userId` (UNIQUE) (중복 찜 방지)

---

### 13. product_skin_codes (제품-피부코드 관계)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 관계 ID |
| productId | BIGINT | FOREIGN KEY, NOT NULL | 제품 ID |
| skinCode | VARCHAR(10) | NOT NULL | 피부코드 (예: "DS", "DW" 등) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `productId` → `products.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_product_skin_codes_productId`: `productId` (제품별 피부코드 조회 최적화)
- `idx_product_skin_codes_skinCode`: `skinCode` (피부코드별 제품 추천 조회 최적화)
- `idx_product_skin_codes_unique`: `productId`, `skinCode` (UNIQUE) (중복 관계 방지)

---

### 14. product_personal_colors (제품-퍼스널컬러 관계)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 관계 ID |
| productId | BIGINT | FOREIGN KEY, NOT NULL | 제품 ID |
| personalColor | VARCHAR(20) | NOT NULL | 퍼스널컬러 (예: "봄웜톤", "여름쿨톤" 등) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `productId` → `products.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_product_personal_colors_productId`: `productId` (제품별 퍼스널컬러 조회 최적화)
- `idx_product_personal_colors_personalColor`: `personalColor` (퍼스널컬러별 제품 추천 조회 최적화)
- `idx_product_personal_colors_unique`: `productId`, `personalColor` (UNIQUE) (중복 관계 방지)

---

### 15. profanity_words (비속어 단어)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 비속어 단어 ID |
| word | VARCHAR(100) | UNIQUE, NOT NULL | 비속어 단어 |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**인덱스**:
- `idx_profanity_words_word`: `word` (UNIQUE) (비속어 검색 최적화)

---

### 16. images (이미지)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 이미지 ID |
| url | VARCHAR(500) | NOT NULL | 이미지 URL (S3 URL) |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| deletedAt | TIMESTAMP | NULL | 삭제일시 |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |
| updatedAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 수정일시 |

**인덱스**:
- `idx_images_url`: `url` (이미지 URL 검색 최적화)
- `idx_images_isDeleted`: `isDeleted` (소프트 삭제 필터링 최적화)

---

### 17. post_images (게시글-이미지 관계)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 관계 ID |
| postId | BIGINT | FOREIGN KEY, NOT NULL | 게시글 ID |
| imageId | BIGINT | FOREIGN KEY, NOT NULL | 이미지 ID |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| deletedAt | TIMESTAMP | NULL | 삭제일시 |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `postId` → `posts.id` (ON DELETE CASCADE)
- `imageId` → `images.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_post_images_postId`: `postId` (게시글별 이미지 조회 최적화)
- `idx_post_images_imageId`: `imageId` (이미지별 게시글 조회 최적화)
- `idx_post_images_order`: `postId`, `createdAt` (게시글 내 이미지 순서 정렬 최적화)

---

### 18. notice_images (공지/이벤트-이미지 관계)

| 컬럼명 | 타입 | 제약조건 | 설명 |
|--------|------|----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 관계 ID |
| noticeId | BIGINT | FOREIGN KEY, NOT NULL | 공지/이벤트 ID |
| imageId | BIGINT | FOREIGN KEY, NOT NULL | 이미지 ID |
| isDeleted | BOOLEAN | NOT NULL, DEFAULT FALSE | 삭제 여부 (소프트 삭제) |
| deletedAt | TIMESTAMP | NULL | 삭제일시 |
| createdAt | TIMESTAMP | NOT NULL, DEFAULT NOW() | 생성일시 |

**외래키**:
- `noticeId` → `notices.id` (ON DELETE CASCADE)
- `imageId` → `images.id` (ON DELETE CASCADE)

**인덱스**:
- `idx_notice_images_noticeId`: `noticeId` (공지/이벤트별 이미지 조회 최적화)
- `idx_notice_images_imageId`: `imageId` (이미지별 공지/이벤트 조회 최적화)
- `idx_notice_images_order`: `noticeId`, `createdAt` (공지/이벤트 내 이미지 순서 정렬 최적화)

---

## SQL DDL (Data Definition Language)

> **참고**: 외래키 참조 관계로 인해 `images` 테이블을 먼저 생성한 후, 다른 테이블들을 생성해야 합니다.

### Images 테이블 생성
```sql
CREATE TABLE images (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    deletedAt TIMESTAMP NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_images_url ON images(url);
CREATE INDEX idx_images_isDeleted ON images(isDeleted);
```

### Users 테이블 생성
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    birthDate DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    skinType VARCHAR(50) NULL,
    profileImageId BIGINT NULL,
    provider VARCHAR(20) NOT NULL DEFAULT 'local',
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_provider ON users(provider);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_skinType ON users(skinType);
CREATE INDEX idx_users_isDeleted ON users(isDeleted);

ALTER TABLE users ADD CONSTRAINT fk_users_profile_image 
    FOREIGN KEY (profileImageId) REFERENCES images(id) ON DELETE SET NULL;
```

### Posts 테이블 생성
```sql
CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    authorId BIGINT NOT NULL,
    hashtags JSONB NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_posts_author FOREIGN KEY (authorId) 
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_authorId ON posts(authorId);
CREATE INDEX idx_posts_createdAt ON posts(createdAt DESC);

-- 검색 기능을 위한 Full-Text Search 인덱스 (PostgreSQL)
CREATE INDEX idx_posts_search ON posts USING gin(to_tsvector('korean', title || ' ' || content));

-- MySQL의 경우
-- ALTER TABLE posts ADD FULLTEXT INDEX idx_posts_search (title, content);
```

### Comments 테이블 생성
```sql
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    postId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    content TEXT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comments_post FOREIGN KEY (postId) 
        REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_user FOREIGN KEY (userId) 
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_comments_postId ON comments(postId);
CREATE INDEX idx_comments_userId ON comments(userId);
CREATE INDEX idx_comments_createdAt ON comments(createdAt);
```

### Post_Likes 테이블 생성
```sql
CREATE TABLE post_likes (
    id BIGSERIAL PRIMARY KEY,
    postId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    deletedAt TIMESTAMP NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_likes_post FOREIGN KEY (postId) 
        REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_likes_user FOREIGN KEY (userId) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_post_likes_post_user UNIQUE (postId, userId)
);

CREATE INDEX idx_post_likes_postId ON post_likes(postId);
CREATE INDEX idx_post_likes_userId ON post_likes(userId);
```

### Notices 테이블 생성
```sql
CREATE TABLE notices (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    links JSONB NULL,
    startDate TIMESTAMP NULL,
    endDate TIMESTAMP NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_notices_type ON notices(type);
CREATE INDEX idx_notices_createdAt ON notices(createdAt DESC);
CREATE INDEX idx_notices_title ON notices(title);
```

### Banners 테이블 생성
```sql
CREATE TABLE banners (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NULL,
    imageId BIGINT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_banners_createdAt ON banners(createdAt DESC);

ALTER TABLE banners ADD CONSTRAINT fk_banners_image 
    FOREIGN KEY (imageId) REFERENCES images(id) ON DELETE SET NULL;
```

### Skin_Analysis_Reports 테이블 생성
```sql
CREATE TABLE skin_analysis_reports (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    userAge INTEGER NOT NULL,
    skinAge INTEGER NOT NULL,
    skinCondition VARCHAR(50) NOT NULL,
    skinConditionDescription TEXT NOT NULL,
    skinCode VARCHAR(10) NOT NULL,
    skinType VARCHAR(50) NOT NULL,
    scorePores INTEGER NOT NULL,
    scoreBlackheads INTEGER NOT NULL,
    scorePigmentation INTEGER NOT NULL,
    scoreWrinkles INTEGER NOT NULL,
    scorePorphyrin INTEGER NOT NULL,
    scoreSensitivity INTEGER NOT NULL,
    scoreDarkCircles INTEGER NOT NULL,
    skinTags JSONB NULL,
    skinCodeDescription TEXT NOT NULL,
    careTips JSONB NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_skin_reports_user FOREIGN KEY (userId) 
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_skin_reports_userId ON skin_analysis_reports(userId);
CREATE INDEX idx_skin_reports_skinCode ON skin_analysis_reports(skinCode);
CREATE INDEX idx_skin_reports_createdAt ON skin_analysis_reports(createdAt DESC);
```

### Personal_Color_Colors 테이블 생성
```sql
CREATE TABLE personal_color_colors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    hexCode VARCHAR(7) NOT NULL,
    category VARCHAR(20) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pc_colors_category ON personal_color_colors(category);
CREATE INDEX idx_pc_colors_name ON personal_color_colors(name);
```

### Personal_Color_Diagnoses 테이블 생성
```sql
CREATE TABLE personal_color_diagnoses (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    personalColor VARCHAR(20) NOT NULL,
    typePercentage INTEGER NOT NULL,
    typeDescriptions JSONB NOT NULL,
    labL DECIMAL(5,2) NOT NULL,
    labA DECIMAL(5,2) NOT NULL,
    labB DECIMAL(5,2) NOT NULL,
    matchingColorsTitle VARCHAR(255) NOT NULL,
    matchingColorsDescription TEXT NOT NULL,
    nonMatchingColorsTitle VARCHAR(255) NOT NULL,
    nonMatchingColorsDescription TEXT NOT NULL,
    contouringGuideImageId BIGINT NULL,
    makeupTips JSONB NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pc_diagnoses_user FOREIGN KEY (userId) 
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_pc_diagnoses_userId ON personal_color_diagnoses(userId);
CREATE INDEX idx_pc_diagnoses_personalColor ON personal_color_diagnoses(personalColor);
CREATE INDEX idx_pc_diagnoses_createdAt ON personal_color_diagnoses(createdAt DESC);

ALTER TABLE personal_color_diagnoses ADD CONSTRAINT fk_pc_diagnoses_contouring_image 
    FOREIGN KEY (contouringGuideImageId) REFERENCES images(id) ON DELETE SET NULL;
```

### Personal_Color_Diagnosis_Colors 테이블 생성
```sql
CREATE TABLE personal_color_diagnosis_colors (
    id BIGSERIAL PRIMARY KEY,
    diagnosisId BIGINT NOT NULL,
    colorId BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pc_diag_colors_diagnosis FOREIGN KEY (diagnosisId) 
        REFERENCES personal_color_diagnoses(id) ON DELETE CASCADE,
    CONSTRAINT fk_pc_diag_colors_color FOREIGN KEY (colorId) 
        REFERENCES personal_color_colors(id) ON DELETE CASCADE
);

CREATE INDEX idx_pc_diag_colors_diagnosisId ON personal_color_diagnosis_colors(diagnosisId);
CREATE INDEX idx_pc_diag_colors_colorId ON personal_color_diagnosis_colors(colorId);
CREATE INDEX idx_pc_diag_colors_type ON personal_color_diagnosis_colors(type);
```

### Products 테이블 생성
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    price BIGINT NOT NULL,
    imageId BIGINT NOT NULL,
    url VARCHAR(500) NOT NULL,
    category VARCHAR(50) NULL,
    tags JSONB NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_name_brand ON products(name, brand);
CREATE INDEX idx_products_createdAt ON products(createdAt DESC);

ALTER TABLE products ADD CONSTRAINT fk_products_image 
    FOREIGN KEY (imageId) REFERENCES images(id) ON DELETE SET NULL;
```

### Product_Wishes 테이블 생성
```sql
CREATE TABLE product_wishes (
    id BIGSERIAL PRIMARY KEY,
    productId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_wishes_product FOREIGN KEY (productId) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_wishes_user FOREIGN KEY (userId) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_product_wishes_product_user UNIQUE (productId, userId)
);

CREATE INDEX idx_product_wishes_productId ON product_wishes(productId);
CREATE INDEX idx_product_wishes_userId ON product_wishes(userId);
```

### Product_Skin_Codes 테이블 생성
```sql
CREATE TABLE product_skin_codes (
    id BIGSERIAL PRIMARY KEY,
    productId BIGINT NOT NULL,
    skinCode VARCHAR(10) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_skin_codes_product FOREIGN KEY (productId) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uk_product_skin_codes_product_skin UNIQUE (productId, skinCode)
);

CREATE INDEX idx_product_skin_codes_productId ON product_skin_codes(productId);
CREATE INDEX idx_product_skin_codes_skinCode ON product_skin_codes(skinCode);
```

### Product_Personal_Colors 테이블 생성
```sql
CREATE TABLE product_personal_colors (
    id BIGSERIAL PRIMARY KEY,
    productId BIGINT NOT NULL,
    personalColor VARCHAR(20) NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_personal_colors_product FOREIGN KEY (productId) 
        REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uk_product_personal_colors_product_color UNIQUE (productId, personalColor)
);

CREATE INDEX idx_product_personal_colors_productId ON product_personal_colors(productId);
CREATE INDEX idx_product_personal_colors_personalColor ON product_personal_colors(personalColor);
```

### Profanity_Words 테이블 생성
```sql
CREATE TABLE profanity_words (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(100) UNIQUE NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_profanity_words_word ON profanity_words(word);
```

### Post_Images 테이블 생성
```sql
CREATE TABLE post_images (
    id BIGSERIAL PRIMARY KEY,
    postId BIGINT NOT NULL,
    imageId BIGINT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    deletedAt TIMESTAMP NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_images_post FOREIGN KEY (postId) 
        REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_post_images_image FOREIGN KEY (imageId) 
        REFERENCES images(id) ON DELETE CASCADE
);

CREATE INDEX idx_post_images_postId ON post_images(postId);
CREATE INDEX idx_post_images_imageId ON post_images(imageId);
CREATE INDEX idx_post_images_order ON post_images(postId, createdAt);
```

### Notice_Images 테이블 생성
```sql
CREATE TABLE notice_images (
    id BIGSERIAL PRIMARY KEY,
    noticeId BIGINT NOT NULL,
    imageId BIGINT NOT NULL,
    isDeleted BOOLEAN NOT NULL DEFAULT FALSE,
    deletedAt TIMESTAMP NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notice_images_notice FOREIGN KEY (noticeId) 
        REFERENCES notices(id) ON DELETE CASCADE,
    CONSTRAINT fk_notice_images_image FOREIGN KEY (imageId) 
        REFERENCES images(id) ON DELETE CASCADE
);

CREATE INDEX idx_notice_images_noticeId ON notice_images(noticeId);
CREATE INDEX idx_notice_images_imageId ON notice_images(imageId);
CREATE INDEX idx_notice_images_order ON notice_images(noticeId, createdAt);
```

---

## 관계 설명

### 1. Users ↔ Posts
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 게시글을 작성할 수 있음
- **구현**: `posts.authorId`가 `users.id`를 참조

### 2. Users ↔ Comments
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 댓글을 작성할 수 있음
- **구현**: `comments.userId`가 `users.id`를 참조

### 3. Posts ↔ Comments
- **관계**: One-to-Many
- **설명**: 한 게시글에는 여러 댓글이 달릴 수 있음
- **구현**: `comments.postId`가 `posts.id`를 참조

### 4. Posts ↔ Post_Likes
- **관계**: One-to-Many
- **설명**: 한 게시글에는 여러 좋아요가 있을 수 있음
- **구현**: `post_likes.postId`가 `posts.id`를 참조
- **제약**: 한 사용자는 한 게시글에 한 번만 좋아요 가능 (UNIQUE 제약)

### 5. Users ↔ Post_Likes
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 게시글에 좋아요를 누를 수 있음
- **구현**: `post_likes.userId`가 `users.id`를 참조

### 6. Users ↔ Skin_Analysis_Reports
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 피부분석 보고서를 가질 수 있음
- **구현**: `skin_analysis_reports.userId`가 `users.id`를 참조

### 7. Users ↔ Personal_Color_Diagnoses
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 퍼스널컬러 진단을 가질 수 있음
- **구현**: `personal_color_diagnoses.userId`가 `users.id`를 참조

### 8. Personal_Color_Diagnoses ↔ Personal_Color_Colors
- **관계**: Many-to-Many
- **설명**: 한 진단은 여러 색상과 연결되고, 한 색상은 여러 진단에 사용될 수 있음
- **구현**: `personal_color_diagnosis_colors` 중간 테이블 사용
- **타입**: `matching` (어울리는 색상), `nonMatching` (안 어울리는 색상)

### 9. Products ↔ Product_Wishes
- **관계**: One-to-Many
- **설명**: 한 제품은 여러 사용자에게 찜될 수 있음
- **구현**: `product_wishes.productId`가 `products.id`를 참조
- **제약**: 한 사용자는 한 제품에 한 번만 찜 가능 (UNIQUE 제약)

### 10. Users ↔ Product_Wishes
- **관계**: One-to-Many
- **설명**: 한 사용자는 여러 제품을 찜할 수 있음
- **구현**: `product_wishes.userId`가 `users.id`를 참조

### 11. Products ↔ Product_Skin_Codes
- **관계**: Many-to-Many
- **설명**: 한 제품은 여러 피부코드에 추천될 수 있고, 한 피부코드는 여러 제품에 적용될 수 있음
- **구현**: `product_skin_codes` 중간 테이블 사용
- **제약**: 한 제품은 동일한 피부코드에 중복 추천 불가 (UNIQUE 제약)

### 12. Products ↔ Product_Personal_Colors
- **관계**: Many-to-Many
- **설명**: 한 제품은 여러 퍼스널컬러에 추천될 수 있고, 한 퍼스널컬러는 여러 제품에 적용될 수 있음
- **구현**: `product_personal_colors` 중간 테이블 사용
- **제약**: 한 제품은 동일한 퍼스널컬러에 중복 추천 불가 (UNIQUE 제약)

### 13. Images ↔ Users
- **관계**: One-to-Many
- **설명**: 한 이미지는 한 사용자의 프로필 이미지로 사용될 수 있음
- **구현**: `users.profileImageId`가 `images.id`를 참조

### 14. Images ↔ Posts
- **관계**: Many-to-Many
- **설명**: 한 게시글은 여러 이미지를 가질 수 있고, 한 이미지는 여러 게시글에 사용될 수 있음
- **구현**: `post_images` 중간 테이블 사용

### 15. Images ↔ Notices
- **관계**: Many-to-Many
- **설명**: 한 공지/이벤트는 여러 이미지를 가질 수 있고, 한 이미지는 여러 공지/이벤트에 사용될 수 있음
- **구현**: `notice_images` 중간 테이블 사용

### 16. Images ↔ Banners
- **관계**: One-to-Many
- **설명**: 한 이미지는 한 배너의 이미지로 사용될 수 있음
- **구현**: `banners.imageId`가 `images.id`를 참조

### 17. Images ↔ Products
- **관계**: One-to-Many
- **설명**: 한 이미지는 한 제품의 이미지로 사용될 수 있음
- **구현**: `products.imageId`가 `images.id`를 참조

### 18. Images ↔ Personal_Color_Diagnoses
- **관계**: One-to-Many
- **설명**: 한 이미지는 한 퍼스널컬러 진단의 컨투어링 가이드 이미지로 사용될 수 있음
- **구현**: `personal_color_diagnoses.contouringGuideImageId`가 `images.id`를 참조

---

## 데이터 타입 참고사항

### PostgreSQL
- `BIGSERIAL`: 자동 증가하는 64비트 정수
- `VARCHAR(n)`: 가변 길이 문자열
- `TEXT`: 무제한 길이 문자열
- `TIMESTAMP`: 날짜와 시간
- `JSONB`: JSON 데이터 (이진 형식, 인덱싱 및 쿼리 최적화)
- `DECIMAL(p,s)`: 고정 소수점 숫자 (p: 전체 자릿수, s: 소수점 자릿수)

### MySQL
- `BIGINT AUTO_INCREMENT`: 자동 증가하는 64비트 정수
- `VARCHAR(n)`: 가변 길이 문자열
- `TEXT`: 최대 65,535 바이트 문자열
- `DATETIME` 또는 `TIMESTAMP`: 날짜와 시간
- `JSON`: JSON 데이터 (MySQL 5.7.8 이상)
- `DECIMAL(p,s)`: 고정 소수점 숫자

---

## 추가 고려사항

### 1. 소프트 삭제 (Soft Delete)
- **구현 방식**: 모든 테이블에 `isDeleted` BOOLEAN 컬럼 추가 (DEFAULT FALSE)
- **삭제 처리**: 실제 데이터를 삭제하지 않고 `isDeleted = TRUE`로 업데이트
- **조회 필터링**: 모든 SELECT 쿼리에서 `WHERE isDeleted = FALSE` 조건 추가 필요
- **인덱스**: `isDeleted` 컬럼에 인덱스 추가하여 필터링 성능 최적화 고려

### 2. 페이지네이션
대용량 데이터 처리를 위해 인덱스 최적화 필요

### 3. 검색 기능
- **API 요구사항**: `/api/v1/posts?search=검색어` 파라미터 지원
- **구현**: Full-Text Search 인덱스 적용 (PostgreSQL의 `tsvector`, MySQL의 `FULLTEXT`)
- **검색 대상**: `title`, `content` 필드
- **성능**: 대용량 데이터 처리를 위해 인덱스 필수

### 4. 파일 업로드 및 이미지 관리
- **이미지 저장**: `images` 테이블에 S3 URL 저장
- **프로필 이미지**: `users.profileImageId`가 `images.id`를 참조
- **게시글 이미지**: `post_images` 중간 테이블로 게시글과 이미지 연결 (Many-to-Many)
- **공지/이벤트 이미지**: `notice_images` 중간 테이블로 공지/이벤트와 이미지 연결 (Many-to-Many)
- **배너 이미지**: `banners.imageId`가 `images.id`를 참조
- **제품 이미지**: `products.imageId`가 `images.id`를 참조
- **컨투어링 가이드**: `personal_color_diagnoses.contouringGuideImageId`가 `images.id`를 참조
- **저장소**: AWS S3 사용
- **이미지 형식**: jpg, jpeg, png, gif
- **최대 파일 크기**: 5MB/장
- **권장 이미지 크기**: 
  - 프로필: 500x500px 이상 (정사각형 권장)
  - 게시글: 최대 2000x2000px
- **이미지 삭제**: `images.isDeleted = TRUE`, `images.deletedAt` 설정으로 소프트 삭제
- **이미지 재사용**: 동일한 이미지를 여러 게시글/공지에 재사용 가능

### 5. JSON 데이터 저장
- **PostgreSQL**: `JSONB` 타입 사용 (이진 형식, 인덱싱 및 쿼리 최적화)
- **MySQL**: `JSON` 타입 사용 (MySQL 5.7.8 이상)
- **대체 방안**: `TEXT` 타입에 JSON 문자열 저장 후 애플리케이션 레벨에서 파싱
- **사용 테이블**:
  - `posts.hashtags`
  - `notices.links`
  - `skin_analysis_reports.skinTags`, `skin_analysis_reports.careTips`
  - `personal_color_diagnoses.typeDescriptions`, `personal_color_diagnoses.makeupTips`
  - `products.tags`

### 6. 집계 쿼리 최적화
- **게시글 댓글 수**: `comments` 테이블에서 `COUNT(*)` 집계
- **게시글 좋아요 수**: `post_likes` 테이블에서 `COUNT(*)` 집계
- **제품 찜 여부**: `product_wishes` 테이블에서 `EXISTS` 쿼리
- **성능 최적화**: 인덱스 활용 및 필요시 캐싱 고려

### 7. 사용자 피부타입 관리
- **users.skinType**: 사용자의 최신 피부분석 보고서의 `skinType` 값
- **업데이트 방식**: 
  - 피부분석 보고서 생성/수정 시 해당 사용자의 `skinType` 자동 업데이트
  - 또는 애플리케이션 레벨에서 최신 보고서 조회 시 동적으로 계산

### 8. 비속어 검사
- **구현 방식**: `profanity_words` 테이블의 단어 목록과 게시글/댓글 내용 비교
- **검사 대상**: `title`, `content`, `hashtags` (게시글), `content` (댓글)
- **성능**: 인덱스 활용 및 필요시 메모리 캐싱 고려

### 9. 제품 추천
- **피부코드 기반**: `product_skin_codes` 중간 테이블을 통해 제품과 피부코드 연결
  - 한 제품은 여러 피부코드에 추천 가능 (Many-to-Many)
  - 쿼리: `JOIN product_skin_codes ON products.id = product_skin_codes.productId WHERE product_skin_codes.skinCode = ?`
- **퍼스널컬러 기반**: `product_personal_colors` 중간 테이블을 통해 제품과 퍼스널컬러 연결
  - 한 제품은 여러 퍼스널컬러에 추천 가능 (Many-to-Many)
  - 쿼리: `JOIN product_personal_colors ON products.id = product_personal_colors.productId WHERE product_personal_colors.personalColor = ?`
- **제품 기반 추천**: `products.category`, `products.tags`를 활용한 유사 제품 추천 가능
- **인덱스**: `product_skin_codes.skinCode`, `product_personal_colors.personalColor`에 인덱스 추가하여 추천 조회 성능 최적화

### 10. 퍼스널컬러 진단 색상 관계
- **구현 방식**: `personal_color_diagnosis_colors` 중간 테이블 사용
- **타입 구분**: `type` 컬럼으로 `matching` (어울리는 색상), `nonMatching` (안 어울리는 색상) 구분
- **색상 개수**: 어울리는 색상 5개, 안 어울리는 색상 5개

### 11. 관리자 권한
- **구현 방식**: `users` 테이블에 `role` 컬럼 추가
- **권한 값**: `user` (일반 사용자), `admin` (관리자)
- **기본값**: `user`
- **관리자 API 접근**: JWT 토큰에 `role: 'admin'` 포함 여부로 검증
- **인덱스**: `role` 컬럼에 인덱스 추가하여 관리자 조회 최적화

### 12. 공지/이벤트 관리
- **타입**: `notice` (공지), `event` (이벤트)
- **이벤트 기간**: `startDate`, `endDate`로 이벤트 기간 관리
- **공지사항**: `startDate`, `endDate`는 NULL 가능
- **인덱스**: `type`, `createdAt` 인덱스로 조회 성능 최적화

### 13. 회원가입 필드
- **필수 필드**: 이메일, 이름, 휴대폰 번호, 생년월일, 성별, 비밀번호, 비밀번호 확인
- **휴대폰 번호**: UNIQUE 제약조건으로 중복 가입 방지
- **비밀번호**: 일반 회원가입 시 필수, 소셜 로그인 사용자는 NULL 가능
- **생년월일**: DATE 타입으로 저장 (YYYY-MM-DD)
- **성별**: VARCHAR(10), 값은 "male", "female", "other" 중 하나

### 14. 소셜 로그인
- **지원 제공자**: 네이버(naver), 카카오(kakao)
- **구현 방식**: 
  - 단일 엔드포인트 `/api/v1/auth/social` 사용
  - Request Body에 `provider` 필드로 제공자 구분 (naver, kakao)
  - `provider` 필드로 로그인 제공자 구분 (local, naver, kakao)
  - 소셜 로그인 사용자는 `password`가 NULL
  - 소셜 로그인 시 Access Token을 받아서 사용자 정보 조회 후 자동 가입/로그인 처리
- **인덱스**: `provider` 컬럼에 인덱스 추가하여 소셜 로그인 사용자 조회 최적화

### 15. 정렬 및 필터링
- **정렬 기준**: `sortBy` 파라미터로 다양한 필드 정렬 지원
- **정렬 방향**: `order` 파라미터로 `asc` (오름차순), `desc` (내림차순) 지원
- **필터링**: `search`, `type`, `userId`, `skinCode`, `personalColor` 등 다양한 필터 지원
- **인덱스**: 정렬 및 필터링에 사용되는 컬럼에 인덱스 추가
