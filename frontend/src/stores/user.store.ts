import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  updateUser,
  uploadProfileImage,
  deleteProfileImage,
  type UpdateUserRequest,
} from '@/api/user.api'
import type { User } from '@/types/user.types'

export const useUserStore = defineStore('user', () => {
  // State
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  // Actions
  const updateUserInfo = async (data: UpdateUserRequest) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await updateUser(data)
      if (response.success) {
        // auth store의 사용자 정보도 업데이트
        const authStore = useAuthStore()
        await authStore.fetchCurrentUser()
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '사용자 정보 수정에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const uploadProfile = async (imageFile: File) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await uploadProfileImage(imageFile)
      if (response.success) {
        // auth store의 사용자 정보도 업데이트
        const authStore = useAuthStore()
        await authStore.fetchCurrentUser()
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '프로필 이미지 업로드에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const removeProfileImage = async () => {
    isLoading.value = true
    error.value = null

    try {
      const response = await deleteProfileImage()
      if (response.success) {
        // auth store의 사용자 정보도 업데이트
        const authStore = useAuthStore()
        await authStore.fetchCurrentUser()
        return response
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '프로필 이미지 삭제에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  return {
    // State
    isLoading,
    error,
    // Actions
    updateUserInfo,
    uploadProfile,
    removeProfileImage,
  }
})

// auth store import를 위한 타입 참조
import { useAuthStore } from '@/stores/auth.store'


