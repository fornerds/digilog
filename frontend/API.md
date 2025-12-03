# API 명세서

## 기본 정보
- **Base URL**: `http://localhost:18080/api`
- **API Version**: `v1`
- **Content-Type**: `application/json`

## 인증
- **방식**: Bearer Token (JWT)
- **Header**: `Authorization: Bearer {token}`

---

## 엔드포인트 목록

### 1. 사용자 관련 API

#### 1.1 회원가입
- **Method**: `POST`
- **URL**: `/api/v1/auth/register`
- **Description**: 새로운 사용자 회원가입

**Request Body**:
```json
{
  "email": "string",
  "name": "string",
  "phone": "string",
  "birthDate": "string",
  "gender": "string",
  "password": "string",
  "passwordConfirm": "string"
}
```

**필드 설명**:
- `email`: 이메일 주소 (필수)
- `name`: 이름 (필수)
- `phone`: 휴대폰 번호 (필수, 형식: 010-1234-5678 또는 01012345678)
- `birthDate`: 생년월일 (필수, 형식: YYYY-MM-DD)
- `gender`: 성별 (필수, "male", "female", "other")
- `password`: 비밀번호 (필수, 최소 8자 이상)
- `passwordConfirm`: 비밀번호 확인 (필수, password와 일치해야 함)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "string",
    "gender": "string",
    "createdAt": "datetime"
  }
}
```

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "이메일 형식이 올바르지 않습니다." // 또는 "비밀번호가 일치하지 않습니다.", "휴대폰 번호 형식이 올바르지 않습니다." 등
  }
}
```

**Error Response 409**:
```json
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "이미 등록된 이메일입니다." // 또는 "이미 등록된 휴대폰 번호입니다."
  }
}
```

---

#### 1.2 로그인
- **Method**: `POST`
- **URL**: `/api/v1/auth/login`
- **Description**: 사용자 로그인

**Request Body**:
```json
{
  "email": "string",
  "password": "string"
}
```

**Response 200**:
```json
{
  "success": true,
  "data": {
    "token": "string",
    "user": {
      "id": "number",
      "email": "string",
      "name": "string"
    }
  }
}
```

**Error Response 401**:
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "이메일 또는 비밀번호가 올바르지 않습니다."
  }
}
```

---

#### 1.3 소셜 로그인
- **Method**: `POST`
- **URL**: `/api/v1/auth/social`
- **Description**: 소셜 로그인 (네이버, 카카오)

**Request Body**:
```json
{
  "provider": "string",
  "accessToken": "string"
}
```

**필드 설명**:
- `provider`: 소셜 로그인 제공자 (필수, "naver" 또는 "kakao")
- `accessToken`: OAuth 인증 후 받은 Access Token (필수)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "token": "string",
    "user": {
      "id": "number",
      "email": "string",
      "name": "string",
      "provider": "string"
    },
    "isNewUser": "boolean"
  }
}
```

**필드 설명**:
- `token`: JWT 토큰 (서버에서 발급)
- `user.provider`: 로그인 제공자 (naver, kakao)
- `isNewUser`: 신규 사용자 여부 (true: 신규 가입, false: 기존 사용자)

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "지원하지 않는 소셜 로그인 제공자입니다." // 또는 "provider는 필수입니다."
  }
}
```

**Error Response 401**:
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "유효하지 않은 소셜 로그인 토큰입니다."
  }
}
```

---

#### 1.4 사용자 정보 조회
- **Method**: `GET`
- **URL**: `/api/v1/users/me`
- **Description**: 현재 로그인한 사용자 정보 조회
- **Authentication**: Required

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "string",
    "gender": "string",
    "profileImageUrl": "string",
    "provider": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `id`: 사용자 ID
- `email`: 이메일 주소
- `name`: 이름
- `phone`: 휴대폰 번호
- `birthDate`: 생년월일 (YYYY-MM-DD)
- `gender`: 성별 (male, female, other)
- `profileImageUrl`: 프로필 이미지 URL (S3 URL, 없으면 null)
- `provider`: 로그인 제공자 (local, naver, kakao)
- `createdAt`: 가입일시
- `updatedAt`: 수정일시

---

#### 1.5 사용자 정보 수정
- **Method**: `PUT`
- **URL**: `/api/v1/users/me`
- **Description**: 현재 로그인한 사용자 정보 수정
- **Authentication**: Required

