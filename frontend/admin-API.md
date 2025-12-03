# 관리자 API 명세서

## 기본 정보
- **Base URL**: `http://localhost:18080/api`
- **API Version**: `v1`
- **Content-Type**: `application/json`

## 인증
- **방식**: Bearer Token (JWT) + Admin Role
- **Header**: `Authorization: Bearer {token}`
- **주의사항**: 모든 관리자 API는 관리자 권한이 필요합니다.
- **Base URL**: `/api/v1/admin`

---

## 엔드포인트 목록

### 1. 사용자 관리

#### 1.1 사용자 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/users`
- **Description**: 전체 사용자 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (이메일, 이름)
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (가입일시)
  - `updatedAt`: 수정일시
  - `name`: 이름 (가나다순)
  - `email`: 이메일 (알파벳순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "users": [
      {
        "id": "number",
        "email": "string",
        "name": "string",
        "phone": "string",
        "birthDate": "date",
        "gender": "string",
        "profileImageUrl": "string",
        "provider": "string",
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

#### 1.2 사용자 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/users/:id`
- **Description**: 특정 사용자 상세 정보 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 사용자 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "date",
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

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  }
}
```

---

#### 1.3 사용자 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/users`
- **Description**: 관리자가 사용자 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "email": "string",
  "password": "string",
  "name": "string",
  "phone": "string",
  "birthDate": "date",
  "gender": "string"
}
```

**필드 설명**:
- `email`: 이메일 주소 (필수)
- `password`: 비밀번호 (필수, 최소 8자 이상)
- `name`: 이름 (필수)
- `phone`: 휴대폰 번호 (필수, 형식: 010-1234-5678 또는 01012345678)
- `birthDate`: 생년월일 (필수, 형식: YYYY-MM-DD)
- `gender`: 성별 (필수, "male", "female", "other")

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "date",
    "gender": "string",
    "provider": "string",
    "createdAt": "datetime"
  }
}
```

**필드 설명**:
- `provider`: 로그인 제공자 (기본값: "local")

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "이메일 형식이 올바르지 않습니다." // 또는 "비밀번호는 최소 8자 이상이어야 합니다.", "휴대폰 번호 형식이 올바르지 않습니다." 등
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

#### 1.4 사용자 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/users/:id`
- **Description**: 관리자가 사용자 정보 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 사용자 ID

**Request Body**:
```json
{
  "email": "string",
  "name": "string",
  "phone": "string",
  "birthDate": "date",
  "gender": "string"
}
```

**필드 설명**:
- `email`: 이메일 주소 (선택)
- `name`: 이름 (선택)
- `phone`: 휴대폰 번호 (선택, 형식: 010-1234-5678 또는 01012345678)
- `birthDate`: 생년월일 (선택, 형식: YYYY-MM-DD)
- `gender`: 성별 (선택, "male", "female", "other")

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `email`과 `phone`은 중복 체크가 필요합니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "email": "string",
    "name": "string",
    "phone": "string",
    "birthDate": "date",
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

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
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

#### 1.5 사용자 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/users/:id`
- **Description**: 관리자가 사용자 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 사용자 ID

**주의사항**:
- 사용자 삭제 시 관련 데이터는 정책에 따라 삭제되거나 보관됩니다.
- 프로필 이미지가 있으면 S3에서 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "사용자가 삭제되었습니다."
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  }
}
```

---

### 2. 게시글 관리

#### 2.1 게시글 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/posts`
- **Description**: 관리자가 전체 게시글 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어
- `authorId` (optional): 작성자 ID로 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
  - `title`: 제목 (가나다순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

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
        "authorEmail": "string",
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
- `id`: 게시글 ID
- `title`: 제목
- `content`: 내용
- `authorId`: 작성자 ID
- `authorName`: 작성자 이름
- `authorEmail`: 작성자 이메일
- `hashtags`: 해시태그 배열 (없으면 빈 배열)
- `images`: 이미지 URL 배열 (S3 URL, 없으면 빈 배열)
- `commentCount`: 댓글 수
- `likeCount`: 좋아요 수
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 2.2 게시글 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/posts/:id`
- **Description**: 관리자가 게시글 상세 조회
- **Authentication**: Required (Admin)

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
    "authorEmail": "string",
    "hashtags": ["string"],
    "images": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `id`: 게시글 ID
- `title`: 제목
- `content`: 내용
- `authorId`: 작성자 ID
- `authorName`: 작성자 이름
- `authorEmail`: 작성자 이메일
- `hashtags`: 해시태그 배열 (없으면 빈 배열)
- `images`: 이미지 URL 배열 (S3 URL, 없으면 빈 배열)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

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

#### 2.3 게시글 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/posts`
- **Description**: 관리자가 게시글 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "authorId": "number",
  "hashtags": ["string"],
  "imageUrls": ["string"]
}
```

**필드 설명**:
- `title`: 제목 (필수)
- `content`: 내용 (필수)
- `authorId`: 작성자 ID (필수)
- `hashtags`: 해시태그 배열 (선택, 예: ["뷰티", "메이크업", "리뷰"])
- `imageUrls`: 이미지 URL 배열 (선택, S3 URL들)

**주의사항**:
- 관리자가 생성하는 게시글은 비속어 검사를 건너뜁니다.
- `imageUrls`는 S3에 이미 업로드된 URL이어야 합니다.

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

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "제목은 필수입니다." // 또는 "내용은 필수입니다.", "작성자 ID는 필수입니다." 등
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "작성자를 찾을 수 없습니다."
  }
}
```

