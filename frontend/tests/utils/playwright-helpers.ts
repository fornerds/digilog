import { Page } from '@playwright/test';

/**
 * Playwright 테스트 헬퍼 함수
 */

/**
 * 페이지가 완전히 로드될 때까지 대기
 */
export async function waitForPageLoad(page: Page) {
  await page.waitForLoadState('networkidle');
  await page.waitForLoadState('domcontentloaded');
  // 추가적인 애니메이션이나 트랜지션이 있다면 대기
  await page.waitForTimeout(500);
}

/**
 * 반응형 뷰포트 설정
 */
export const VIEWPORTS = {
  mobile: { width: 375, height: 667 },
  tablet: { width: 768, height: 1024 },
  desktop: { width: 1920, height: 1080 },
} as const;

/**
 * 스크린샷을 찍고 저장
 */
export async function takeScreenshot(
  page: Page,
  name: string,
  viewport: keyof typeof VIEWPORTS
) {
  await page.setViewportSize(VIEWPORTS[viewport]);
  await waitForPageLoad(page);
  return await page.screenshot({
    fullPage: true,
    path: `tests/screenshots/${name}-${viewport}.png`,
  });
}