**Request Body**:
```json
{
  "name": "string",
  "phone": "string",
  "birthDate": "string",
  "gender": "string",
  "email": "string"
}
```

**필드 설명**:
- `name`: 이름 (선택)
- `phone`: 휴대폰 번호 (선택, 형식: 010-1234-5678 또는 01012345678)
- `birthDate`: 생년월일 (선택, 형식: YYYY-MM-DD)
- `gender`: 성별 (선택, "male", "female", "other")
- `email`: 이메일 주소 (선택)

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `phone`과 `email`은 중복 체크가 필요합니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "string",
    "gender": "string",
    "profileImageUrl": "string",
    "provider": "string",
    "updatedAt": "datetime"
  }
}
```

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "휴대폰 번호 형식이 올바르지 않습니다." // 또는 "이메일 형식이 올바르지 않습니다." 등
  }
}
```

**Error Response 409**:
```json
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "이미 사용 중인 이메일입니다." // 또는 "이미 사용 중인 휴대폰 번호입니다."
  }
}
```

---

#### 1.6 회원 탈퇴
- **Method**: `DELETE`
- **URL**: `/api/v1/users/me`
- **Description**: 현재 로그인한 사용자 계정 삭제
- **Authentication**: Required

**Request Body** (선택):
```json
{
  "password": "string"
}
```

**필드 설명**:
- `password`: 비밀번호 (일반 회원가입 사용자만 필수, 소셜 로그인 사용자는 불필요)

**주의사항**:
- 일반 회원가입 사용자는 비밀번호 확인이 필요합니다.
- 소셜 로그인 사용자는 비밀번호 없이 탈퇴 가능합니다.
- 탈퇴 시 관련 데이터는 정책에 따라 삭제되거나 보관됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "회원 탈퇴가 완료되었습니다."
}
```

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "비밀번호를 입력해주세요."
  }
}
```

**Error Response 401**:
```json
{
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "비밀번호가 올바르지 않습니다."
  }
}
```

---

#### 1.7 프로필 이미지 업로드/수정
- **Method**: `PUT`
- **URL**: `/api/v1/users/me/profile-image`
- **Description**: 프로필 이미지 업로드 및 수정 (S3 사용)
- **Authentication**: Required
- **Content-Type**: `multipart/form-data`

**Request Body** (Form Data):
- `image`: 이미지 파일 (필수, jpg, jpeg, png, gif, 최대 5MB)

**주의사항**:
- 이미지 파일은 S3에 업로드됩니다.
- 기존 프로필 이미지가 있으면 S3에서 삭제 후 새 이미지로 교체됩니다.
- 지원 형식: jpg, jpeg, png, gif
- 최대 파일 크기: 5MB
- 권장 이미지 크기: 500x500px 이상 (정사각형 권장)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "profileImageUrl": "string",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `profileImageUrl`: 업로드된 프로필 이미지의 S3 URL

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "이미지 파일만 업로드 가능합니다." // 또는 "파일 크기는 5MB 이하여야 합니다."
  }
}
```

**Error Response 500**:
```json
{
  "success": false,
  "error": {
    "code": "INTERNAL_ERROR",
    "message": "이미지 업로드에 실패했습니다."
  }
}
```

---

#### 1.8 프로필 이미지 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/users/me/profile-image`
- **Description**: 프로필 이미지 삭제
- **Authentication**: Required