---

#### 2.4 게시글 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/posts/:id`
- **Description**: 관리자가 게시글 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 게시글 ID

**Request Body**:
```json
{
  "title": "string",
  "content": "string",
  "hashtags": ["string"],
  "imageUrls": ["string"]
}
```

**필드 설명**:
- `title`: 제목 (선택)
- `content`: 내용 (선택)
- `hashtags`: 해시태그 배열 (선택, 전송된 배열로 교체)
- `imageUrls`: 이미지 URL 배열 (선택, 전송된 배열로 교체, 기존 이미지는 S3에서 삭제)

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `hashtags`와 `imageUrls`는 빈 배열로 전송하면 모두 삭제됩니다.
- 관리자가 수정하는 게시글은 비속어 검사를 건너뜁니다.

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

#### 2.5 게시글 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/posts/:id`
- **Description**: 관리자가 게시글 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 게시글 ID

**주의사항**:
- 게시글 삭제 시 관련 이미지는 S3에서 삭제됩니다.
- 관련 댓글도 함께 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "게시글이 삭제되었습니다."
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

### 3. 댓글 관리

#### 3.1 댓글 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/comments`
- **Description**: 관리자가 전체 댓글 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (댓글 내용, 작성자 이름, 작성자 이메일)
- `postId` (optional): 게시글 ID로 필터링
- `userId` (optional): 사용자 ID로 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "comments": [
      {
        "id": "number",
        "postId": "number",
        "postTitle": "string",
        "userId": "number",
        "userName": "string",
        "userEmail": "string",
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
- `id`: 댓글 ID
- `postId`: 게시글 ID
- `postTitle`: 게시글 제목
- `userId`: 댓글 작성자 ID
- `userName`: 댓글 작성자 이름
- `userEmail`: 댓글 작성자 이메일
- `content`: 댓글 내용
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 3.2 댓글 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/comments/:id`
- **Description**: 관리자가 댓글 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 댓글 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "postId": "number",
    "postTitle": "string",
    "userId": "number",
    "userName": "string",
    "userEmail": "string",
    "content": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `id`: 댓글 ID
- `postId`: 게시글 ID
- `postTitle`: 게시글 제목
- `userId`: 댓글 작성자 ID
- `userName`: 댓글 작성자 이름
- `userEmail`: 댓글 작성자 이메일
- `content`: 댓글 내용
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "댓글을 찾을 수 없습니다."
  }
}
```

---

#### 3.3 댓글 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/comments`
- **Description**: 관리자가 댓글 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "postId": "number",
  "userId": "number",
  "content": "string"
}
```

**필드 설명**:
- `postId`: 게시글 ID (필수)
- `userId`: 사용자 ID (필수)
- `content`: 댓글 내용 (필수)

**주의사항**:
- 관리자가 생성하는 댓글은 비속어 검사를 건너뜁니다.

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "postId": "number",
    "userId": "number",
    "content": "string",
    "createdAt": "datetime",
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
    "message": "게시글 ID는 필수입니다." // 또는 "사용자 ID는 필수입니다.", "댓글 내용은 필수입니다." 등
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "게시글을 찾을 수 없습니다." // 또는 "사용자를 찾을 수 없습니다."
  }
}
```

---

#### 3.4 댓글 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/comments/:id`
- **Description**: 관리자가 댓글 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 댓글 ID

**Request Body**:
```json
{
  "content": "string"
}
```

**필드 설명**:
- `content`: 댓글 내용 (필수)

**주의사항**:
- 관리자가 수정하는 댓글은 비속어 검사를 건너뜁니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "postId": "number",
    "userId": "number",
    "content": "string",
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
    "message": "댓글 내용은 필수입니다."
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "댓글을 찾을 수 없습니다."
  }
}
```

---

#### 3.5 댓글 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/comments/:id`
- **Description**: 관리자가 댓글 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 댓글 ID

