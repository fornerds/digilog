# Assets 디렉토리

정적 자산(이미지, 아이콘, 폰트 등)을 관리하는 디렉토리입니다.

## 디렉토리 구조

```
assets/
├── images/          # 이미지 파일
│   ├── logo.png
│   ├── placeholder.png
│   └── ...
├── icons/           # 아이콘 파일 (SVG 권장)
│   ├── user.svg
│   ├── home.svg
│   └── ...
└── fonts/           # 폰트 파일
    └── ...
```

## 사용 방법

### 1. 이미지 사용

#### 방법 1: import로 가져오기 (권장)
```vue
<template>
  <img :src="logoUrl" alt="Logo" />
</template>

<script setup lang="ts">
import logoUrl from '@/assets/images/logo.png'
</script>
```

#### 방법 2: public 디렉토리 사용 (빌드 시 복사되지 않음)
```vue
<template>
  <img src="/images/logo.png" alt="Logo" />
</template>
```
- `public/images/logo.png`에 파일을 두고 사용
- 빌드 시 그대로 복사되므로 파일명 변경 불가

### 2. SVG 아이콘 사용

#### 방법 1: 컴포넌트로 import
```vue
<template>
  <UserIcon class="w-6 h-6" />
</template>

<script setup lang="ts">
import UserIcon from '@/assets/icons/user.svg?component'
</script>
```

#### 방법 2: img 태그로 사용
```vue
<template>
  <img src="@/assets/icons/user.svg" alt="User" />
</template>
```

### 3. CSS에서 사용
```css
.background {
  background-image: url('@/assets/images/background.jpg');
}
```

## 권장사항

1. **이미지 최적화**: 프로덕션 배포 전 이미지 최적화 권장
2. **SVG 아이콘**: 아이콘은 SVG 형식 사용 권장 (확대/축소 시 깨짐 없음)
3. **파일명 규칙**: kebab-case 사용 (예: `user-profile.png`)
4. **용도별 분류**: 이미지, 아이콘, 폰트 등 용도별로 디렉토리 분리

## Vite의 특별한 기능

Vite는 `?url`, `?raw`, `?component` 등의 쿼리 파라미터를 지원합니다:

- `?url`: 파일의 URL 반환
- `?raw`: 파일의 원본 내용 반환
- `?component`: Vue 컴포넌트로 변환 (SVG에 유용)

```typescript
// URL로 가져오기
import logoUrl from '@/assets/images/logo.png?url'

// 원본 내용 가져오기
import svgContent from '@/assets/icons/icon.svg?raw'

// Vue 컴포넌트로 변환
import IconComponent from '@/assets/icons/icon.svg?component'
```