**주의사항**:
- 프로필 이미지가 없으면 에러가 발생하지 않고 성공 응답을 반환합니다.
- S3에서 이미지 파일이 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "프로필 이미지가 삭제되었습니다."
}
```

**Error Response 500**:
```json
{
  "success": false,
  "error": {
    "code": "INTERNAL_ERROR",
    "message": "이미지 삭제에 실패했습니다."
  }
}
```

---

### 2. 게시글 관련 API

#### 2.1 게시글 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/posts`
- **Description**: 게시글 목록 조회 (페이지네이션 지원)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
  - `title`: 제목 (가나다순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순
  - `popularity`: 인기순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "posts": [
      {
        "id": "number",
        "title": "string",
        "content": "string",
        "authorId": "number",
        "authorName": "string",
        "authorSkinType": "string",
        "hashtags": ["string"],
        "images": ["string"],
        "commentCount": "number",
        "likeCount": "number",
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `authorSkinType`: 게시글 작성자 피부타입 (없으면 null)
- `commentCount`: 댓글 수
- `likeCount`: 좋아요 수

---

#### 2.2 게시글 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/posts/:id`
- **Description**: 특정 게시글 상세 정보 조회

**Path Parameters**:
- `id`: 게시글 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "content": "string",
    "authorId": "number",
    "authorName": "string",
    "hashtags": ["string"],
    "images": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "게시글을 찾을 수 없습니다."
  }
}
```

---

#### 2.3 게시글 사진 업로드
TODO: 한장씩 올릴지 아니면 여러 장 한 번에 올릴지 결정해야함
- **Method**: `POST`
- **URL**: `/api/v1/posts/images`
- **Description**: 게시글용 사진 업로드 (S3 사용)
- **Authentication**: Required
- **Content-Type**: `multipart/form-data`

**Request Body** (Form Data):
- `images`: 이미지 파일들 (필수, 여러 장 가능, jpg, jpeg, png, gif, 최대 5MB/장)

**주의사항**:
- 여러 장의 이미지를 한 번에 업로드할 수 있습니다.
- 이미지 파일은 S3에 업로드됩니다.
- 지원 형식: jpg, jpeg, png, gif
- 최대 파일 크기: 5MB/장
- 권장 이미지 크기: 최대 2000x2000px

**Response 200**:
```json
{
  "success": true,
  "data": {
    "imageUrls": ["string"]
  }
}
```

**필드 설명**:
- `imageUrls`: 업로드된 이미지들의 S3 URL 배열 (업로드 순서대로 반환)

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "이미지 파일만 업로드 가능합니다." // 또는 "파일 크기는 5MB 이하여야 합니다."
  }
}
```

**Error Response 500**:
```json
{
  "success": false,
  "error": {
    "code": "INTERNAL_ERROR",
    "message": "이미지 업로드에 실패했습니다."
  }
}
```

---

#### 2.4 게시글 비속어 검사
- **Method**: `POST`
- **URL**: `/api/v1/posts/check-profanity`
- **Description**: 게시글 내용의 비속어 검사
- **Authentication**: Required

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "hashtags": ["string"]
}
```

**필드 설명**:
- `title`: 제목 (필수)
- `content`: 내용 (필수)
- `hashtags`: 해시태그 배열 (선택)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "hasProfanity": "boolean",
    "detectedWords": ["string"],
    "message": "string"
  }
}
```

**필드 설명**:
- `hasProfanity`: 비속어 감지 여부 (true: 비속어 있음, false: 비속어 없음)
- `detectedWords`: 감지된 비속어 목록 (없으면 빈 배열)
- `message`: 안내 메시지 (비속어가 있으면 "비속어가 감지되었습니다.", 없으면 "비속어가 감지되지 않았습니다.")

**사용 시나리오**:
1. 사용자가 게시글 작성 후 "생성" 버튼 클릭
2. 프론트엔드에서 비속어 검사 API 호출
3. `hasProfanity: false` → 게시글 생성 API 호출
4. `hasProfanity: true` → "비속어가 감지되었습니다" 알림 표시 + "게시하기" 버튼 표시
5. 사용자가 "게시하기" 클릭 시 게시글 생성 API에 `forcePublish: true` 전송

---

#### 2.5 게시글 생성
- **Method**: `POST`
- **URL**: `/api/v1/posts`
- **Description**: 새로운 게시글 생성
- **Authentication**: Required

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "hashtags": ["string"],
  "imageUrls": ["string"],
  "forcePublish": "boolean"
}
```

**필드 설명**:
- `title`: 제목 (필수)
- `content`: 내용 (필수)
- `hashtags`: 해시태그 배열 (선택, 예: ["뷰티", "메이크업", "리뷰"])
- `imageUrls`: 이미지 URL 배열 (선택, 사진 업로드 API에서 받은 URL들)
- `forcePublish`: 강제 게시 여부 (선택, 기본값: false)
  - `false`: 비속어가 있으면 게시글 생성 실패
  - `true`: 비속어가 있어도 강제로 게시글 생성

**주의사항**:
- `forcePublish: false`일 때 비속어가 감지되면 게시글 생성이 실패합니다.
- `forcePublish: true`일 때는 비속어가 있어도 게시글이 생성됩니다.
- 권장 플로우: 비속어 검사 API로 먼저 확인 후, 사용자 확인을 받고 `forcePublish: true`로 생성

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "content": "string",
    "authorId": "number",
    "hashtags": ["string"],
    "images": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**Error Response 400** (비속어 감지 시):
```json
{
  "success": false,
  "error": {
    "code": "PROFANITY_DETECTED",
    "message": "비속어가 감지되었습니다.",
    "data": {
      "detectedWords": ["string"]
    }
  }
}
```

---

#### 2.6 게시글 수정 비속어 검사
- **Method**: `POST`
- **URL**: `/api/v1/posts/:id/check-profanity`
- **Description**: 게시글 수정 내용의 비속어 검사
- **Authentication**: Required (작성자만 가능)

**Path Parameters**:
- `id`: 게시글 ID

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "hashtags": ["string"]
}
```