**Response 200**:
```json
{
  "success": true,
  "message": "댓글이 삭제되었습니다."
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "댓글을 찾을 수 없습니다."
  }
}
```

---

### 4. 공지/이벤트 관리

#### 4.1 공지/이벤트 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/notices`
- **Description**: 관리자가 공지/이벤트 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (제목, 내용)
- `type` (optional): `notice` 또는 `event` 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
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
- `id`: 공지/이벤트 ID
- `type`: 타입 (notice: 공지, event: 이벤트)
- `title`: 제목
- `content`: 내용
- `startDate`: 시작일시 (이벤트의 경우 필수, 공지의 경우 null)
- `endDate`: 종료일시 (이벤트의 경우 필수, 공지의 경우 null)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 4.2 공지/이벤트 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/notices/:id`
- **Description**: 관리자가 공지/이벤트 상세 조회
- **Authentication**: Required (Admin)

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
- `id`: 공지/이벤트 ID
- `type`: 타입 (notice: 공지, event: 이벤트)
- `title`: 제목
- `content`: 내용
- `images`: 이미지 URL 배열 (S3 URL, 없으면 빈 배열)
- `links`: 링크 URL 배열 (없으면 빈 배열)
- `startDate`: 시작일시 (이벤트의 경우 필수, 공지의 경우 null)
- `endDate`: 종료일시 (이벤트의 경우 필수, 공지의 경우 null)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

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

#### 4.3 공지/이벤트 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/notices`
- **Description**: 관리자가 공지/이벤트 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "type": "string",
  "title": "string",
  "content": "string",
  "startDate": "datetime",
  "endDate": "datetime",
  "imageUrls": ["string"],
  "links": ["string"]
}
```

**필드 설명**:
- `type`: 타입 (필수, "notice" 또는 "event")
- `title`: 제목 (필수)
- `content`: 내용 (필수)
- `startDate`: 시작일시 (선택, 이벤트의 경우 권장, 공지의 경우 null)
- `endDate`: 종료일시 (선택, 이벤트의 경우 권장, 공지의 경우 null)
- `imageUrls`: 이미지 URL 배열 (선택, S3 URL들)
- `links`: 링크 URL 배열 (선택)

**주의사항**:
- `imageUrls`는 S3에 이미 업로드된 URL이어야 합니다.
- 이벤트의 경우 `startDate`와 `endDate`를 설정하는 것을 권장합니다.

**Response 201**:
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

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "타입은 필수입니다." // 또는 "제목은 필수입니다.", "내용은 필수입니다." 등
  }
}
```

---

#### 4.4 공지/이벤트 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/notices/:id`
- **Description**: 관리자가 공지/이벤트 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 공지/이벤트 ID

**Request Body**:
```json
{
  "type": "string",
  "title": "string",
  "content": "string",
  "startDate": "datetime",
  "endDate": "datetime",
  "imageUrls": ["string"],
  "links": ["string"]
}
```

**필드 설명**:
- `type`: 타입 (선택, "notice" 또는 "event")
- `title`: 제목 (선택)
- `content`: 내용 (선택)
- `startDate`: 시작일시 (선택)
- `endDate`: 종료일시 (선택)
- `imageUrls`: 이미지 URL 배열 (선택, 전송된 배열로 교체, 기존 이미지는 S3에서 삭제)
- `links`: 링크 URL 배열 (선택, 전송된 배열로 교체)

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `imageUrls`와 `links`는 빈 배열로 전송하면 모두 삭제됩니다.

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
    "message": "공지/이벤트를 찾을 수 없습니다."
  }
}
```

---

#### 4.5 공지/이벤트 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/notices/:id`
- **Description**: 관리자가 공지/이벤트 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 공지/이벤트 ID

