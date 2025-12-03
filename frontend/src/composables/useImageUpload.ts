import { ref } from 'vue'
import { uploadPostImages } from '@/api/post.api'

export const useImageUpload = () => {
  const isUploading = ref(false)
  const uploadedUrls = ref<string[]>([])
  const error = ref<string | null>(null)

  const uploadImages = async (files: File[]) => {
    isUploading.value = true
    error.value = null
    uploadedUrls.value = []

    try {
      // 파일 크기 검증 (5MB)
      const maxSize = 5 * 1024 * 1024 // 5MB
      for (const file of files) {
        if (file.size > maxSize) {
          throw new Error(`파일 크기는 5MB 이하여야 합니다. (${file.name})`)
        }
      }

      // 파일 형식 검증
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif']
      for (const file of files) {
        if (!allowedTypes.includes(file.type)) {
          throw new Error(`지원하지 않는 파일 형식입니다. (${file.name})`)
        }
      }

      const response = await uploadPostImages(files)
      if (response.success) {
        uploadedUrls.value = response.data.imageUrls
        return response.data.imageUrls
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || err.message || '이미지 업로드에 실패했습니다.'
      throw err
    } finally {
      isUploading.value = false
    }
  }

  const reset = () => {
    uploadedUrls.value = []
    error.value = null
  }

  return {
    isUploading,
    uploadedUrls,
    error,
    uploadImages,
    reset,
  }
}


