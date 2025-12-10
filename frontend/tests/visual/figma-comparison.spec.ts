import { test, expect } from '@playwright/test';
import { compareScreenshots } from '../utils/figma-comparison';

/**
 * Figma 디자인과 실제 구현을 비교하는 테스트
 */
test.describe('Figma Design Comparison', () => {
  // 반응형 브레이크포인트 정의
  const viewports = [
    { name: 'mobile', width: 375, height: 667 },
    { name: 'tablet', width: 768, height: 1024 },
    { name: 'desktop', width: 1920, height: 1080 },
  ];

  test('홈페이지 디자인 비교', async ({ page }) => {
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/');
      await page.waitForLoadState('networkidle');

      const screenshot = await page.screenshot({ fullPage: true });
      const comparison = await compareScreenshots(
        screenshot,
        'home',
        viewport.name
      );

      expect(comparison.diff).toBeLessThan(0.02); // 2% 이하 차이 허용
    }
  });

  test('로그인 페이지 디자인 비교', async ({ page }) => {
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/auth/login');
      await page.waitForLoadState('networkidle');

      const screenshot = await page.screenshot({ fullPage: true });
      const comparison = await compareScreenshots(
        screenshot,
        'login',
        viewport.name
      );

      expect(comparison.diff).toBeLessThan(0.02);
    }
  });

  test('회원가입 페이지 디자인 비교', async ({ page }) => {
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/auth/signup');
      await page.waitForLoadState('networkidle');

      const screenshot = await page.screenshot({ fullPage: true });
      const comparison = await compareScreenshots(
        screenshot,
        'signup',
        viewport.name
      );

      expect(comparison.diff).toBeLessThan(0.02);
    }
  });

  test('커뮤니티 페이지 디자인 비교', async ({ page }) => {
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/community');
      await page.waitForLoadState('networkidle');

      const screenshot = await page.screenshot({ fullPage: true });
      const comparison = await compareScreenshots(
        screenshot,
        'community',
        viewport.name
      );

      expect(comparison.diff).toBeLessThan(0.02);
    }
  });

  test('마이페이지 디자인 비교', async ({ page }) => {
    // 로그인이 필요한 경우, 테스트용 인증 처리 필요
    for (const viewport of viewports) {
      await page.setViewportSize({ width: viewport.width, height: viewport.height });
      await page.goto('/mypage');
      await page.waitForLoadState('networkidle');

      const screenshot = await page.screenshot({ fullPage: true });
      const comparison = await compareScreenshots(
        screenshot,
        'mypage',
        viewport.name
      );

      expect(comparison.diff).toBeLessThan(0.02);
    }
  });
});