**주의사항**:
- 공지/이벤트 삭제 시 관련 이미지는 S3에서 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "공지/이벤트가 삭제되었습니다."
}
```

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

### 5. 배너 관리

#### 5.1 배너 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/banners`
- **Description**: 관리자가 배너 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (제목, 설명)
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
  - `title`: 제목 (가나다순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

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
- `id`: 배너 ID
- `title`: 배너 제목
- `description`: 배너 설명
- `imageUrl`: 배너 이미지 URL (S3 URL)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 5.2 배너 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/banners/:id`
- **Description**: 관리자가 배너 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 배너 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "description": "string",
    "imageUrl": "string",
    "createdAt": "datetime",
    "updatedAt": "datetime"
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

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "배너를 찾을 수 없습니다."
  }
}
```

---

#### 5.3 배너 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/banners`
- **Description**: 관리자가 배너 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "title": "string",
  "description": "string",
  "imageUrl": "string"
}
```

**필드 설명**:
- `title`: 배너 제목 (필수)
- `description`: 배너 설명 (필수)
- `imageUrl`: 배너 이미지 URL (필수, S3 URL)

**주의사항**:
- `imageUrl`은 S3에 이미 업로드된 URL이어야 합니다.

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "description": "string",
    "imageUrl": "string",
    "createdAt": "datetime",
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
    "message": "제목은 필수입니다." // 또는 "설명은 필수입니다.", "이미지 URL은 필수입니다." 등
  }
}
```

---

#### 5.4 배너 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/banners/:id`
- **Description**: 관리자가 배너 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 배너 ID

**Request Body**:
```json
{
  "title": "string",
  "description": "string",
  "imageUrl": "string"
}
```

**필드 설명**:
- `title`: 배너 제목 (선택)
- `description`: 배너 설명 (선택)
- `imageUrl`: 배너 이미지 URL (선택, S3 URL)

**주의사항**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `imageUrl` 변경 시 기존 이미지는 S3에서 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "title": "string",
    "description": "string",
    "imageUrl": "string",
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
    "message": "배너를 찾을 수 없습니다."
  }
}
```

---

#### 5.5 배너 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/banners/:id`
- **Description**: 관리자가 배너 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 배너 ID

**주의사항**:
- 배너 삭제 시 관련 이미지는 S3에서 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "배너가 삭제되었습니다."
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "배너를 찾을 수 없습니다."
  }
}
```

---

### 6. 피부분석 보고서 관리

#### 6.1 피부분석 보고서 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/skin-analysis-reports`
- **Description**: 관리자가 피부분석 보고서 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (사용자 이름, 사용자 이메일, 피부코드)
- `userId` (optional): 사용자 ID로 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

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
        "userEmail": "string",
        "skinAge": "number",
        "skinType": "string",
        "skinCode": "string",
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
- `id`: 피부분석 보고서 ID
- `userId`: 보고서 작성자 ID
- `userName`: 보고서 작성자 이름
- `userEmail`: 보고서 작성자 이메일
- `skinAge`: 피부나이 (숫자)
- `skinType`: 피부타입 (분석 결과)
- `skinCode`: 피부코드 (예: "DS" 등)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 6.2 피부분석 보고서 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/skin-analysis-reports/:id`
- **Description**: 관리자가 피부분석 보고서 상세 조회
- **Authentication**: Required (Admin)

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
    "userEmail": "string",
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
    "careTips": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- `id`: 피부분석 보고서 ID
- `userId`: 보고서 작성자 ID
- `userName`: 보고서 작성자 이름
- `userEmail`: 보고서 작성자 이메일
- `userAge`: 사용자 나이 (숫자)
- `skinAge`: 피부나이 (숫자)
- `skinCondition`: 피부상태 (예: "중성" 등)
- `skinConditionDescription`: 피부 상태에 따른 설명
- `skinCode`: 피부코드 (예: "DS" 등)
- `skinType`: 피부타입 (예: "건성 민감성" 등)
- `analysisScores`: 피부상태 분석 점수 객체 (0-100)
- `skinTags`: 피부 태그 배열
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

#### 6.3 피부분석 보고서 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/skin-analysis-reports`
- **Description**: 관리자가 피부분석 보고서 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "userId": "number",
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
  "careTips": ["string"]
}
```

**필드 설명**:
- `userId`: 사용자 ID (필수)
- `userAge`: 사용자 나이 (필수, 숫자)
- `skinAge`: 피부나이 (필수, 숫자)
- `skinCondition`: 피부상태 (필수, 예: "중성" 등)
- `skinConditionDescription`: 피부 상태에 따른 설명 (필수)
- `skinCode`: 피부코드 (필수, 예: "DS" 등)
- `skinType`: 피부타입 (필수, 예: "건성 민감성" 등)
- `analysisScores`: 피부상태 분석 점수 객체 (필수, 0-100)
- `skinTags`: 피부 태그 배열 (선택)
- `skinCodeDescription`: 피부코드에 따른 설명요약 (필수)
- `careTips`: 피부코드에 따른 피부관리 팁 배열 (필수, 3개)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
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
    "careTips": ["string"],
    "createdAt": "datetime",
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
    "message": "사용자 ID는 필수입니다." // 또는 다른 필수 필드 오류
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다."
  }
}
```

