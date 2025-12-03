# Digilog Frontend

Vue 3 + TypeScript 기반 프론트엔드 프로젝트

## 기술 스택

- **Vue 3** (Composition API)
- **TypeScript**
- **Vite** (빌드 도구)
- **Pinia** (상태 관리)
- **Vue Router** (라우팅)
- **Tailwind CSS** (스타일링)
- **Axios** (HTTP 클라이언트)
- **VueUse** (유틸리티 컴포저블)
- **Playwright** (E2E 테스트 및 시각적 회귀 테스트)

## 프로젝트 구조

```
frontend/
├── public/                 # 정적 파일
├── src/
│   ├── api/               # API 클라이언트
│   │   ├── client.ts      # Axios 인스턴스 설정
│   │   ├── auth.api.ts    # 인증 관련 API
│   │   ├── user.api.ts    # 사용자 관련 API
│   │   ├── post.api.ts    # 게시글 관련 API
│   │   ├── comment.api.ts # 댓글 관련 API
│   │   ├── notice.api.ts  # 공지/이벤트 관련 API
│   │   ├── banner.api.ts  # 배너 관련 API
│   │   ├── report.api.ts  # 피부분석 보고서 관련 API
│   │   ├── personal-color.api.ts # 퍼스널컬러 관련 API
│   │   ├── product.api.ts # 제품 관련 API
│   │   └── admin.api.ts   # 관리자 API
│   │
│   ├── assets/            # 정적 자산
│   │   ├── images/
│   │   ├── icons/
│   │   └── fonts/
│   │
│   ├── components/        # 공통 컴포넌트
│   │   ├── common/        # 범용 컴포넌트
│   │   │   ├── Button/
│   │   │   ├── Input/
│   │   │   ├── Modal/
│   │   │   ├── Card/
│   │   │   └── ...
│   │   ├── layout/        # 레이아웃 컴포넌트
│   │   │   ├── Header/
│   │   │   ├── Footer/
│   │   │   ├── Sidebar/
│   │   │   └── ...
│   │   └── features/      # 기능별 컴포넌트
│   │       ├── auth/
│   │       ├── post/
│   │       ├── comment/
│   │       └── ...
│   │
│   ├── composables/       # 재사용 가능한 로직
│   │   ├── useAuth.ts     # 인증 관련
│   │   ├── useApi.ts      # API 호출 헬퍼
│   │   ├── usePagination.ts # 페이지네이션
│   │   ├── useForm.ts     # 폼 관리
│   │   ├── useImageUpload.ts # 이미지 업로드
│   │   └── ...
│   │
│   ├── layouts/            # 레이아웃 템플릿
│   │   ├── DefaultLayout.vue
│   │   ├── AuthLayout.vue
│   │   └── AdminLayout.vue
│   │
│   ├── pages/              # 페이지 컴포넌트
│   │   ├── onboarding/
│   │   ├── auth/
│   │   │   ├── LoginPage.vue
│   │   │   └── SignupPage.vue
│   │   ├── home/
│   │   │   └── HomePage.vue
│   │   ├── reports/
│   │   │   ├── ReportsPage.vue
│   │   │   ├── ReportDetailPage.vue
│   │   │   ├── PersonalColorPage.vue
│   │   │   └── RecommendationsPage.vue
│   │   ├── community/
│   │   │   ├── CommunityPage.vue
│   │   │   ├── PostDetailPage.vue
│   │   │   ├── PostWritePage.vue
│   │   │   └── NoticeDetailPage.vue
│   │   └── mypage/
│   │       ├── MyPage.vue
│   │       └── ProfileEditPage.vue
│   │
│   ├── router/            # 라우터 설정
│   │   ├── index.ts
│   │   └── guards.ts      # 라우트 가드
│   │
│   ├── stores/            # Pinia 스토어
│   │   ├── auth.store.ts  # 인증 상태
│   │   ├── user.store.ts  # 사용자 정보
│   │   ├── post.store.ts  # 게시글 상태
│   │   ├── ui.store.ts    # UI 상태
│   │   └── ...
│   │
│   ├── types/             # TypeScript 타입 정의
│   │   ├── api.types.ts   # API 응답 타입
│   │   ├── user.types.ts
│   │   ├── post.types.ts
│   │   ├── report.types.ts
│   │   └── ...
│   │
│   ├── utils/             # 유틸리티 함수
│   │   ├── constants.ts   # 상수
│   │   ├── validators.ts  # 유효성 검사
│   │   ├── formatters.ts  # 포맷터
│   │   ├── helpers.ts     # 헬퍼 함수
│   │   └── ...
│   │
│   ├── styles/            # 전역 스타일
│   │   ├── main.css
│   │   ├── tailwind.css
│   │   └── variables.css
│   │
│   ├── App.vue            # 루트 컴포넌트
│   └── main.ts            # 진입점
│
├── tests/                 # 테스트 파일
│   ├── visual/            # 시각적 회귀 테스트
│   │   └── figma-comparison.spec.ts
│   ├── utils/             # 테스트 유틸리티
│   │   ├── figma-comparison.ts
│   │   └── playwright-helpers.ts
│   ├── baselines/         # 베이스라인 이미지
│   ├── screenshots/       # 실제 스크린샷
│   └── diffs/             # 차이점 이미지
│
├── mcp-servers/           # MCP 서버
│   └── figma-server.js    # Figma 연동 MCP 서버
│
├── .env                   # 환경 변수
├── .env.local             # 로컬 환경 변수
├── .gitignore
├── index.html
├── package.json
├── playwright.config.ts    # Playwright 설정
├── mcp-server.config.json # MCP 서버 설정
├── tsconfig.json
├── vite.config.ts
└── tailwind.config.js
```

