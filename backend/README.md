# Digilog API Server

Spring Boot 기반 REST API 서버

## 기술 스택

- Java 21
- Spring Boot 3.3.5
- Spring Security
- JWT (Cookie 기반, HttpOnly, Secure)
- Spring Data JPA
- MySQL / H2 (개발용)
- Swagger/OpenAPI 3
- Gradle

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/example/apiserver/
│   │   ├── config/          # 설정 클래스
│   │   ├── security/         # Security 설정
│   │   ├── controller/       # 컨트롤러
│   │   ├── service/          # 서비스
│   │   ├── repository/       # 리포지토리
│   │   ├── entity/           # 엔티티
│   │   ├── dto/              # DTO
│   │   └── util/             # 유틸리티
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       └── application-prod.yml
```

## 환경 설정

### 개발 환경 (dev)

```bash
export SPRING_PROFILES_ACTIVE=dev
```

- H2 인메모리 데이터베이스 사용
- SQL 로그 출력
- 디버그 레벨 로깅

### 프로덕션 환경 (prod)

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://localhost:3306/api_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your-secret-key-minimum-256-bits
```

- MySQL 데이터베이스 사용
- 보안 강화된 Cookie 설정
- 프로덕션 레벨 로깅

## 실행 방법

### 로컬 실행

```bash
# Gradle 빌드
./gradlew build

# 실행
./gradlew bootRun
```

또는

```bash
java -jar build/libs/digilog-1.0.0-SNAPSHOT.jar
```

## API 문서

서버 실행 후 다음 URL에서 Swagger UI 확인:

- http://localhost:18080/api/swagger-ui.html
- http://localhost:18080/api/v3/api-docs

## Health Check

- http://localhost:18080/api/actuator/health
- http://localhost:18080/api/actuator/info

## 인증

### JWT 토큰

- **Access Token**: Cookie에 저장 (HttpOnly, Secure)
- **Refresh Token**: Cookie에 저장 (HttpOnly, Secure)
- **Bearer Token**: Authorization Header에서도 지원

### 인증이 필요 없는 경로

- `/api/v1/auth/**` - 인증 관련
- `/api/v1/notices/**` - 공지사항
- `/api/v1/banners/**` - 배너
- `/api/v1/posts/**` - 게시글 (조회)
- `/api/v1/skin-analysis-reports/**` - 피부분석 보고서
- `/api/v1/personal-colors/**` - 퍼스널컬러
- `/api/v1/products/**` - 제품
- `/api/swagger-ui/**` - Swagger UI
- `/api/actuator/health` - Health Check

### 인증이 필요한 경로

- `/api/v1/users/me/**` - 사용자 정보
- `/api/v1/posts` (POST, PUT, DELETE) - 게시글 작성/수정/삭제
- 기타 사용자별 리소스

### 관리자 전용 경로

- `/api/v1/admin/**` - 관리자 API

## Cookie 설정

- **HttpOnly**: true (JavaScript 접근 불가)
- **Secure**: 
  - dev: false
  - prod: true (HTTPS만)
- **SameSite**: 
  - dev: Lax
  - prod: None (CORS 환경)

## 포트

- 기본 포트: 18080
- Context Path: `/api`

## 데이터베이스

### 개발 환경
- H2 인메모리 데이터베이스
- 자동 DDL 생성

### 프로덕션 환경
- MySQL 8.0
- 환경변수로 설정

## 환경 변수

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| SPRING_PROFILES_ACTIVE | 활성 프로파일 | dev |
| DB_URL | 데이터베이스 URL | jdbc:h2:mem:testdb |
| DB_USERNAME | 데이터베이스 사용자명 | sa |
| DB_PASSWORD | 데이터베이스 비밀번호 | - |
| JWT_SECRET | JWT 시크릿 키 | - |
| JWT_EXPIRATION | Access Token 만료 시간 (ms) | 86400000 (24시간) |
| JWT_REFRESH_EXPIRATION | Refresh Token 만료 시간 (ms) | 604800000 (7일) |
| COOKIE_SECURE | Cookie Secure 플래그 | false |
| CORS_ORIGINS | CORS 허용 Origin | http://localhost:3000,http://localhost:5173 |

