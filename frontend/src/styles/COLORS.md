# 프로젝트 커스텀 색상 가이드

이 프로젝트에서 사용하는 커스텀 색상 팔레트입니다.

## 색상 목록

| 색상 이름 | HEX 코드 | 설명 | 사용 예시 |
|---------|---------|------|----------|
| `brown` | `#8C704E` | 브라운/갈색 | 주요 브랜드 색상, 버튼 배경 |
| `tan` | `#A9745A` | 탄/갈색 | 보조 브랜드 색상 |
| `cream` | `#FFF5CC` | 크림/노란색 | 배경, 강조 영역 |
| `sky` | `#C9D7F8` | 연한 파란색 | 정보 영역, 배경 |
| `navy` | `#61627F` | 네이비/어두운 회색 | 텍스트, 아이콘 |
| `pink` | `#FFEDED` | 연한 분홍 | 경고/알림 배경 |
| `peach` | `#FFEBEB` | 연한 피치 | 경고/알림 배경 |
| `burgundy` | `#B0003A` | 버건디/진한 빨간색 | 에러, 중요 액션 |

## 사용 방법

### 1. Tailwind CSS 클래스로 사용

```vue
<template>
  <!-- 배경색 -->
  <div class="bg-digilog-brown">브라운 배경</div>
  <div class="bg-digilog-cream">크림 배경</div>
  
  <!-- 텍스트 색상 -->
  <p class="text-digilog-navy">네이비 텍스트</p>
  <p class="text-digilog-burgundy">버건디 텍스트</p>
  
  <!-- 테두리 색상 -->
  <div class="border-digilog-sky border-2">스카이 테두리</div>
  
  <!-- 호버 상태 -->
  <button class="bg-digilog-brown hover:bg-digilog-tan">
    호버 버튼
  </button>
</template>
```

### 2. CSS 변수로 사용

```vue
<template>
  <div class="custom-box">커스텀 박스</div>
</template>

<style scoped>
.custom-box {
  background-color: var(--color-digilog-brown);
  color: var(--color-digilog-cream);
  border: 2px solid var(--color-digilog-navy);
}
</style>
```

또는 전역 CSS에서:

```css
.my-component {
  background: var(--color-digilog-sky);
  color: var(--color-digilog-navy);
}
```

### 3. TypeScript/JavaScript에서 사용

```typescript
import { Colors, getColor, getColorVar } from '@/utils/colors'

// 직접 색상 값 사용
const backgroundColor = Colors.brown // '#8C704E'

// 헬퍼 함수 사용
const textColor = getColor('navy') // '#61627F'
const cssVar = getColorVar('sky') // 'var(--color-digilog-sky)'
```

### 4. 인라인 스타일로 사용

```vue
<template>
  <div :style="{ backgroundColor: Colors.cream }">
    크림 배경
  </div>
</template>

<script setup lang="ts">
import { Colors } from '@/utils/colors'
</script>
```

## 색상 조합 가이드

### 권장 조합

1. **브랜드 조합**
   - Primary: `brown` + `cream`
   - Secondary: `tan` + `sky`

2. **텍스트 조합**
   - Dark text on light: `navy` on `cream`
   - Light text on dark: `cream` on `brown`

3. **액션 조합**
   - Primary action: `brown` background
   - Danger action: `burgundy` background
   - Info: `sky` background

4. **배경 조합**
   - Light background: `cream`, `pink`, `peach`
   - Accent background: `sky`

## Tailwind 클래스 참조

모든 색상은 Tailwind의 표준 유틸리티 클래스와 함께 사용할 수 있습니다:

- `bg-digilog-{color}`: 배경색
- `text-digilog-{color}`: 텍스트 색상
- `border-digilog-{color}`: 테두리 색상
- `ring-digilog-{color}`: 링 색상 (포커스)
- `divide-digilog-{color}`: 구분선 색상
- `outline-digilog-{color}`: 아웃라인 색상

예시:
- `bg-digilog-brown`
- `text-digilog-navy`
- `border-digilog-sky`
- `hover:bg-digilog-tan`
- `focus:ring-digilog-burgundy`

## 접근성 고려사항

색상 대비를 고려하여 텍스트 가독성을 보장하세요:

- **높은 대비**: `navy` on `cream` ✅
- **중간 대비**: `brown` on `cream` ✅
- **낮은 대비**: `pink` on `peach` ⚠️ (주의 필요)

WCAG 2.1 AA 기준을 충족하려면 최소 4.5:1의 대비율이 필요합니다.

