# 커스텀 색상 사용 예시

프로젝트 커스텀 색상을 다양한 방법으로 사용하는 예시입니다.

## 1. Tailwind 클래스 사용

### 배경색
```vue
<template>
  <div class="bg-digilog-brown">브라운 배경</div>
  <div class="bg-digilog-cream">크림 배경</div>
  <div class="bg-digilog-sky">스카이 배경</div>
  <div class="bg-digilog-pink">핑크 배경</div>
</template>
```

### 텍스트 색상
```vue
<template>
  <p class="text-digilog-navy">네이비 텍스트</p>
  <p class="text-digilog-brown">브라운 텍스트</p>
  <p class="text-digilog-burgundy">버건디 텍스트</p>
</template>
```

### 테두리
```vue
<template>
  <div class="border-2 border-digilog-brown">브라운 테두리</div>
  <div class="border border-digilog-sky">스카이 테두리</div>
</template>
```

### 호버 및 포커스
```vue
<template>
  <button class="bg-digilog-brown hover:bg-digilog-tan focus:ring-digilog-brown">
    호버 버튼
  </button>
  
  <a class="text-digilog-navy hover:text-digilog-brown">
    호버 링크
  </a>
</template>
```

## 2. CSS 변수 사용

### 스타일 블록에서
```vue
<template>
  <div class="custom-card">카드</div>
</template>

<style scoped>
.custom-card {
  background-color: var(--color-digilog-cream);
  color: var(--color-digilog-navy);
  border: 2px solid var(--color-digilog-brown);
}

.custom-card:hover {
  background-color: var(--color-digilog-pink);
}
</style>
```

### 전역 스타일에서
```css
/* main.css 또는 전역 CSS 파일 */
.hero-section {
  background: linear-gradient(
    to bottom,
    var(--color-digilog-cream),
    var(--color-digilog-sky)
  );
}

.error-message {
  background-color: var(--color-digilog-pink);
  color: var(--color-digilog-burgundy);
  border-left: 4px solid var(--color-digilog-burgundy);
}
```

## 3. TypeScript/JavaScript에서 사용

### 직접 색상 값 사용
```vue
<script setup lang="ts">
import { Colors } from '@/utils/colors'

const cardStyle = {
  backgroundColor: Colors.cream,
  color: Colors.navy,
  borderColor: Colors.brown,
}
</script>

<template>
  <div :style="cardStyle">카드</div>
</template>
```

### 동적 색상 선택
```vue
<script setup lang="ts">
import { Colors, getColor } from '@/utils/colors'

const status = ref<'success' | 'error' | 'warning'>('success')

const statusColor = computed(() => {
  switch (status.value) {
    case 'success':
      return Colors.sky
    case 'error':
      return Colors.burgundy
    case 'warning':
      return Colors.pink
    default:
      return Colors.cream
  }
})
</script>

<template>
  <div :style="{ backgroundColor: statusColor }">
    상태 메시지
  </div>
</template>
```

## 4. 컴포넌트에서 사용

### Button 컴포넌트
```vue
<template>
  <Button variant="primary">Primary 버튼 (브라운)</Button>
  <Button variant="secondary">Secondary 버튼 (스카이)</Button>
  <Button variant="danger">Danger 버튼 (버건디)</Button>
</template>
```

### 커스텀 컴포넌트
```vue
<template>
  <div class="alert" :class="alertClass">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'info' | 'warning' | 'error'
}

const props = withDefaults(defineProps<Props>(), {
  type: 'info',
})

const alertClass = computed(() => {
  return {
    'alert-info': props.type === 'info',
    'alert-warning': props.type === 'warning',
    'alert-error': props.type === 'error',
  }
})
</script>

<style scoped>
.alert {
  @apply p-4 rounded-lg;
}

.alert-info {
  background-color: var(--color-digilog-sky);
  color: var(--color-digilog-navy);
}

.alert-warning {
  background-color: var(--color-digilog-pink);
  color: var(--color-digilog-burgundy);
}

.alert-error {
  background-color: var(--color-digilog-peach);
  color: var(--color-digilog-burgundy);
  border: 1px solid var(--color-digilog-burgundy);
}
</style>
```

## 5. 반응형 및 다크모드

### 반응형 색상
```vue
<template>
  <div class="responsive-bg">
    반응형 배경
  </div>
</template>

<style scoped>
.responsive-bg {
  @apply bg-digilog-cream md:bg-digilog-sky lg:bg-digilog-pink;
}
</style>
```

## 6. 그라데이션 사용

```vue
<template>
  <div class="gradient-bg">그라데이션 배경</div>
</template>

<style scoped>
.gradient-bg {
  background: linear-gradient(
    135deg,
    var(--color-digilog-cream) 0%,
    var(--color-digilog-sky) 100%
  );
}
</style>
```

또는 Tailwind로:

```vue
<template>
  <div class="bg-gradient-to-r from-digilog-cream to-digilog-sky">
    그라데이션
  </div>
</template>
```

## 7. 그림자와 함께 사용

```vue
<template>
  <div class="shadow-card">카드</div>
</template>

<style scoped>
.shadow-card {
  background-color: var(--color-digilog-cream);
  box-shadow: 0 4px 6px rgba(140, 112, 78, 0.1); /* brown with opacity */
}
</style>
```

## 8. 아이콘 색상

```vue
<template>
  <Icon name="check" class="text-digilog-brown" />
  <Icon name="alert" class="text-digilog-burgundy" />
  <Icon name="info" class="text-digilog-sky" />
</template>
```

## 9. 폼 요소

```vue
<template>
  <input 
    class="border-digilog-brown focus:ring-digilog-brown"
    type="text"
  />
  
  <select class="bg-digilog-cream text-digilog-navy">
    <option>옵션 1</option>
  </select>
</template>
```

## 10. 테이블

```vue
<template>
  <table class="table-custom">
    <thead>
      <tr class="bg-digilog-brown text-white">
        <th>헤더</th>
      </tr>
    </thead>
    <tbody>
      <tr class="bg-digilog-cream hover:bg-digilog-pink">
        <td>데이터</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
.table-custom {
  @apply w-full border-collapse;
}

.table-custom th,
.table-custom td {
  @apply p-4 border border-digilog-sky;
}
</style>
```

