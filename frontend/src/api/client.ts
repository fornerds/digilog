import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { useAuthStore } from '@/stores/auth.store'

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:18080/api'
const API_VERSION = 'v1'

// Axios 인스턴스 생성
const apiClient: AxiosInstance = axios.create({
  baseURL: `${BASE_URL}/${API_VERSION}`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

// 요청 인터셉터: 토큰 추가
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const authStore = useAuthStore()
    const token = authStore.token

    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

// 응답 인터셉터: 에러 처리
apiClient.interceptors.response.use(
  (response) => {
    return response
  },
  async (error: AxiosError) => {
    const authStore = useAuthStore()

    // 401 Unauthorized: 토큰 만료 또는 유효하지 않음
    if (error.response?.status === 401) {
      authStore.logout()
      // 로그인 페이지로 리다이렉트
      window.location.href = '/auth/login'
    }

    return Promise.reject(error)
  }
)

export default apiClient


