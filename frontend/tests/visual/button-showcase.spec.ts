import { test, expect } from '@playwright/test';

/**
 * Button 컴포넌트 쇼케이스 페이지 테스트
 */
test.describe('Button Component Showcase', () => {
  test('버튼 컴포넌트 쇼케이스 페이지 스크린샷', async ({ page }) => {
    await page.goto('/components/buttons');
    await page.waitForLoadState('networkidle');
    
    // 페이지가 완전히 로드될 때까지 대기
    await page.waitForTimeout(1000);
    
    // 전체 페이지 스크린샷
    await page.screenshot({
      path: 'tests/screenshots/button-showcase-full.png',
      fullPage: true,
    });
    
    // 각 섹션별 스크린샷
    const sections = [
      'Variants',
      'Sizes',
      'With Icons',
      'States',
      'Button Groups',
      'Full Width',
      'Rounded',
    ];
    
    for (const section of sections) {
      const heading = page.getByRole('heading', { name: section });
      if (await heading.isVisible()) {
        const sectionElement = heading.locator('..');
        await sectionElement.screenshot({
          path: `tests/screenshots/button-showcase-${section.toLowerCase().replace(/\s+/g, '-')}.png`,
        });
      }
    }
  });
  
  test('버튼 컴포넌트 반응형 테스트', async ({ page }) => {
    const viewports = [
      { name: 'mobile', width: 375, height: 667 },
      { name: 'tablet', width: 768, height: 1024 },
      { name: 'desktop', width: 1920, height: 1080 },
    ];
    
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/components/buttons');
      await page.waitForLoadState('networkidle');
      await page.waitForTimeout(1000);
      
      await page.screenshot({
        path: `tests/screenshots/button-showcase-${viewport.name}.png`,
        fullPage: true,
      });
    }
  });
});

