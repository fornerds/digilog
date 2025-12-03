export interface RegisterRequest {
  email: string
  name: string
  phone: string
  birthDate: string // YYYY-MM-DD
  gender: 'male' | 'female' | 'other'
  password: string
  passwordConfirm: string
}

export interface RegisterResponse {
  success: boolean
  data: {
    id: number
    email: string
    name: string
    phone: string
    birthDate: string
    gender: string
    createdAt: string
  }
}

export interface LoginRequest {
  email: string
  password: string
}

export interface LoginResponse {
  success: boolean
  data: {
    token: string
    user: {
      id: number
      email: string
      name: string
    }
  }
}

export interface SocialLoginRequest {
  provider: 'naver' | 'kakao'
  accessToken: string
}

export interface SocialLoginResponse {
  success: boolean
  data: {
    token: string
    user: {
      id: number
      email: string
      name: string
      provider: string
    }
    isNewUser: boolean
  }
}


