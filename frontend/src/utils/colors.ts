/**
 * 프로젝트 커스텀 색상 상수
 * TypeScript에서 타입 안전하게 색상을 사용할 수 있도록 정의
 */

export const Colors = {
  brown: '#8C704E',
  tan: '#A9745A',
  cream: '#FFF5CC',
  sky: '#C9D7F8',
  navy: '#61627F',
  pink: '#FFEDED',
  peach: '#FFEBEB',
  burgundy: '#B0003A',
} as const

export type ColorName = keyof typeof Colors

/**
 * 색상 이름으로 색상 값을 가져오는 헬퍼 함수
 */
export function getColor(name: ColorName): string {
  return Colors[name]
}

/**
 * CSS 변수 이름으로 색상 값을 가져오는 헬퍼 함수
 */
export function getColorVar(name: ColorName): string {
  return `var(--color-digilog-${name})`
}

/**
 * Tailwind 클래스 이름을 생성하는 헬퍼 함수
 */
export function getTailwindColor(name: ColorName, shade?: 'bg' | 'text' | 'border'): string {
  const prefix = shade || 'bg'
  return `${prefix}-digilog-${name}`
}

