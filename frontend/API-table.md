# API 명세서 (표 형식)

## 기본 정보
- **Base URL**: `http://localhost:18080/api`
- **API Version**: `v1`
- **Content-Type**: `application/json`
- **인증**: Bearer Token (JWT) - `Authorization: Bearer {token}`

---

## 1. 사용자 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 회원가입 | 회원가입 | POST | `/api/v1/auth/register` | - | `{ email, name, phone, birthDate, gender, password, passwordConfirm }` | `{ success, data: { id, email, name, phone, birthDate, gender, createdAt } }` | 이메일/전화번호 중복 체크, 비밀번호 확인 검증, 비밀번호 해시화 후 저장 | 400: 검증 실패, 409: 중복 이메일/전화번호 |
| 로그인 | 로그인 | POST | `/api/v1/auth/login` | - | `{ email, password }` | `{ success, data: { token, user: { id, email, name } } }` | 이메일/비밀번호 검증, JWT 토큰 발급 | 401: 인증 실패 |
| 소셜 로그인 | 소셜 로그인 | POST | `/api/v1/auth/social` | - | `{ provider, accessToken }` | `{ success, data: { token, user: { id, email, name, provider }, isNewUser } }` | OAuth 토큰 검증, 사용자 정보 조회/생성, JWT 토큰 발급 | provider: "naver" 또는 "kakao", 400: 검증 실패, 401: 유효하지 않은 토큰 |
| 사용자 정보 조회 | 사용자 정보 조회 | GET | `/api/v1/users/me` | - | - | `{ success, data: { id, email, name, phone, birthDate, gender, profileImageUrl, provider, createdAt, updatedAt } }` | JWT 토큰에서 사용자 ID 추출, 사용자 정보 조회 | 인증 필수 |
| 사용자 정보 수정 | 사용자 정보 수정 | PUT | `/api/v1/users/me` | - | `{ name?, phone?, birthDate?, gender?, email? }` | `{ success, data: { id, email, name, phone, birthDate, gender, profileImageUrl, provider, updatedAt } }` | 전송된 필드만 수정, 이메일/전화번호 중복 체크 | 인증 필수, 400: 검증 실패, 409: 중복 |
| 회원 탈퇴 | 회원 탈퇴 | DELETE | `/api/v1/users/me` | - | `{ password? }` | `{ success, message }` | 일반 회원: 비밀번호 확인, 소셜 회원: 바로 탈퇴, 관련 데이터 처리 | 인증 필수, 400: 비밀번호 필요, 401: 비밀번호 불일치 |
| 프로필 이미지 업로드/수정 | 프로필 이미지 업로드/수정 | PUT | `/api/v1/users/me/profile-image` | - | Form Data: `image` (jpg, jpeg, png, gif, 최대 5MB) | `{ success, data: { profileImageUrl, updatedAt } }` | 기존 이미지 S3 삭제, 새 이미지 S3 업로드, DB 업데이트 | 인증 필수, Content-Type: multipart/form-data, 400: 파일 형식/크기 오류, 500: 업로드 실패 |
| 프로필 이미지 삭제 | 프로필 이미지 삭제 | DELETE | `/api/v1/users/me/profile-image` | - | - | `{ success, message }` | S3에서 이미지 삭제, DB에서 URL 제거 | 인증 필수, 500: 삭제 실패 |

---