---

#### 6.4 피부분석 보고서 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/skin-analysis-reports/:id`
- **Description**: 관리자가 피부분석 보고서 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 피부분석 보고서 ID

**Request Body**:
```json
{
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
  "careTips": ["string"]
}
```

**필드 설명**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
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
    "careTips": ["string"],
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
    "message": "피부분석 보고서를 찾을 수 없습니다."
  }
}
```

---

#### 6.5 피부분석 보고서 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/skin-analysis-reports/:id`
- **Description**: 관리자가 피부분석 보고서 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 피부분석 보고서 ID

**Response 200**:
```json
{
  "success": true,
  "message": "피부분석 보고서가 삭제되었습니다."
}
```

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

### 7. 퍼스널컬러 관리

#### 7.1 퍼스널컬러 색상 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/personal-colors/colors`
- **Description**: 관리자가 퍼스널컬러 색상 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `category` (optional): 카테고리 필터링 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤")
- `sortBy` (optional): 정렬 기준 (기본값: "id")
  - `id`: 색상 ID
  - `name`: 색상명 (가나다순)
  - `category`: 카테고리
- `order` (optional): 정렬 방향 (기본값: "asc")
  - `asc`: 오름차순
  - `desc`: 내림차순

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
        "category": "string",
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
- `id`: 색상 ID
- `name`: 색상명
- `hexCode`: 색상 HEX 코드 (예: "#FF5733")
- `category`: 색상 카테고리 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 등)

---

#### 7.2 퍼스널컬러 색상 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/personal-colors/colors/:id`
- **Description**: 관리자가 퍼스널컬러 색상 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 색상 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "hexCode": "string",
    "category": "string",
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
    "message": "색상을 찾을 수 없습니다."
  }
}
```

---

#### 7.3 퍼스널컬러 색상 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/personal-colors/colors`
- **Description**: 관리자가 퍼스널컬러 색상 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "name": "string",
  "hexCode": "string",
  "category": "string"
}
```

**필드 설명**:
- `name`: 색상명 (필수)
- `hexCode`: 색상 HEX 코드 (필수, 예: "#FF5733")
- `category`: 색상 카테고리 (필수, "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 중 하나)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "hexCode": "string",
    "category": "string",
    "createdAt": "datetime",
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
    "message": "색상명은 필수입니다." // 또는 "HEX 코드 형식이 올바르지 않습니다.", "카테고리는 필수입니다." 등
  }
}
```

---

#### 7.4 퍼스널컬러 색상 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/personal-colors/colors/:id`
- **Description**: 관리자가 퍼스널컬러 색상 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 색상 ID

**Request Body**:
```json
{
  "name": "string",
  "hexCode": "string",
  "category": "string"
}
```

**필드 설명**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "hexCode": "string",
    "category": "string",
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
    "message": "색상을 찾을 수 없습니다."
  }
}
```

---

#### 7.5 퍼스널컬러 색상 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/personal-colors/colors/:id`
- **Description**: 관리자가 퍼스널컬러 색상 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 색상 ID

**주의사항**:
- 다른 진단에서 사용 중인 색상은 삭제할 수 없습니다.

**Response 200**:
```json
{
  "success": true,
  "message": "색상이 삭제되었습니다."
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "색상을 찾을 수 없습니다."
  }
}
```

**Error Response 409**:
```json
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "사용 중인 색상은 삭제할 수 없습니다."
  }
}
```

---

