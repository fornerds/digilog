# SUIT 폰트 설정 가이드

프로젝트 전역에서 SUIT 폰트를 사용하도록 설정되어 있습니다.

## 폰트 정보

- **폰트명**: SUIT
- **제작자**: sunn-us
- **특징**: 한국어 웹폰트, 다양한 굵기 지원

## 적용 방법

### 1. 자동 적용

프로젝트의 모든 텍스트는 자동으로 SUIT 폰트를 사용합니다.

```vue
<template>
  <div>이 텍스트는 자동으로 SUIT 폰트를 사용합니다.</div>
</template>
```

### 2. Tailwind 클래스 사용

```vue
<template>
  <!-- font-sans는 SUIT 폰트를 사용합니다 -->
  <p class="font-sans">SUIT 폰트 텍스트</p>
  
  <!-- 굵기 조절 -->
  <p class="font-thin">Thin (100)</p>
  <p class="font-extralight">ExtraLight (200)</p>
  <p class="font-light">Light (300)</p>
  <p class="font-normal">Normal (400)</p>
  <p class="font-medium">Medium (500)</p>
  <p class="font-semibold">SemiBold (600)</p>
  <p class="font-bold">Bold (700)</p>
  <p class="font-extrabold">ExtraBold (800)</p>
  <p class="font-black">Heavy (900)</p>
</template>
```

### 3. CSS에서 직접 사용

```vue
<style scoped>
.custom-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
}
</style>
```

### 4. TypeScript/JavaScript에서 사용

```typescript
const style = {
  fontFamily: "'SUIT', sans-serif",
  fontWeight: 600,
}
```

## 폰트 로딩 방식

현재는 CDN을 통해 SUIT 폰트를 로드합니다:

```css
@import url('https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css');
```

### 로컬 폰트 파일 사용하기

CDN 대신 로컬 폰트 파일을 사용하려면:

1. `src/assets/fonts/` 디렉토리에 폰트 파일 추가
2. `src/styles/fonts.css` 파일에서 CDN import를 주석 처리
3. `@font-face` 선언의 주석을 해제

```css
/* fonts.css */
/* CDN import 주석 처리 */
/* @import url('https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/woff2/SUIT.css'); */

/* 로컬 폰트 사용 */
@font-face {
  font-family: 'SUIT';
  font-weight: 400;
  src: url('@/assets/fonts/SUIT-Regular.woff2') format('woff2');
  font-display: swap;
}
/* ... 나머지 굵기들 ... */
```

## 폰트 최적화

### Preconnect

`index.html`에 preconnect가 추가되어 있어 폰트 로딩이 최적화됩니다:

```html
<link rel="preconnect" href="https://cdn.jsdelivr.net" crossorigin>
```

### Font Display

`font-display: swap`을 사용하여 폰트 로딩 중에도 텍스트가 표시됩니다.

## 사용 예시

### 기본 사용

```vue
<template>
  <div class="container">
    <h1 class="text-3xl font-bold">제목</h1>
    <p class="text-base font-normal">본문 텍스트</p>
    <span class="text-sm font-medium">작은 텍스트</span>
  </div>
</template>
```

### 컴포넌트에서 사용

```vue
<template>
  <button class="font-semibold">버튼</button>
</template>
```

### 커스텀 스타일

```vue
<style scoped>
.heading {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 2rem;
  letter-spacing: -0.02em;
}

.body-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 400;
  line-height: 1.6;
}
</style>
```

## 폰트 파일 다운로드

로컬 폰트 파일이 필요한 경우:

1. [SUIT 폰트 GitHub](https://github.com/sunn-us/SUIT)에서 다운로드
2. `src/assets/fonts/` 디렉토리에 파일 추가
3. `fonts.css`에서 로컬 폰트 사용 설정

## 주의사항

- CDN을 사용하는 경우 인터넷 연결이 필요합니다
- 프로덕션 환경에서는 로컬 폰트 파일 사용을 권장합니다
- 폰트 파일 크기가 크므로 필요한 굵기만 포함하는 것을 권장합니다

## 폰트 라이선스

SUIT 폰트는 SIL Open Font License 1.1을 따릅니다.
자세한 내용은 [SUIT 폰트 GitHub](https://github.com/sunn-us/SUIT)를 참조하세요.





