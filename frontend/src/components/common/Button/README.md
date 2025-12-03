# Button 컴포넌트 시스템

아토믹 디자인 패턴을 따르는 버튼 컴포넌트 시스템입니다.

## 구조

```
Button/
├── Button.vue          # Atom: 기본 버튼 컴포넌트
├── ButtonGroup.vue     # Molecule: 버튼 그룹
├── ButtonWithIcon.vue  # Molecule: 아이콘 버튼 (편의 컴포넌트)
├── Button.types.ts     # 타입 정의
├── Button.stories.md   # 사용 가이드
├── Button.examples.vue # 예시 컴포넌트
├── index.ts            # Export
└── README.md           # 이 파일
```

## Atoms: Button

가장 기본이 되는 버튼 컴포넌트입니다.

### 기본 사용법

```vue
<template>
  <Button>클릭하세요</Button>
</template>

<script setup lang="ts">
import { Button } from '@/components/common/Button'
</script>
```

### Variants (스타일 변형)

```vue
<Button variant="primary">Primary</Button>
<Button variant="secondary">Secondary</Button>
<Button variant="tertiary">Tertiary</Button>
<Button variant="danger">Danger</Button>
<Button variant="outline">Outline</Button>
<Button variant="ghost">Ghost</Button>
<Button variant="text">Text</Button>
```

### Sizes (크기)

```vue
<Button size="xs">Extra Small</Button>
<Button size="sm">Small</Button>
<Button size="md">Medium</Button>
<Button size="lg">Large</Button>
<Button size="xl">Extra Large</Button>
```

### 아이콘 사용

```vue
<!-- 왼쪽 아이콘 -->
<Button icon="arrow-right">다음</Button>

<!-- 오른쪽 아이콘 -->
<Button icon="arrow-left" icon-right>이전</Button>
```

**참고**: 아이콘은 `src/assets/icons/` 디렉토리에 SVG 파일로 저장되어야 합니다.

### 로딩 상태

```vue
<Button loading>저장 중...</Button>
<Button loading loading-text="처리 중">저장</Button>
```

### 기타 옵션

```vue
<!-- 전체 너비 -->
<Button full-width>전체 너비 버튼</Button>

<!-- 둥근 모서리 -->
<Button rounded>둥근 버튼</Button>

<!-- 링크로 사용 -->
<Button tag="a" href="/about">링크 버튼</Button>
<Button tag="router-link" to="/home">라우터 링크</Button>

<!-- 비활성화 -->
<Button disabled>비활성화</Button>
```

## Molecules: ButtonGroup

여러 버튼을 그룹으로 묶는 컴포넌트입니다.

### 기본 사용법

```vue
<template>
  <ButtonGroup>
    <Button variant="outline">취소</Button>
    <Button>확인</Button>
  </ButtonGroup>
</template>

<script setup lang="ts">
import { Button, ButtonGroup } from '@/components/common/Button'
</script>
```

### Attached (붙어있는 버튼)

```vue
<ButtonGroup attached>
  <Button variant="outline">이전</Button>
  <Button variant="outline">다음</Button>
</ButtonGroup>
```

### Segmented (세그먼트 스타일)

```vue
<ButtonGroup variant="segmented">
  <Button variant="ghost">목록</Button>
  <Button variant="primary">그리드</Button>
  <Button variant="ghost">카드</Button>
</ButtonGroup>
```

### 수직 그룹

```vue
<ButtonGroup orientation="vertical">
  <Button>버튼 1</Button>
  <Button>버튼 2</Button>
  <Button>버튼 3</Button>
</ButtonGroup>
```

## Molecules: ButtonWithIcon

아이콘이 포함된 버튼의 편의 컴포넌트입니다. 내부적으로 `Button` 컴포넌트를 사용합니다.

```vue
<template>
  <ButtonWithIcon icon="arrow-right">
    다음 단계
  </ButtonWithIcon>
</template>

<script setup lang="ts">
import { ButtonWithIcon } from '@/components/common/Button'
</script>
```

