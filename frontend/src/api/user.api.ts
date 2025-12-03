import apiClient from './client'
import type {
  User,
  UpdateUserRequest,
  UpdateUserResponse,
  UploadProfileImageResponse,
} from '@/types/user.types'

/**
 * 현재 로그인한 사용자 정보 조회
 */
export const getCurrentUser = async (): Promise<{ success: boolean; data: User }> => {
  const response = await apiClient.get<{ success: boolean; data: User }>('/users/me')
  return response.data
}

/**
 * 사용자 정보 수정
 */
export const updateUser = async (
  data: UpdateUserRequest
): Promise<UpdateUserResponse> => {
  const response = await apiClient.put<UpdateUserResponse>('/users/me', data)
  return response.data
}

/**
 * 회원 탈퇴
 */
export const deleteUser = async (password?: string): Promise<{ success: boolean; message: string }> => {
  const response = await apiClient.delete<{ success: boolean; message: string }>('/users/me', {
    data: password ? { password } : undefined,
  })
  return response.data
}

/**
 * 프로필 이미지 업로드/수정
 */
export const uploadProfileImage = async (
  imageFile: File
): Promise<UploadProfileImageResponse> => {
  const formData = new FormData()
  formData.append('image', imageFile)

  const response = await apiClient.put<UploadProfileImageResponse>(
    '/users/me/profile-image',
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
  )
  return response.data
}

/**
 * 프로필 이미지 삭제
 */
export const deleteProfileImage = async (): Promise<{ success: boolean; message: string }> => {
  const response = await apiClient.delete<{ success: boolean; message: string }>(
    '/users/me/profile-image'
  )
  return response.data
}