**필드 설명**:
- `title`: 제목 (선택, 수정할 제목)
- `content`: 내용 (선택, 수정할 내용)
- `hashtags`: 해시태그 배열 (선택, 수정할 해시태그)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "hasProfanity": "boolean",
    "detectedWords": ["string"],
    "message": "string"
  }
}
```

**필드 설명**:
- `hasProfanity`: 비속어 감지 여부 (true: 비속어 있음, false: 비속어 없음)
- `detectedWords`: 감지된 비속어 목록 (없으면 빈 배열)
- `message`: 안내 메시지

---

#### 2.7 게시글 수정
- **Method**: `PUT`
- **URL**: `/api/v1/posts/:id`
- **Description**: 게시글 수정
- **Authentication**: Required (작성자만 수정 가능)

**Path Parameters**:
- `id`: 게시글 ID

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "hashtags": ["string"],
  "imageUrls": ["string"],
  "forcePublish": "boolean"
}
```

**필드 설명**:
- `title`: 제목 (선택)
- `content`: 내용 (선택)
- `hashtags`: 해시태그 배열 (선택, 전송된 배열로 교체)
- `imageUrls`: 이미지 URL 배열 (선택, 전송된 배열로 교체, 기존 이미지는 S3에서 삭제)
- `forcePublish`: 강제 게시 여부 (선택, 기본값: false)
  - `false`: 비속어가 있으면 게시글 수정 실패
  - `true`: 비속어가 있어도 강제로 게시글 수정

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `hashtags`와 `imageUrls`는 빈 배열로 전송하면 모두 삭제됩니다.
- 권장 플로우: 비속어 검사 API로 먼저 확인 후, 사용자 확인을 받고 `forcePublish: true`로 수정

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "content": "string",
    "authorId": "number",
    "hashtags": ["string"],
    "images": ["string"],
    "updatedAt": "datetime"
  }
}
```

**Error Response 400** (비속어 감지 시):
```json
{
  "success": false,
  "error": {
    "code": "PROFANITY_DETECTED",
    "message": "비속어가 감지되었습니다.",
    "data": {
      "detectedWords": ["string"]
    }
  }
}
```

---

#### 2.8 게시글 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/posts/:id`
- **Description**: 게시글 삭제
- **Authentication**: Required (작성자만 삭제 가능)

**Path Parameters**:
- `id`: 게시글 ID

**Response 200**:
```json
{
  "success": true,
  "message": "게시글이 삭제되었습니다."
}
```

---

#### 2.9 게시글 댓글 조회
- **Method**: `GET`
- **URL**: `/api/v1/posts/:id/comments`
- **Description**: 특정 게시글의 댓글 목록 조회 (페이지네이션 지원)
- **Authentication**: Not Required