#### 7.6 퍼스널컬러 진단 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/personal-colors`
- **Description**: 관리자가 퍼스널컬러 진단 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (사용자 이름, 사용자 이메일, 퍼스널컬러 타입)
- `userId` (optional): 사용자 ID로 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "diagnoses": [
      {
        "id": "number",
        "userId": "number",
        "userName": "string",
        "userEmail": "string",
        "personalColor": "string",
        "typePercentage": "number",
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
- `id`: 퍼스널컬러 진단 ID
- `userId`: 사용자 ID
- `userName`: 사용자 이름
- `userEmail`: 사용자 이메일
- `personalColor`: 퍼스널컬러 타입 (예: "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 등)
- `typePercentage`: 해당 타입일 확률 (퍼센트, 0-100)

---

#### 7.7 퍼스널컬러 진단 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/personal-colors/:id`
- **Description**: 관리자가 퍼스널컬러 진단 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 퍼스널컬러 진단 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
    "userName": "string",
    "userEmail": "string",
    "personalColor": "string",
    "typePercentage": "number",
    "typeDescriptions": ["string"],
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
    "makeupTips": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
  }
}
```

**필드 설명**:
- 일반 API의 퍼스널컬러 상세 진단 조회와 동일한 구조

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

#### 7.8 퍼스널컬러 진단 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/personal-colors`
- **Description**: 관리자가 퍼스널컬러 진단 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "userId": "number",
  "personalColor": "string",
  "typePercentage": "number",
  "typeDescriptions": ["string"],
  "labValues": {
    "l": "number",
    "a": "number",
    "b": "number"
  },
  "matchingColors": {
    "title": "string",
    "description": "string",
    "colorIds": ["number"]
  },
  "nonMatchingColors": {
    "title": "string",
    "description": "string",
    "colorIds": ["number"]
  },
  "contouringGuideUrl": "string",
  "makeupTips": ["string"]
}
```

**필드 설명**:
- `userId`: 사용자 ID (필수)
- `personalColor`: 퍼스널컬러 타입 (필수, "봄웜톤", "여름쿨톤", "가을웜톤", "겨울쿨톤" 중 하나)
- `typePercentage`: 해당 타입일 확률 (필수, 0-100)
- `typeDescriptions`: 타입에 대한 설명 배열 (필수, 4가지)
- `labValues`: LAB 색공간 분석값 객체 (필수)
- `matchingColors`: 어울리는 색상 객체 (필수)
  - `title`: 어울리는 색상 제목 (필수)
  - `description`: 어울리는 색상 설명 (필수)
  - `colorIds`: 어울리는 색상 ID 배열 (필수, 5개)
- `nonMatchingColors`: 안 어울리는 색상 객체 (필수)
  - `title`: 안 어울리는 색상 제목 (필수)
  - `description`: 안 어울리는 색상 설명 (필수)
  - `colorIds`: 안 어울리는 색상 ID 배열 (필수, 5개)
- `contouringGuideUrl`: 컨투어링 가이드 사진 URL (선택, S3 URL)
- `makeupTips`: 화장팁 배열 (필수, 3개)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
    "personalColor": "string",
    "typePercentage": "number",
    "typeDescriptions": ["string"],
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
    "makeupTips": ["string"],
    "createdAt": "datetime",
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
    "message": "사용자 ID는 필수입니다." // 또는 다른 필수 필드 오류
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "사용자를 찾을 수 없습니다." // 또는 "색상을 찾을 수 없습니다."
  }
}
```

---

#### 7.9 퍼스널컬러 진단 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/personal-colors/:id`
- **Description**: 관리자가 퍼스널컬러 진단 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 퍼스널컬러 진단 ID

**Request Body**:
```json
{
  "personalColor": "string",
  "typePercentage": "number",
  "typeDescriptions": ["string"],
  "labValues": {
    "l": "number",
    "a": "number",
    "b": "number"
  },
  "matchingColors": {
    "title": "string",
    "description": "string",
    "colorIds": ["number"]
  },
  "nonMatchingColors": {
    "title": "string",
    "description": "string",
    "colorIds": ["number"]
  },
  "contouringGuideUrl": "string",
  "makeupTips": ["string"]
}
```

**필드 설명**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "userId": "number",
    "personalColor": "string",
    "typePercentage": "number",
    "typeDescriptions": ["string"],
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
    "makeupTips": ["string"],
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
    "message": "퍼스널컬러 진단 정보를 찾을 수 없습니다."
  }
}
```

---

#### 7.10 퍼스널컬러 진단 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/personal-colors/:id`
- **Description**: 관리자가 퍼스널컬러 진단 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 퍼스널컬러 진단 ID

**주의사항**:
- 진단 삭제 시 관련 컨투어링 가이드 이미지는 S3에서 삭제됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "퍼스널컬러 진단 정보가 삭제되었습니다."
}
```

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

### 8. 제품 관리

#### 8.1 제품 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/products`
- **Description**: 관리자가 제품 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (제품명, 브랜드명)
- `skinCode` (optional): 피부코드로 필터링
- `personalColor` (optional): 퍼스널컬러로 필터링
- `sortBy` (optional): 정렬 기준 (기본값: "createdAt")
  - `createdAt`: 생성일시 (최신순)
  - `updatedAt`: 수정일시
  - `name`: 제품명 (가나다순)
  - `brand`: 브랜드명 (가나다순)
  - `price`: 가격 (낮은순/높은순)
