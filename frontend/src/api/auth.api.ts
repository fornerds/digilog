import apiClient from './client'
import type {
  RegisterRequest,
  RegisterResponse,
  LoginRequest,
  LoginResponse,
  SocialLoginRequest,
  SocialLoginResponse,
} from '@/types/auth.types'

/**
 * 회원가입
 */
export const register = async (data: RegisterRequest): Promise<RegisterResponse> => {
  const response = await apiClient.post<RegisterResponse>('/auth/register', data)
  return response.data
}

/**
 * 로그인
 */
export const login = async (data: LoginRequest): Promise<LoginResponse> => {
  const response = await apiClient.post<LoginResponse>('/auth/login', data)
  return response.data
}

/**
 * 소셜 로그인
 */
export const socialLogin = async (data: SocialLoginRequest): Promise<SocialLoginResponse> => {
  const response = await apiClient.post<SocialLoginResponse>('/auth/social', data)
  return response.data
}