## 주요 디렉토리 설명

### `/api`
- API 클라이언트 및 엔드포인트 정의
- Axios 인스턴스 설정 및 인터셉터 관리

### `/composables`
- Vue 3 Composition API 기반 재사용 로직
- 비즈니스 로직과 상태 관리 로직 분리

### `/stores`
- Pinia를 사용한 전역 상태 관리
- 서버 상태는 API 호출로 관리, 클라이언트 상태는 Pinia로 관리

### `/types`
- TypeScript 타입 정의
- API 응답/요청 타입, 도메인 모델 타입 등

### `/components`
- 컴포넌트는 기능별로 분류
- `common/`: 범용 컴포넌트
- `layout/`: 레이아웃 컴포넌트
- `features/`: 기능별 컴포넌트

## 개발 가이드

### 컴포넌트 작성 규칙
- Composition API 사용
- `<script setup lang="ts">` 사용
- Props와 Emits는 TypeScript로 타입 정의

### 상태 관리
- 서버 상태: API 호출로 직접 관리 (React Query 대신)
- 클라이언트 상태: Pinia 스토어 사용

### 스타일링
- Tailwind CSS 우선 사용
- 컴포넌트별 스타일이 복잡한 경우 CSS Module 사용

## 설치 및 실행

### 의존성 설치

```bash
npm install
```

### 개발 서버 실행

```bash
npm run dev
```

### 빌드

```bash
npm run build
```

### 프리뷰

```bash
npm run preview
```

## Figma 디자인 비교 테스트

Figma 디자인과 실제 구현을 비교하는 시각적 회귀 테스트를 실행할 수 있습니다.

자세한 내용은 [README-FIGMA-TESTING.md](./README-FIGMA-TESTING.md)를 참조하세요.

### 빠른 시작

1. Figma WebSocket 서버 실행:
```bash
npm run figma:socket
```

2. 시각적 테스트 실행:
```bash
npm run test:visual
```

3. UI 모드로 실행:
```bash
npm run test:visual:ui
```

## MCP 서버

Figma 연동을 위한 MCP 서버가 포함되어 있습니다. Cursor에서 MCP 도구로 사용할 수 있습니다.

자세한 내용은 [README-FIGMA-TESTING.md](./README-FIGMA-TESTING.md)를 참조하세요.
