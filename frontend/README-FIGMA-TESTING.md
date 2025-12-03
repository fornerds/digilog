# Figma 디자인 비교 테스트 가이드

이 프로젝트는 Cursor Talk To Figma Plugin과 Playwright를 사용하여 Figma 디자인과 실제 구현을 비교 테스트합니다.

## 설정 방법

### 1. 의존성 설치

```bash
npm install
```

### 2. Figma WebSocket 서버 실행

터미널에서 다음 명령어를 실행하여 Figma WebSocket 서버를 시작합니다:

```bash
npm run figma:socket
# 또는
bunx cursor-talk-to-figma-socket
```

서버는 포트 3055에서 실행됩니다.

### 3. 환경 변수 설정

`.env` 파일을 생성하고 다음 내용을 추가하세요:

```env
FIGMA_API_KEY=your_figma_api_key_here
FIGMA_WS_PORT=3055
VITE_API_BASE_URL=http://localhost:18080
```

### 4. MCP 서버 설정 (선택사항)

Cursor에서 MCP 서버를 사용하려면 `mcp-server.config.json` 파일을 Cursor 설정에 추가하세요.

## 사용 방법

### 시각적 회귀 테스트 실행

```bash
# 모든 시각적 테스트 실행
npm run test:visual

# UI 모드로 실행 (대화형)
npm run test:visual:ui

# 베이스라인 업데이트
npm run test:visual:update
```

### 반응형 테스트

테스트는 자동으로 다음 뷰포트에서 실행됩니다:
- Mobile (375x667)
- Tablet (768x1024)
- Desktop (1920x1080)

### 테스트 구조

```
tests/
├── visual/
│   └── figma-comparison.spec.ts  # 메인 테스트 파일
├── utils/
│   ├── figma-comparison.ts       # Figma 비교 유틸리티
│   └── playwright-helpers.ts     # Playwright 헬퍼 함수
├── baselines/                     # 베이스라인 이미지
├── screenshots/                   # 실제 스크린샷
└── diffs/                         # 차이점 이미지
```

## MCP 도구 사용

MCP 서버를 통해 다음 도구를 사용할 수 있습니다:

### 1. get_figma_design
Figma에서 디자인 이미지를 가져옵니다.

```json
{
  "nodeId": "figma-node-id",
  "viewport": "mobile"
}
```

### 2. compare_with_implementation
Figma 디자인과 실제 구현을 비교합니다.

```json
{
  "pageName": "home",
  "nodeId": "figma-node-id",
  "viewport": "desktop",
  "url": "http://localhost:3000/"
}
```

### 3. update_baseline
베이스라인 이미지를 업데이트합니다.

```json
{
  "pageName": "home",
  "viewport": "desktop",
  "source": "figma"
}
```

## 테스트 작성 가이드

새로운 페이지에 대한 테스트를 추가하려면 `tests/visual/figma-comparison.spec.ts`에 다음 형식으로 추가하세요:

```typescript
test('새 페이지 디자인 비교', async ({ page }) => {
  for (const viewport of viewports) {
    await page.setViewportSize({ width: viewport.width, height: viewport.height });
    await page.goto('/your-page');
    await page.waitForLoadState('networkidle');

    const screenshot = await page.screenshot({ fullPage: true });
    const comparison = await compareScreenshots(
      screenshot,
      'your-page-name',
      viewport.name
    );

    expect(comparison.diff).toBeLessThan(0.02);
  }
});
```

## 차이 허용 범위

기본적으로 2% (0.02) 이하의 차이는 허용됩니다. 이는 반응형 레이아웃의 미세한 차이나 렌더링 차이를 고려한 것입니다.

필요에 따라 `expect(comparison.diff).toBeLessThan(0.02)` 값을 조정할 수 있습니다.

## 문제 해결

### WebSocket 연결 실패
- Figma WebSocket 서버가 실행 중인지 확인하세요
- 포트 3055가 사용 가능한지 확인하세요
- 방화벽 설정을 확인하세요

### 테스트 실패
- 개발 서버가 실행 중인지 확인하세요 (`npm run dev`)
- 베이스라인 이미지가 올바른지 확인하세요
- 차이 이미지를 확인하여 문제를 파악하세요 (`tests/diffs/`)

### MCP 서버 오류
- Node.js 버전이 18 이상인지 확인하세요
- 필요한 패키지가 모두 설치되었는지 확인하세요

