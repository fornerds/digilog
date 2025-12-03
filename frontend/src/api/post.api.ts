import apiClient from './client'
import type {
  Post,
  PostListResponse,
  CreatePostRequest,
  CreatePostResponse,
  UpdatePostRequest,
  UpdatePostResponse,
  CheckProfanityRequest,
  CheckProfanityResponse,
  UploadImagesResponse,
  PostQueryParams,
} from '@/types/post.types'

/**
 * 게시글 목록 조회
 */
export const getPosts = async (
  params?: PostQueryParams
): Promise<PostListResponse> => {
  const response = await apiClient.get<PostListResponse>('/posts', { params })
  return response.data
}

/**
 * 게시글 상세 조회
 */
export const getPost = async (id: number): Promise<{ success: boolean; data: Post }> => {
  const response = await apiClient.get<{ success: boolean; data: Post }>(`/posts/${id}`)
  return response.data
}

/**
 * 게시글 사진 업로드
 */
export const uploadPostImages = async (
  images: File[]
): Promise<UploadImagesResponse> => {
  const formData = new FormData()
  images.forEach((image) => {
    formData.append('images', image)
  })

  const response = await apiClient.post<UploadImagesResponse>(
    '/posts/images',
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
 * 게시글 비속어 검사
 */
export const checkProfanity = async (
  data: CheckProfanityRequest
): Promise<CheckProfanityResponse> => {
  const response = await apiClient.post<CheckProfanityResponse>(
    '/posts/check-profanity',
    data
  )
  return response.data
}

/**
 * 게시글 생성
 */
export const createPost = async (
  data: CreatePostRequest
): Promise<CreatePostResponse> => {
  const response = await apiClient.post<CreatePostResponse>('/posts', data)
  return response.data
}

/**
 * 게시글 수정 비속어 검사
 */
export const checkPostProfanity = async (
  id: number,
  data: CheckProfanityRequest
): Promise<CheckProfanityResponse> => {
  const response = await apiClient.post<CheckProfanityResponse>(
    `/posts/${id}/check-profanity`,
    data
  )
  return response.data
}

/**
 * 게시글 수정
 */
export const updatePost = async (
  id: number,
  data: UpdatePostRequest
): Promise<UpdatePostResponse> => {
  const response = await apiClient.put<UpdatePostResponse>(`/posts/${id}`, data)
  return response.data
}

/**
 * 게시글 삭제
 */
export const deletePost = async (id: number): Promise<{ success: boolean; message: string }> => {
  const response = await apiClient.delete<{ success: boolean; message: string }>(
    `/posts/${id}`
  )
  return response.data
}