## 2. 게시글 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 게시글 목록 조회 | 게시글 목록 조회 | GET | `/api/v1/posts` | `page?, limit?, search?, sortBy?, order?` | - | `{ success, data: { posts: [{ id, title, content, authorId, authorName, authorSkinType, hashtags, images, commentCount, likeCount, createdAt, updatedAt }], pagination } }` | 페이지네이션, 검색, 정렬 처리, 댓글 수/좋아요 수 집계 | sortBy: createdAt/updatedAt/title, order: asc/desc/popularity |
| 게시글 상세 조회 | 게시글 상세 조회 | GET | `/api/v1/posts/:id` | - | - | `{ success, data: { id, title, content, authorId, authorName, hashtags, images, createdAt, updatedAt } }` | 게시글 ID로 조회 | 404: 게시글 없음 |
| 게시글 사진 업로드 | 게시글 사진 업로드 | POST | `/api/v1/posts/images` | - | Form Data: `images[]` (여러 장, jpg, jpeg, png, gif, 최대 5MB/장) | `{ success, data: { imageUrls: [] } }` | 여러 이미지 S3 업로드, URL 배열 반환 | 인증 필수, Content-Type: multipart/form-data, 400: 파일 형식/크기 오류, 500: 업로드 실패 |
| 게시글 비속어 검사 | 게시글 비속어 검사 | POST | `/api/v1/posts/check-profanity` | - | `{ title, content, hashtags? }` | `{ success, data: { hasProfanity, detectedWords, message } }` | 제목/내용/해시태그에서 비속어 검사 | 인증 필수 |
| 게시글 생성 | 게시글 생성 | POST | `/api/v1/posts` | - | `{ title, content, hashtags?, imageUrls?, forcePublish? }` | `{ success, data: { id, title, content, authorId, hashtags, images, createdAt, updatedAt } }` | 비속어 검사 (forcePublish=false 시), 게시글 저장 | 인증 필수, 400: PROFANITY_DETECTED (forcePublish=false일 때) |
| 게시글 수정 비속어 검사 | 게시글 수정 비속어 검사 | POST | `/api/v1/posts/:id/check-profanity` | - | `{ title?, content?, hashtags? }` | `{ success, data: { hasProfanity, detectedWords, message } }` | 수정 내용 비속어 검사 | 인증 필수, 작성자만 가능 |
| 게시글 수정 | 게시글 수정 | PUT | `/api/v1/posts/:id` | - | `{ title?, content?, hashtags?, imageUrls?, forcePublish? }` | `{ success, data: { id, title, content, authorId, hashtags, images, updatedAt } }` | 비속어 검사 (forcePublish=false 시), 전송된 필드만 수정, 기존 이미지 S3 삭제 | 인증 필수, 작성자만 가능, 400: PROFANITY_DETECTED |
| 게시글 삭제 | 게시글 삭제 | DELETE | `/api/v1/posts/:id` | - | - | `{ success, message }` | 게시글 삭제, 관련 이미지 S3 삭제 | 인증 필수, 작성자만 가능 |
| 게시글 댓글 조회 | 게시글 댓글 조회 | GET | `/api/v1/posts/:id/comments` | `page?, limit?, sortBy?, order?` | - | `{ success, data: { comments: [{ id, userId, userName, userProfileImageUrl, userSkinType, content, createdAt, updatedAt }], pagination } }` | 게시글의 댓글 목록 조회, 페이지네이션 | 404: 게시글 없음 |

---

## 3. 공지/이벤트 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 공지/이벤트 목록 조회 | 공지/이벤트 목록 조회 | GET | `/api/v1/notices` | `page?, limit?, type?, sortBy?, order?` | - | `{ success, data: { notices: [{ id, type, title, content, startDate, endDate, createdAt, updatedAt }], pagination } }` | 페이지네이션, 타입 필터링, 정렬 | type: notice/event, sortBy: createdAt/startDate/endDate/title |
| 공지/이벤트 상세 조회 | 공지/이벤트 상세 조회 | GET | `/api/v1/notices/:id` | - | - | `{ success, data: { id, type, title, content, images, links, startDate, endDate, createdAt, updatedAt } }` | 공지/이벤트 상세 정보 조회 | 404: 공지/이벤트 없음 |

---

## 4. 배너 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 배너 목록 조회 | 배너 목록 조회 | GET | `/api/v1/banners` | - | - | `{ success, data: { banners: [{ id, title, description, imageUrl, createdAt, updatedAt }] } }` | 배너 목록 조회 | - |

---

