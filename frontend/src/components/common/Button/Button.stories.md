# Button 컴포넌트 가이드

아토믹 디자인 패턴을 따르는 버튼 컴포넌트 시스템입니다.

## Atoms: Button

기본 버튼 컴포넌트입니다.

### 사용 예시

```vue
<template>
  <!-- 기본 버튼 -->
  <Button>클릭하세요</Button>
  
  <!-- 다양한 Variant -->
  <Button variant="primary">Primary</Button>
  <Button variant="secondary">Secondary</Button>
  <Button variant="tertiary">Tertiary</Button>
  <Button variant="danger">Danger</Button>
  <Button variant="outline">Outline</Button>
  <Button variant="ghost">Ghost</Button>
  <Button variant="text">Text</Button>
  
  <!-- 다양한 Size -->
  <Button size="xs">Extra Small</Button>
  <Button size="sm">Small</Button>
  <Button size="md">Medium</Button>
  <Button size="lg">Large</Button>
  <Button size="xl">Extra Large</Button>
  
  <!-- 아이콘 포함 -->
  <Button icon="arrow-right">다음</Button>
  <Button icon="arrow-left" icon-right>이전</Button>
  
  <!-- 로딩 상태 -->
  <Button loading>저장 중...</Button>
  <Button loading loading-text="처리 중">저장</Button>
  
  <!-- Full Width -->
  <Button full-width>전체 너비 버튼</Button>
  
  <!-- Rounded -->
  <Button rounded>둥근 버튼</Button>
  
  <!-- Link로 사용 -->
  <Button tag="a" href="/about">링크 버튼</Button>
</template>
```

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| type | `'button' \| 'submit' \| 'reset'` | `'button'` | 버튼 타입 |
| tag | `'button' \| 'a' \| 'router-link'` | `'button'` | 렌더링할 태그 |
| variant | `'primary' \| 'secondary' \| 'tertiary' \| 'danger' \| 'outline' \| 'ghost' \| 'text'` | `'primary'` | 버튼 스타일 변형 |
| size | `'xs' \| 'sm' \| 'md' \| 'lg' \| 'xl'` | `'md'` | 버튼 크기 |
| disabled | `boolean` | `false` | 비활성화 여부 |
| loading | `boolean` | `false` | 로딩 상태 |
| loadingText | `string` | - | 로딩 중 표시할 텍스트 |
| icon | `string` | - | 아이콘 이름 |
| iconRight | `boolean` | `false` | 아이콘을 오른쪽에 배치 |
| fullWidth | `boolean` | `false` | 전체 너비 사용 |
| rounded | `boolean` | `false` | 완전히 둥근 모서리 |

## Molecules: ButtonGroup

여러 버튼을 그룹으로 묶는 컴포넌트입니다.

### 사용 예시

```vue
<template>
  <!-- 기본 그룹 -->
  <ButtonGroup>
    <Button variant="outline">취소</Button>
    <Button>확인</Button>
  </ButtonGroup>
  
  <!-- Attached 그룹 (붙어있는 버튼) -->
  <ButtonGroup attached>
    <Button variant="outline">이전</Button>
    <Button variant="outline">다음</Button>
  </ButtonGroup>
  
  <!-- 세그먼트 그룹 -->
  <ButtonGroup variant="segmented">
    <Button variant="ghost">목록</Button>
    <Button variant="primary">그리드</Button>
    <Button variant="ghost">카드</Button>
  </ButtonGroup>
  
  <!-- 수직 그룹 -->
  <ButtonGroup orientation="vertical">
    <Button>버튼 1</Button>
    <Button>버튼 2</Button>
    <Button>버튼 3</Button>
  </ButtonGroup>
</template>
```

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| orientation | `'horizontal' \| 'vertical'` | `'horizontal'` | 그룹 방향 |
| attached | `boolean` | `false` | 버튼을 붙여서 표시 |
| variant | `'default' \| 'outlined' \| 'segmented'` | `'default'` | 그룹 스타일 |

## Molecules: ButtonWithIcon

아이콘이 포함된 버튼의 편의 컴포넌트입니다.

### 사용 예시

```vue
<template>
  <ButtonWithIcon icon="arrow-right">
    다음 단계
  </ButtonWithIcon>
  
  <ButtonWithIcon icon="check" icon-right>
    완료
  </ButtonWithIcon>
</template>
```

## 디자인 시스템

### 색상

- **Primary**: 주요 액션 (파란색 계열)
- **Secondary**: 보조 액션 (회색 계열)
- **Tertiary**: 3차 액션 (연한 회색)
- **Danger**: 위험한 액션 (빨간색)
- **Outline**: 테두리만 있는 스타일
- **Ghost**: 투명 배경
- **Text**: 텍스트 링크 스타일

### 크기

- **xs**: 24px 높이
- **sm**: 32px 높이
- **md**: 40px 높이 (기본)
- **lg**: 48px 높이
- **xl**: 56px 높이

## 접근성

- 키보드 포커스 스타일 제공
- `disabled` 상태에서 포인터 이벤트 비활성화
- 적절한 ARIA 속성 사용
- 스크린 리더 지원

