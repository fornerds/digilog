// API 관련 상수
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:18080/api'
export const API_VERSION = 'v1'

// 페이지네이션 기본값
export const DEFAULT_PAGE = 1
export const DEFAULT_LIMIT = 10

// 파일 업로드 제한
export const MAX_FILE_SIZE = 5 * 1024 * 1024 // 5MB
export const ALLOWED_IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif']

// 성별 옵션
export const GENDER_OPTIONS = [
  { value: 'male', label: '남성' },
  { value: 'female', label: '여성' },
  { value: 'other', label: '기타' },
] as const

// 퍼스널컬러 타입
export const PERSONAL_COLOR_TYPES = ['봄웜톤', '여름쿨톤', '가을웜톤', '겨울쿨톤'] as const

// 공지/이벤트 타입
export const NOTICE_TYPES = ['notice', 'event'] as const