**Path Parameters**:
- `id`: 게시글 ID

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
- `order` (optional): 정렬 방향 (기본값: "asc")
  - `asc`: 오름차순 (오래된 댓글부터)
  - `desc`: 내림차순 (최신 댓글부터)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "comments": [
      {
        "id": "number",
        "userId": "number",
        "userName": "string",
        "userProfileImageUrl": "string",
        "userSkinType": "string",
        "content": "string",
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `userId`: 댓글 작성자 ID
- `userName`: 댓글 작성자 이름
- `userProfileImageUrl`: 댓글 작성자 프로필 이미지 URL (없으면 null)
- `userSkinType`: 댓글 작성자 피부타입 (없으면 null)
- `content`: 댓글 내용

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "게시글을 찾을 수 없습니다."
  }
}
```

---

### 3. 공지/이벤트 관련 API

#### 3.1 공지/이벤트 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/notices`
- **Description**: 공지/이벤트 목록 조회 (페이지네이션 지원)
- **Authentication**: Not Required

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `type` (optional): `notice` 또는 `event` 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `startDate`: 시작일시 (이벤트용)
  - `endDate`: 종료일시 (이벤트용)
  - `title`: 제목 (가나다순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "notices": [
      {
        "id": "number",
        "type": "string",
        "title": "string",
        "content": "string",
        "startDate": "datetime",
        "endDate": "datetime",
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `type`: 타입 (notice: 공지, event: 이벤트)
- `startDate`: 시작일시 (이벤트의 경우 필수, 공지의 경우 null)
- `endDate`: 종료일시 (이벤트의 경우 필수, 공지의 경우 null)

---

#### 3.2 공지/이벤트 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/notices/:id`
- **Description**: 특정 공지/이벤트 상세 정보 조회
- **Authentication**: Not Required

**Path Parameters**:
- `id`: 공지/이벤트 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "type": "string",
    "title": "string",
    "content": "string",
    "images": ["string"],
    "links": ["string"],
    "startDate": "datetime",
    "endDate": "datetime",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `type`: 타입 (notice: 공지, event: 이벤트)
- `images`: 이미지 URL 배열 (S3 URL, 없으면 빈 배열)
- `links`: 링크 URL 배열 (없으면 빈 배열)
- `startDate`: 시작일시 (이벤트의 경우 필수, 공지의 경우 null)
- `endDate`: 종료일시 (이벤트의 경우 필수, 공지의 경우 null)

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "공지/이벤트를 찾을 수 없습니다."
  }
}
```

---

### 4. 배너 관련 API

#### 4.1 배너 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/banners`
- **Description**: 배너 목록 조회
- **Authentication**: Not Required

**Response 200**:
```json
{
  "success": true,
  "data": {
    "banners": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "imageUrl": "string",
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ]
  }
}
```

**필드 설명**:
- `id`: 배너 ID
- `title`: 배너 제목
- `description`: 배너 설명
- `imageUrl`: 배너 이미지 URL (S3 URL)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

### 5. 피부분석 보고서 관련 API

#### 5.1 피부분석 보고서 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/skin-analysis-reports`
- **Description**: 피부분석 보고서 목록 조회 (페이지네이션 지원)
- **Authentication**: Not Required

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순/과거순)
  - `updatedAt`: 수정일시
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순 (과거순)
  - `desc`: 내림차순 (최신순)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "reports": [
      {
        "id": "number",
        "userId": "number",
        "userName": "string",
        "userSkinType": "string",
        "skinAge": "number",
        "skinType": "string",
        "scores": {
          "pores": "number",
          "blackheads": "number",
          "darkCircles": "number",
          "porphyrin": "number",
          "keratin": "number",
          "redness": "number"
        },
        "createdAt": "datetime",
        "updatedAt": "datetime"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `userId`: 보고서 작성자 ID
- `userName`: 보고서 작성자 이름
- `userSkinType`: 보고서 작성자 피부타입
- `skinAge`: 피부나이 (숫자)
- `skinType`: 피부타입 (분석 결과)
- `scores`: 피부 분석 점수 객체
  - `pores`: 모공 점수 (0-100)
  - `blackheads`: 블랙헤드 점수 (0-100)
  - `darkCircles`: 다크서클 점수 (0-100)
  - `porphyrin`: 포르피린 점수 (0-100)
  - `keratin`: 각질 점수 (0-100)
  - `redness`: 홍조 점수 (0-100)

---

#### 5.2 피부분석 보고서 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/skin-analysis-reports/:id`
- **Description**: 특정 피부분석 보고서 상세 정보 조회
- **Authentication**: Not Required

**Path Parameters**:
- `id`: 피부분석 보고서 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
    "userName": "string",
    "userAge": "number",
    "skinAge": "number",
    "skinCondition": "string",
    "skinConditionDescription": "string",
    "skinCode": "string",
    "skinType": "string",
    "analysisScores": {
      "pores": "number",
      "blackheads": "number",
      "pigmentation": "number",
      "wrinkles": "number",
      "porphyrin": "number",
      "sensitivity": "number",
      "darkCircles": "number"
    },
    "skinTags": ["string"],
    "skinCodeDescription": "string",
    "careTips": [
      "string",
      "string",
      "string"
    ],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `userId`: 보고서 작성자 ID
- `userName`: 보고서 작성자 이름
- `userAge`: 사용자 나이 (숫자)
- `skinAge`: 피부나이 (숫자)
- `skinCondition`: 피부상태 (예: "중성" 등)
- `skinConditionDescription`: 피부 상태에 따른 설명 (예: "건강한 상태" 등)
- `skinCode`: 피부코드 (예: "DS" 등)
- `skinType`: 피부타입 (예: "건성 민감성" 등)
- `analysisScores`: 피부상태 분석 점수 객체 (0-100)
  - `pores`: 모공 점수
  - `blackheads`: 블랙헤드 점수
  - `pigmentation`: 색소스팟 점수
  - `wrinkles`: 주름 점수
  - `porphyrin`: 포르피린 점수
  - `sensitivity`: 민감도 점수
  - `darkCircles`: 다크서클 점수
- `skinTags`: 피부 태그 배열 (예: ["건성피부", "민감피부", "피부장벽강화"] 등)
- `skinCodeDescription`: 피부코드에 따른 설명요약
- `careTips`: 피부코드에 따른 피부관리 팁 배열 (3개)

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "피부분석 보고서를 찾을 수 없습니다."
  }
}
```

---

### 6. 퍼스널컬러 관련 API

#### 6.1 퍼스널컬러 색상 조회
- **Method**: `GET`
- **URL**: `/api/v1/personal-colors/colors`
- **Description**: 퍼스널컬러 색상 목록 조회
- **Authentication**: Not Required

**Response 200**:
```json
{
  "success": true,
  "data": {
    "colors": [
      {
        "id": "number",
        "name": "string",
        "hexCode": "string",
        "category": "string"
      }
    ]
  }
}
```

**필드 설명**:
- `id`: 색상 ID
- `name`: 색상명
- `hexCode`: 색상 HEX 코드 (예: "#FF5733")
- `category`: 색상 카테고리 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 등)

---

#### 6.2 퍼스널컬러 상세 진단 조회
- **Method**: `GET`
- **URL**: `/api/v1/personal-colors/:id`
- **Description**: 퍼스널컬러 상세 진단 정보 조회
- **Authentication**: Not Required

**Path Parameters**:
- `id`: 퍼스널컬러 진단 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
    "personalColor": "string",
    "typePercentage": "number",
    "typeDescriptions": [
      "string",
      "string",
      "string",
      "string"
    ],
    "labValues": {
      "l": "number",
      "a": "number",
      "b": "number"
    },
    "matchingColors": {
      "title": "string",
      "description": "string",
      "colors": [
        {
          "id": "number",
          "name": "string",
          "hexCode": "string"
        }
      ]
    },
    "nonMatchingColors": {
      "title": "string",
      "description": "string",
      "colors": [
        {
          "id": "number",
          "name": "string",
          "hexCode": "string"
        }
      ]
    },
    "contouringGuideUrl": "string",
    "makeupTips": [
      "string",
      "string",
      "string"
    ],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `id`: 퍼스널컬러 진단 ID
- `userId`: 사용자 ID
- `personalColor`: 퍼스널컬러 타입 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 등)
- `typePercentage`: 해당 타입일 확률 (퍼센트, 0-100)
- `typeDescriptions`: 타입에 대한 설명 배열 (4가지)
- `labValues`: LAB 색공간 분석값 객체
  - `l`: 명도 (Lightness, 0-100)
  - `a`: 적-녹축 (Red-Green axis, -128 ~ 127)
  - `b`: 황-청축 (Yellow-Blue axis, -128 ~ 127)
- `matchingColors`: 어울리는 색상 객체
  - `title`: 어울리는 색상 제목
  - `description`: 어울리는 색상 설명
  - `colors`: 어울리는 색상 배열 (5개)
    - `id`: 색상 ID
    - `name`: 색상명
    - `hexCode`: 색상 HEX 코드 (예: "#FF5733")
- `nonMatchingColors`: 안 어울리는 색상 객체
  - `title`: 안 어울리는 색상 제목
  - `description`: 안 어울리는 색상 설명
  - `colors`: 안 어울리는 색상 배열 (5개)
    - `id`: 색상 ID
    - `name`: 색상명
    - `hexCode`: 색상 HEX 코드 (예: "#FF5733")
- `contouringGuideUrl`: 컨투어링 가이드 사진 URL (S3 URL)
- `makeupTips`: 화장팁 배열 (3개)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "퍼스널컬러 진단 정보를 찾을 수 없습니다."
  }
}
```

