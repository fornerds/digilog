import { PNG } from 'pngjs';
import pixelmatch from 'pixelmatch';
import fs from 'fs/promises';
import path from 'path';
import WebSocket from 'ws';

interface ComparisonResult {
  diff: number;
  matched: boolean;
  diffImage?: Buffer;
}

/**
 * Figma 디자인과 실제 스크린샷을 비교하는 유틸리티
 */
export async function compareScreenshots(
  actualScreenshot: Buffer,
  pageName: string,
  viewport: string
): Promise<ComparisonResult> {
  const baselineDir = path.join(process.cwd(), 'tests', 'baselines', pageName);
  const baselinePath = path.join(baselineDir, `${viewport}.png`);
  const diffPath = path.join(process.cwd(), 'tests', 'diffs', pageName, `${viewport}-diff.png`);

  // 베이스라인 디렉토리 생성
  await fs.mkdir(baselineDir, { recursive: true });
  await fs.mkdir(path.dirname(diffPath), { recursive: true });

  // 베이스라인 이미지가 없으면 생성
  let baselineExists = false;
  try {
    await fs.access(baselinePath);
    baselineExists = true;
  } catch {
    // 베이스라인이 없으면 첫 실행으로 간주하고 저장
    await fs.writeFile(baselinePath, actualScreenshot);
    return { diff: 0, matched: true };
  }

  // 베이스라인 이미지 로드
  const baselineImage = PNG.sync.read(await fs.readFile(baselinePath));
  const actualImage = PNG.sync.read(actualScreenshot);

  // 이미지 크기가 다른 경우 처리
  if (
    baselineImage.width !== actualImage.width ||
    baselineImage.height !== actualImage.height
  ) {
    return {
      diff: 1,
      matched: false,
    };
  }

  // 차이점 계산
  const diffImage = new PNG({
    width: baselineImage.width,
    height: baselineImage.height,
  });

  const numDiffPixels = pixelmatch(
    baselineImage.data,
    actualImage.data,
    diffImage.data,
    baselineImage.width,
    baselineImage.height,
    {
      threshold: 0.1,
      alpha: 0.1,
      diffColor: [255, 0, 0],
      diffColorAlt: [0, 0, 255],
    }
  );

  const totalPixels = baselineImage.width * baselineImage.height;
  const diffRatio = numDiffPixels / totalPixels;

  // 차이 이미지 저장
  if (diffRatio > 0.02) {
    await fs.writeFile(diffPath, PNG.sync.write(diffImage));
  }

  return {
    diff: diffRatio,
    matched: diffRatio <= 0.02,
    diffImage: diffRatio > 0.02 ? PNG.sync.write(diffImage) : undefined,
  };
}

/**
 * Figma에서 디자인 이미지를 가져오는 함수
 * WebSocket을 통해 Figma 플러그인과 통신
 */
export async function fetchFigmaDesign(
  nodeId: string,
  viewport: string
): Promise<Buffer> {
  const ws = new WebSocket('ws://localhost:3055');

  return new Promise((resolve, reject) => {
    const timeout = setTimeout(() => {
      ws.close();
      reject(new Error('Figma export timeout'));
    }, 30000);

    ws.on('open', () => {
      ws.send(
        JSON.stringify({
          type: 'export',
          nodeId,
          format: 'png',
          scale: viewport === 'mobile' ? 2 : 1,
        })
      );
    });

    ws.on('message', (data: WebSocket.Data) => {
      try {
        const message = JSON.parse(data.toString());
        if (message.type === 'export-complete') {
          clearTimeout(timeout);
          resolve(Buffer.from(message.image, 'base64'));
          ws.close();
        }
      } catch (error) {
        clearTimeout(timeout);
        reject(error);
        ws.close();
      }
    });

    ws.on('error', (error) => {
      clearTimeout(timeout);
      reject(error);
    });
  });
}

