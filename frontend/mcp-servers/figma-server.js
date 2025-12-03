#!/usr/bin/env node

/**
 * Figma Design Comparison MCP Server
 * WebSocket을 통해 Cursor Talk To Figma Plugin과 통신
 */

const { Server } = require('@modelcontextprotocol/sdk/server/index.js');
const { StdioServerTransport } = require('@modelcontextprotocol/sdk/server/stdio.js');
const {
  CallToolRequestSchema,
  ListToolsRequestSchema,
} = require('@modelcontextprotocol/sdk/types.js');
const WebSocket = require('ws');
const fs = require('fs').promises;
const path = require('path');

const FIGMA_WS_PORT = process.env.FIGMA_WS_PORT || '3055';
const FIGMA_WS_URL = `ws://localhost:${FIGMA_WS_PORT}`;

class FigmaMCPServer {
  constructor() {
    this.server = new Server(
      {
        name: 'figma-design-comparison',
        version: '1.0.0',
      },
      {
        capabilities: {
          tools: {},
        },
      }
    );

    this.setupHandlers();
  }

  setupHandlers() {
    // 도구 목록 제공
    this.server.setRequestHandler(ListToolsRequestSchema, async () => ({
      tools: [
        {
          name: 'get_figma_design',
          description: 'Figma에서 디자인 이미지를 가져옵니다',
          inputSchema: {
            type: 'object',
            properties: {
              nodeId: {
                type: 'string',
                description: 'Figma 노드 ID',
              },
              viewport: {
                type: 'string',
                enum: ['mobile', 'tablet', 'desktop'],
                description: '뷰포트 크기',
              },
            },
            required: ['nodeId', 'viewport'],
          },
        },
        {
          name: 'compare_with_implementation',
          description: 'Figma 디자인과 실제 구현을 비교합니다',
          inputSchema: {
            type: 'object',
            properties: {
              pageName: {
                type: 'string',
                description: '페이지 이름 (예: home, login, signup)',
              },
              nodeId: {
                type: 'string',
                description: 'Figma 노드 ID',
              },
              viewport: {
                type: 'string',
                enum: ['mobile', 'tablet', 'desktop'],
                description: '뷰포트 크기',
              },
              url: {
                type: 'string',
                description: '비교할 페이지 URL',
              },
            },
            required: ['pageName', 'nodeId', 'viewport', 'url'],
          },
        },
        {
          name: 'update_baseline',
          description: '베이스라인 이미지를 업데이트합니다',
          inputSchema: {
            type: 'object',
            properties: {
              pageName: {
                type: 'string',
                description: '페이지 이름',
              },
              viewport: {
                type: 'string',
                enum: ['mobile', 'tablet', 'desktop'],
                description: '뷰포트 크기',
              },
              source: {
                type: 'string',
                enum: ['figma', 'implementation'],
                description: '베이스라인 소스 (figma 또는 implementation)',
              },
            },
            required: ['pageName', 'viewport', 'source'],
          },
        },
      ],
    }));

    // 도구 실행
    this.server.setRequestHandler(CallToolRequestSchema, async (request) => {
      const { name, arguments: args } = request.params;

      try {
        switch (name) {
          case 'get_figma_design':
            return await this.getFigmaDesign(args.nodeId, args.viewport);

          case 'compare_with_implementation':
            return await this.compareWithImplementation(
              args.pageName,
              args.nodeId,
              args.viewport,
              args.url
            );

          case 'update_baseline':
            return await this.updateBaseline(
              args.pageName,
              args.viewport,
              args.source
            );

          default:
            throw new Error(`Unknown tool: ${name}`);
        }
      } catch (error) {
        return {
          content: [
            {
              type: 'text',
              text: `Error: ${error.message}`,
            },
          ],
          isError: true,
        };
      }
    });
  }

  async getFigmaDesign(nodeId, viewport) {
    return new Promise(async (resolve, reject) => {
      const ws = new WebSocket(FIGMA_WS_URL);

      const timeout = setTimeout(() => {
        ws.close();
        reject(new Error('Figma WebSocket connection timeout'));
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

      ws.on('message', async (data) => {
        try {
          const message = JSON.parse(data.toString());
          if (message.type === 'export-complete') {
            clearTimeout(timeout);
            ws.close();

            // 이미지를 파일로 저장
            const imageBuffer = Buffer.from(message.image, 'base64');
            const outputDir = path.join(
              process.cwd(),
              'tests',
              'figma-exports',
              viewport
            );
            await fs.mkdir(outputDir, { recursive: true });
            const outputPath = path.join(outputDir, `${nodeId}.png`);
            await fs.writeFile(outputPath, imageBuffer);

            resolve({
              content: [
                {
                  type: 'text',
                  text: `Figma 디자인을 성공적으로 가져왔습니다.\n저장 경로: ${outputPath}`,
                },
              ],
            });
          }
        } catch (error) {
          clearTimeout(timeout);
          ws.close();
          reject(error);
        }
      });

      ws.on('error', (error) => {
        clearTimeout(timeout);
        reject(error);
      });
    });
  }

  async compareWithImplementation(pageName, nodeId, viewport, url) {
    // Playwright를 사용하여 실제 페이지 스크린샷 캡처
    // 이 부분은 별도의 스크립트로 실행되어야 함
    return {
      content: [
        {
          type: 'text',
          text: `비교 테스트를 실행하려면 다음 명령어를 사용하세요:\nnpm run test:visual -- --grep "${pageName}"`,
        },
      ],
    };
  }

  async updateBaseline(pageName, viewport, source) {
    const baselineDir = path.join(process.cwd(), 'tests', 'baselines', pageName);
    await fs.mkdir(baselineDir, { recursive: true });

    if (source === 'figma') {
      // Figma에서 가져온 이미지를 베이스라인으로 사용
      return {
        content: [
          {
            type: 'text',
            text: `Figma 디자인을 베이스라인으로 업데이트하려면 get_figma_design을 먼저 실행하세요.`,
          },
        ],
      };
    } else {
      // 실제 구현을 베이스라인으로 사용
      return {
        content: [
          {
            type: 'text',
            text: `실제 구현을 베이스라인으로 업데이트하려면 Playwright 테스트를 실행하세요.`,
          },
        ],
      };
    }
  }

  async run() {
    const transport = new StdioServerTransport();
    await this.server.connect(transport);
    console.error('Figma Design Comparison MCP Server running on stdio');
  }
}

const server = new FigmaMCPServer();
server.run().catch(console.error);