---

### 7. 제품 관련 API

#### 7.1 피부코드 기반 제품 추천 조회
- **Method**: `GET`
- **URL**: `/api/v1/products/recommended-by-skin-code`
- **Description**: 피부코드에 따른 제품 추천 목록 조회
- **Authentication**: Not Required

**Query Parameters**:
- `skinCode` (required): 피부코드 (예: "DS", "DW" 등)
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "products": [
      {
        "id": "number",
        "name": "string",
        "brand": "string",
        "price": "number",
        "imageUrl": "string",
        "url": "string",
        "isWished": "boolean"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `id`: 제품 ID
- `name`: 제품명
- `brand`: 제품 회사 (브랜드명)
- `price`: 가격 (원)
- `imageUrl`: 제품 사진 URL
- `url`: 제품 URL (상세 페이지 링크)
- `isWished`: 좋아요 여부 (인증된 사용자의 경우만, 비인증 시 false)

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "피부코드는 필수입니다."
  }
}
```

---

#### 7.2 퍼스널컬러 기반 제품 추천 조회
- **Method**: `GET`
- **URL**: `/api/v1/products/recommended-by-personal-color`
- **Description**: 퍼스널컬러에 따른 제품 추천 목록 조회
- **Authentication**: Not Required

**Query Parameters**:
- `personalColor` (required): 퍼스널컬러 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 등)
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "products": [
      {
        "id": "number",
        "name": "string",
        "brand": "string",
        "price": "number",
        "imageUrl": "string",
        "url": "string",
        "isWished": "boolean"
      }
    ],
    "pagination": {
      "page": "number",
      "limit": "number",
      "total": "number",
      "totalPages": "number"
    }
  }
}
```