- `order` (optional): 정렬 방향 (기본값: "desc")
  - `asc`: 오름차순
  - `desc`: 내림차순

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
        "category": "string",
        "tags": ["string"],
        "skinCodes": ["string"],
        "personalColors": ["string"],
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
- `id`: 제품 ID
- `name`: 제품명
- `brand`: 제품 회사 (브랜드명)
- `price`: 가격 (원)
- `imageUrl`: 제품 사진 URL
- `url`: 제품 URL (상세 페이지 링크)
- `category`: 제품 카테고리 (없으면 null)
- `tags`: 제품 태그 배열 (없으면 빈 배열)
- `skinCodes`: 추천 피부코드 배열 (없으면 빈 배열)
- `personalColors`: 추천 퍼스널컬러 배열 (없으면 빈 배열)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 8.2 제품 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/products/:id`
- **Description**: 관리자가 제품 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 제품 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "brand": "string",
    "price": "number",
    "imageUrl": "string",
    "url": "string",
    "category": "string",
    "tags": ["string"],
    "skinCodes": ["string"],
    "personalColors": ["string"],
    "createdAt": "datetime",
    "updatedAt": "datetime"
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
- `category`: 제품 카테고리 (없으면 null)
- `tags`: 제품 태그 배열 (없으면 빈 배열)
- `skinCodes`: 추천 피부코드 배열 (없으면 빈 배열)
- `personalColors`: 추천 퍼스널컬러 배열 (없으면 빈 배열)
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

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

#### 8.3 제품 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/products`
- **Description**: 관리자가 제품 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "name": "string",
  "brand": "string",
  "price": "number",
  "imageUrl": "string",
  "url": "string",
  "category": "string",
  "tags": ["string"],
  "skinCodes": ["string"],
  "personalColors": ["string"]
}
```

**필드 설명**:
- `name`: 제품명 (필수)
- `brand`: 제품 회사 (브랜드명) (필수)
- `price`: 가격 (필수, 원)
- `imageUrl`: 제품 사진 URL (필수)
- `url`: 제품 URL (상세 페이지 링크) (필수)
- `category`: 제품 카테고리 (선택, 예: "스킨케어", "메이크업", "선케어" 등)
- `tags`: 제품 태그 배열 (선택, 예: ["수분", "진정", "미백"])
- `skinCodes`: 추천 피부코드 배열 (선택, 예: ["DS", "DW"] 등)
- `personalColors`: 추천 퍼스널컬러 배열 (선택, 예: ["봄웜톤", "여름쿨톤"] 등)

**주의사항**:
- `skinCodes`와 `personalColors` 중 하나 이상은 필수입니다 (배열이 비어있지 않아야 함).

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "brand": "string",
    "price": "number",
    "imageUrl": "string",
    "url": "string",
    "category": "string",
    "tags": ["string"],
    "skinCodes": ["string"],
    "personalColors": ["string"],
    "createdAt": "datetime",
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
    "message": "제품명은 필수입니다." // 또는 "피부코드 또는 퍼스널컬러 중 하나는 필수입니다." 등
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "이미지를 찾을 수 없습니다." // imageUrl이 images 테이블에 존재하지 않는 경우
  }
}
```

---

#### 8.4 제품 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/products/:id`
- **Description**: 관리자가 제품 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 제품 ID

**Request Body**:
```json
{
  "name": "string",
  "brand": "string",
  "price": "number",
  "imageUrl": "string",
  "url": "string",
  "category": "string",
  "tags": ["string"],
  "skinCodes": ["string"],
  "personalColors": ["string"]
}
```

**필드 설명**:
- 모든 필드는 선택사항이며, 전송된 필드만 수정됩니다.
- `tags`, `skinCodes`, `personalColors` 배열을 전송하면 기존 값이 모두 교체됩니다.

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "name": "string",
    "brand": "string",
    "price": "number",
    "imageUrl": "string",
    "url": "string",
    "category": "string",
    "tags": ["string"],
    "skinCodes": ["string"],
    "personalColors": ["string"],
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
    "message": "제품을 찾을 수 없습니다."
  }
}
```

---

#### 8.5 제품 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/products/:id`
- **Description**: 관리자가 제품 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 제품 ID

**주의사항**:
- 제품 삭제 시 관련 찜 목록에서도 제거됩니다.

