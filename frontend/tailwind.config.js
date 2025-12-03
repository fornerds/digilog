/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: [
          'SUIT',
          '-apple-system',
          'BlinkMacSystemFont',
          'Segoe UI',
          'Roboto',
          'Oxygen',
          'Ubuntu',
          'Cantarell',
          'Fira Sans',
          'Droid Sans',
          'Helvetica Neue',
          'sans-serif',
        ],
      },
      colors: {
        // 프로젝트 커스텀 색상
        digilog: {
          brown: '#8C704E',
          tan: '#A9745A',
          cream: '#FFF5CC',
          sky: '#C9D7F8',
          navy: '#61627F',
          pink: '#FFEDED',
          peach: '#FFEBEB',
          burgundy: '#B0003A',
        },
        // 기존 primary 색상 (필요시 유지)
        primary: {
          50: '#f0f9ff',
          100: '#e0f2fe',
          200: '#bae6fd',
          300: '#7dd3fc',
          400: '#38bdf8',
          500: '#0ea5e9',
          600: '#0284c7',
          700: '#0369a1',
          800: '#075985',
          900: '#0c4a6e',
        },
      },
      transitionDuration: {
        '200': '200ms',
      },
    },
  },
  plugins: [],
}


