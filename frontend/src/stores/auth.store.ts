import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, socialLogin, type LoginRequest, type SocialLoginRequest } from '@/api/auth.api'
import { getCurrentUser } from '@/api/user.api'
import type { User } from '@/types/user.types'

export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref<string | null>(localStorage.getItem('token') || null)
  const user = ref<User | null>(null)
  const isAuthenticated = computed(() => !!token.value && !!user.value)

  // Actions
  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUser = (userData: User) => {
    user.value = userData
  }

  const loginUser = async (credentials: LoginRequest) => {
    try {
      const response = await login(credentials)
      if (response.success && response.data.token) {
        setToken(response.data.token)
        setUser(response.data.user)
        return response
      }
      throw new Error('로그인 실패')
    } catch (error) {
      console.error('Login error:', error)
      throw error
    }
  }

  const loginWithSocial = async (data: SocialLoginRequest) => {
    try {
      const response = await socialLogin(data)
      if (response.success && response.data.token) {
        setToken(response.data.token)
        setUser(response.data.user)
        return response
      }
      throw new Error('소셜 로그인 실패')
    } catch (error) {
      console.error('Social login error:', error)
      throw error
    }
  }

  const fetchCurrentUser = async () => {
    try {
      const response = await getCurrentUser()
      if (response.success) {
        setUser(response.data)
        return response.data
      }
    } catch (error) {
      console.error('Fetch user error:', error)
      logout()
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  // 초기화: 토큰이 있으면 사용자 정보 가져오기
  const initialize = async () => {
    if (token.value) {
      await fetchCurrentUser()
    }
  }

  return {
    // State
    token,
    user,
    isAuthenticated,
    // Actions
    loginUser,
    loginWithSocial,
    fetchCurrentUser,
    logout,
    initialize,
  }
})