**Response 200**:
```json
{
  "success": true,
  "message": "제품이 삭제되었습니다."
}
```

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

### 9. 비속어 관리

#### 9.1 비속어 단어 목록 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/profanity-words`
- **Description**: 관리자가 비속어 단어 목록 조회
- **Authentication**: Required (Admin)

**Query Parameters**:
- `page` (optional): 페이지 번호 (기본값: 1)
- `limit` (optional): 페이지당 항목 수 (기본값: 10)
- `search` (optional): 검색어 (단어 검색)
- `sortBy` (optional): 정렬 기준 (기본값: "word")
  - `word`: 단어 (가나다순)
  - `createdAt`: 생성일시
- `order` (optional): 정렬 방향 (기본값: "asc")
  - `asc`: 오름차순
  - `desc`: 내림차순

**Response 200**:
```json
{
  "success": true,
  "data": {
    "words": [
      {
        "id": "number",
        "word": "string",
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
- `id`: 비속어 단어 ID
- `word`: 비속어 단어
- `createdAt`: 생성일시
- `updatedAt`: 수정일시

---

#### 9.2 비속어 단어 상세 조회
- **Method**: `GET`
- **URL**: `/api/v1/admin/profanity-words/:id`
- **Description**: 관리자가 비속어 단어 상세 조회
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 비속어 단어 ID

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "word": "string",
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
    "message": "비속어 단어를 찾을 수 없습니다."
  }
}
```

---

#### 9.3 비속어 단어 생성
- **Method**: `POST`
- **URL**: `/api/v1/admin/profanity-words`
- **Description**: 관리자가 비속어 단어 생성
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "word": "string"
}
```

**필드 설명**:
- `word`: 비속어 단어 (필수)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "word": "string",
    "createdAt": "datetime",
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
    "message": "비속어 단어는 필수입니다."
  }
}
```

**Error Response 409**:
```json
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "이미 등록된 비속어 단어입니다."
  }
}
```

---

#### 9.4 비속어 단어 수정
- **Method**: `PUT`
- **URL**: `/api/v1/admin/profanity-words/:id`
- **Description**: 관리자가 비속어 단어 수정
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 비속어 단어 ID

**Request Body**:
```json
{
  "word": "string"
}
```

**필드 설명**:
- `word`: 비속어 단어 (필수)

**Response 200**:
```json
{
  "success": true,
  "data": {
    "id": "number",
    "word": "string",
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
    "message": "비속어 단어는 필수입니다."
  }
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "비속어 단어를 찾을 수 없습니다."
  }
}
```

**Error Response 409**:
```json
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "이미 등록된 비속어 단어입니다."
  }
}
```

---

#### 9.5 비속어 단어 삭제
- **Method**: `DELETE`
- **URL**: `/api/v1/admin/profanity-words/:id`
- **Description**: 관리자가 비속어 단어 삭제
- **Authentication**: Required (Admin)

**Path Parameters**:
- `id`: 비속어 단어 ID

**Response 200**:
```json
{
  "success": true,
  "message": "비속어 단어가 삭제되었습니다."
}
```

**Error Response 404**:
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "비속어 단어를 찾을 수 없습니다."
  }
}
```

---

#### 9.6 비속어 단어 일괄 추가
- **Method**: `POST`
- **URL**: `/api/v1/admin/profanity-words/batch`
- **Description**: 관리자가 비속어 단어 일괄 추가
- **Authentication**: Required (Admin)

**Request Body**:
```json
{
  "words": ["string"]
}
```

**필드 설명**:
- `words`: 비속어 단어 배열 (필수)

**Response 201**:
```json
{
  "success": true,
  "data": {
    "created": "number",
    "skipped": "number",
    "total": "number"
  }
}
```

**필드 설명**:
- `created`: 생성된 단어 수
- `skipped`: 건너뛴 단어 수 (이미 존재하는 단어)
- `total`: 요청한 전체 단어 수

**Error Response 400**:
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "비속어 단어 배열은 필수입니다."
  }
}
```

---

## 에러 코드

| 코드 | HTTP 상태 | 설명 |
|------|----------|------|
| `VALIDATION_ERROR` | 400 | 요청 데이터 검증 실패 |
| `UNAUTHORIZED` | 401 | 인증 실패 |
| `FORBIDDEN` | 403 | 권한 없음 |
| `NOT_FOUND` | 404 | 리소스를 찾을 수 없음 |
| `CONFLICT` | 409 | 리소스 충돌 (예: 중복 이메일) |
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