**필드 설명**:
- `id`: 제품 ID
- `name`: 제품명
- `brand`: 제품 회사 (브랜드명)
- `price`: 가격 (원)
- `imageUrl`: 제품 사진 URL
- `url`: 제품 URL (상세 페이지 링크)
- `isWished`: 좋아요 여부 (인증된 사용자의 경우만, 비인증 시 false)

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "퍼스널컬러는 필수입니다."
  }
}
```

---

#### 7.3 제품 찜 추가/취소
- **Method**: `PUT`
- **URL**: `/api/v1/products/:id/wish`
- **Description**: 제품 찜 추가 또는 취소
- **Authentication**: Required

**Path Parameters**:
- `id`: 제품 ID

**Request Body**:
```json
{
  "isWished": "boolean"
}
```

**필드 설명**:
- `isWished`: 찜 여부 (true: 찜 추가, false: 찜 취소)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "productId": "number",
    "isWished": "boolean",
    "message": "string"
  }
}
```

**필드 설명**:
- `productId`: 제품 ID
- `isWished`: 현재 찜 여부
- `message`: 성공 메시지 ("제품이 찜 목록에 추가되었습니다." 또는 "제품이 찜 목록에서 제거되었습니다.")

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "제품을 찾을 수 없습니다."
  }
}
```

---

> **참고**: 관리자 API는 별도 파일 [`admin-API.md`](./admin-API.md)에서 확인하실 수 있습니다.

---

## 에러 코드

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `VALIDATION_ERROR` | 400 | 요청 데이터 검증 실패 |
| `UNAUTHORIZED` | 401 | 인증 실패 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `NOT_FOUND` | 404 | 리소스를 찾을 수 없음 |
| `CONFLICT` | 409 | 리소스 충돌 (예: 중복 이메일) |
| `PROFANITY_DETECTED` | 400 | 비속어 감지됨 |
| `INTERNAL_ERROR` | 500 | 서버 내부 오류 |

---

## 공통 응답 형식

### 성공 응답
```json
{
  "success": true,
  "data": { ... }
}
```

### 실패 응답
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "에러 메시지"
  }
}
```