## 5. 피부분석 보고서 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 피부분석 보고서 목록 조회 | 피부분석 보고서 목록 조회 | GET | `/api/v1/skin-analysis-reports` | `page?, limit?, sortBy?, order?` | - | `{ success, data: { reports: [{ id, userId, userName, userSkinType, skinAge, skinType, scores: { pores, blackheads, darkCircles, porphyrin, keratin, redness }, createdAt, updatedAt }], pagination } }` | 페이지네이션, 정렬 처리 | sortBy: createdAt/updatedAt |
| 피부분석 보고서 상세 조회 | 피부분석 보고서 상세 조회 | GET | `/api/v1/skin-analysis-reports/:id` | - | - | `{ success, data: { id, userId, userName, userAge, skinAge, skinCondition, skinConditionDescription, skinCode, skinType, analysisScores: { pores, blackheads, pigmentation, wrinkles, porphyrin, sensitivity, darkCircles }, skinTags, skinCodeDescription, careTips, createdAt, updatedAt } }` | 피부분석 보고서 상세 정보 조회 | 404: 보고서 없음 |

---

## 6. 퍼스널컬러 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 퍼스널컬러 색상 조회 | 퍼스널컬러 색상 조회 | GET | `/api/v1/personal-colors/colors` | - | - | `{ success, data: { colors: [{ id, name, hexCode, category }] } }` | 퍼스널컬러 색상 목록 조회 | category: 봄웜톤/여름쿨톤/가을웜톤/겨울쿨톤 |
| 퍼스널컬러 상세 진단 조회 | 퍼스널컬러 상세 진단 조회 | GET | `/api/v1/personal-colors/:id` | - | - | `{ success, data: { id, userId, personalColor, typePercentage, typeDescriptions, labValues: { l, a, b }, matchingColors: { title, description, colors: [{ id, name, hexCode }] }, nonMatchingColors: { title, description, colors: [{ id, name, hexCode }] }, contouringGuideUrl, makeupTips, createdAt, updatedAt } }` | 퍼스널컬러 진단 상세 정보 조회 | 404: 진단 정보 없음 |

---

## 7. 제품 관련 API

| 구분 | API명 | Method | URL | Query | Request | Response | 로직 | 비고 |
|------|-------|--------|-----|-------|---------|----------|------|------|
| 피부코드 기반 제품 추천 조회 | 피부코드 기반 제품 추천 조회 | GET | `/api/v1/products/recommended-by-skin-code` | `skinCode` (required), `page?, limit?` | - | `{ success, data: { products: [{ id, name, brand, price, imageUrl, url, isWished }], pagination } }` | 피부코드로 제품 필터링, 페이지네이션, 인증된 사용자 찜 여부 확인 | 400: skinCode 필수 |
| 퍼스널컬러 기반 제품 추천 조회 | 퍼스널컬러 기반 제품 추천 조회 | GET | `/api/v1/products/recommended-by-personal-color` | `personalColor` (required), `page?, limit?` | - | `{ success, data: { products: [{ id, name, brand, price, imageUrl, url, isWished }], pagination } }` | 퍼스널컬러로 제품 필터링, 페이지네이션, 인증된 사용자 찜 여부 확인 | 400: personalColor 필수 |
| 제품 찜 추가/취소 | 제품 찜 추가/취소 | PUT | `/api/v1/products/:id/wish` | - | `{ isWished }` | `{ success, data: { productId, isWished, message } }` | 찜 목록 추가/제거 | 인증 필수, 404: 제품 없음 |

---

## 공통 사항

### 에러 코드
| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `VALIDATION_ERROR` | 400 | 요청 데이터 검증 실패 |
| `UNAUTHORIZED` | 401 | 인증 실패 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `NOT_FOUND` | 404 | 리소스를 찾을 수 없음 |
| `CONFLICT` | 409 | 리소스 충돌 (예: 중복 이메일) |
| `PROFANITY_DETECTED` | 400 | 비속어 감지됨 |
| `INTERNAL_ERROR` | 500 | 서버 내부 오류 |

### 공통 응답 형식
- **성공 응답**: `{ success: true, data: { ... } }`
- **실패 응답**: `{ success: false, error: { code, message } }`

### 페이지네이션
- **Query Parameters**: `page` (기본값: 1), `limit` (기본값: 10)
- **Response**: `{ page, limit, total, totalPages }`

### 정렬
- **Query Parameters**: `sortBy` (기본값: "createdAt"), `order` (기본값: "desc")
- **order 값**: `asc` (오름차순), `desc` (내림차순), `popularity` (인기순)

---

> **참고**: 관리자 API는 별도 파일 [`admin-API.md`](./admin-API.md)에서 확인하실 수 있습니다.
