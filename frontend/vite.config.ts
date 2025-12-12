import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'
import svgLoader from 'vite-svg-loader'

// https://vitejs.dev/config/
export default defineConfig({
  base: '/digilog/',
  plugins: [
    vue(),
    svgLoader(), // SVG를 Vue 컴포넌트로 사용할 수 있게 해주는 플러그인
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:18080',
        changeOrigin: true,
      },
    },
  },
})


