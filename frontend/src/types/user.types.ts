export interface User {
  id: number
  email: string
  name: string
  phone: string
  birthDate: string
  gender: 'male' | 'female' | 'other'
  profileImageUrl: string | null
  provider: 'local' | 'naver' | 'kakao'
  createdAt: string
  updatedAt: string
}

export interface UpdateUserRequest {
  name?: string
  phone?: string
  birthDate?: string
  gender?: 'male' | 'female' | 'other'
  email?: string
}

export interface UpdateUserResponse {
  success: boolean
  data: User
}

export interface UploadProfileImageResponse {
  success: boolean
  data: {
    profileImageUrl: string
    updatedAt: string
  }
}