## 실제 사용 예시

### 폼 제출 버튼

```vue
<template>
  <form @submit.prevent="handleSubmit">
    <!-- 폼 필드들 -->
    
    <div class="flex gap-2 justify-end mt-4">
      <Button type="button" variant="outline" @click="handleCancel">
        취소
      </Button>
      <Button type="submit" :loading="isSubmitting">
        저장
      </Button>
    </div>
  </form>
</template>
```

### 네비게이션 버튼

```vue
<template>
  <div class="flex justify-between">
    <Button icon="arrow-left" variant="ghost" @click="goBack">
      이전
    </Button>
    <Button icon="arrow-right" icon-right @click="goNext">
      다음
    </Button>
  </div>
</template>
```

### 액션 그룹

```vue
<template>
  <ButtonGroup attached>
    <Button variant="outline" @click="handleEdit">
      수정
    </Button>
    <Button variant="outline" @click="handleDelete">
      삭제
    </Button>
    <Button variant="danger" @click="handleReport">
      신고
    </Button>
  </ButtonGroup>
</template>
```

## Props API

### Button Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `type` | `'button' \| 'submit' \| 'reset'` | `'button'` | HTML 버튼 타입 |
| `tag` | `'button' \| 'a' \| 'router-link'` | `'button'` | 렌더링할 태그 |
| `variant` | `'primary' \| 'secondary' \| 'tertiary' \| 'danger' \| 'outline' \| 'ghost' \| 'text'` | `'primary'` | 버튼 스타일 |
| `size` | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | 버튼 크기 |
| `disabled` | `boolean` | `false` | 비활성화 여부 |
| `loading` | `boolean` | `false` | 로딩 상태 |
| `loadingText` | `string` | - | 로딩 중 텍스트 |
| `icon` | `string` | - | 아이콘 이름 |
| `iconRight` | `boolean` | `false` | 아이콘 오른쪽 배치 |
| `fullWidth` | `boolean` | `false` | 전체 너비 |
| `rounded` | `boolean` | `false` | 둥근 모서리 |
| `href` | `string` | - | 링크 URL (tag="a"일 때) |
| `to` | `string \| object` | - | 라우터 경로 (tag="router-link"일 때) |

### ButtonGroup Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `orientation` | `'horizontal' \| 'vertical'` | `'horizontal'` | 그룹 방향 |
| `attached` | `boolean` | `false` | 버튼 붙이기 |
| `variant` | `'default' \| 'outlined' \| 'segmented'` | `'default'` | 그룹 스타일 |
| `size` | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | 버튼 크기 |

## 이벤트

### Button Events

- `@click`: 버튼 클릭 이벤트

```vue
<Button @click="handleClick">클릭</Button>
```

## 스타일 커스터마이징

Tailwind CSS를 사용하여 스타일이 정의되어 있습니다. 필요시 `tailwind.config.js`에서 색상을 커스터마이징할 수 있습니다.

```js
// tailwind.config.js
theme: {
  extend: {
    colors: {
      primary: {
        600: '#0284c7', // Primary 버튼 색상
        // ...
      },
    },
  },
}
```

## 접근성

- 키보드 포커스 스타일 제공
- `disabled` 상태에서 적절한 스타일 적용
- 스크린 리더를 위한 ARIA 속성
- 로딩 상태 시 적절한 피드백

## 아토믹 디자인 패턴

이 버튼 시스템은 아토믹 디자인 패턴을 따릅니다:

- **Atoms**: `Button` - 가장 작은 단위의 버튼 컴포넌트
- **Molecules**: `ButtonGroup`, `ButtonWithIcon` - Atoms를 조합한 컴포넌트
- **Organisms**: 더 복잡한 UI 조합에서 사용 (예: Header, Form 등)

이 패턴을 통해 재사용 가능하고 일관된 디자인 시스템을 구축할 수 있습니다.

