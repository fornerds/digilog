# 프로젝트 구조 상세 설명

## Vue 3 + TypeScript 기반 폴더 구조

이 프로젝트는 Vue 3의 Composition API와 TypeScript를 활용한 모던한 프론트엔드 구조입니다.

## 주요 특징

### 1. Composition API 중심
- 모든 컴포넌트는 `<script setup lang="ts">` 사용
- Options API 대신 Composition API로 일관성 유지
- 재사용 가능한 로직은 `composables`로 분리

### 2. TypeScript 완전 지원
- 모든 파일에 TypeScript 타입 정의
- API 응답/요청 타입을 `types/` 디렉토리에 중앙 관리
- 타입 안정성 보장

### 3. 상태 관리: Pinia
- Vuex의 후속인 Pinia 사용
- Composition API 스타일의 스토어 정의
- 서버 상태는 API 호출로 직접 관리, 클라이언트 상태는 Pinia로 관리

### 4. API 클라이언트 분리
- `api/` 디렉토리에 모든 API 호출 로직 분리
- Axios 인스턴스 중앙 관리
- 인터셉터로 인증 토큰 자동 추가 및 에러 처리

## 디렉토리별 상세 설명

### `/src/api`
API 클라이언트 및 엔드포인트 정의
- `client.ts`: Axios 인스턴스 설정, 인터셉터 관리
- `*.api.ts`: 도메인별 API 함수 정의
- 모든 API 함수는 TypeScript 타입으로 요청/응답 정의

### `/src/composables`
Vue 3 Composition API 기반 재사용 로직
- `useAuth.ts`: 인증 관련 로직
- `useApi.ts`: API 호출 헬퍼 (로딩, 에러 상태 관리)
- `usePagination.ts`: 페이지네이션 로직
- `useImageUpload.ts`: 이미지 업로드 로직
- 각 composable은 독립적으로 사용 가능

### `/src/stores`
Pinia 스토어 (전역 상태 관리)
- `auth.store.ts`: 인증 상태 (토큰, 사용자 정보)
- `user.store.ts`: 사용자 관련 상태 및 액션
- `post.store.ts`: 게시글 관련 상태 및 액션
- `ui.store.ts`: UI 상태 (로딩, 사이드바, 테마 등)

### `/src/types`
TypeScript 타입 정의
- API 응답/요청 타입
- 도메인 모델 타입
- 각 도메인별로 파일 분리

### `/src/components`
컴포넌트는 3가지 카테고리로 분류
- `common/`: 범용 컴포넌트 (Button, Input, Modal 등)
- `layout/`: 레이아웃 컴포넌트 (Header, Footer, Sidebar)
- `features/`: 기능별 컴포넌트 (도메인별 컴포넌트)

### `/src/pages`
페이지 컴포넌트
- 라우트와 1:1 매핑
- 각 페이지는 해당 도메인 폴더에 위치

### `/src/router`
라우터 설정
- `index.ts`: 라우트 정의
- `guards.ts`: 라우트 가드 (인증 체크 등)

### `/src/utils`
유틸리티 함수
- `constants.ts`: 상수 정의
- `validators.ts`: 유효성 검사 함수
- `formatters.ts`: 포맷팅 함수 (날짜, 가격 등)

## 개발 패턴

### 컴포넌트 작성
```vue
<template>
  <div>
    <!-- 템플릿 -->
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
// 로직
</script>

<style scoped>
/* 스타일 */
</style>
```

### Composables 사용
```typescript
// 컴포넌트에서
import { useAuth } from '@/composables/useAuth'

const { isAuthenticated, user, login, logout } = useAuth()
```

### Pinia 스토어 사용
```typescript
// 컴포넌트에서
import { useAuthStore } from '@/stores/auth.store'

const authStore = useAuthStore()
// authStore.isAuthenticated, authStore.user 등 사용
```

### API 호출
```typescript
// 컴포넌트에서
import { getPosts } from '@/api/post.api'
import { useApi } from '@/composables/useApi'

const { data, isLoading, error, execute } = useApi(() => getPosts(), {
  immediate: true
})
```

## 스타일링

- **Tailwind CSS** 우선 사용
- 컴포넌트별 스타일이 복잡한 경우 CSS Module 사용
- 전역 스타일은 `styles/main.css`에서 관리

## 빌드 및 개발

- **Vite**: 빠른 개발 서버 및 빌드
- **TypeScript**: 타입 체크
- **ESLint**: 코드 품질 관리

## 환경 변수

`.env` 파일에 다음 변수 설정:
```
VITE_API_BASE_URL=http://localhost:18080/api
```


