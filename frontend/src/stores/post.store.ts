import { defineStore } from 'pinia'
import { ref } from 'vue'
import {
  getPosts,
  getPost,
  createPost,
  updatePost,
  deletePost,
  type PostQueryParams,
  type CreatePostRequest,
  type UpdatePostRequest,
} from '@/api/post.api'
import type { Post } from '@/types/post.types'

export const usePostStore = defineStore('post', () => {
  // State
  const posts = ref<Post[]>([])
  const currentPost = ref<Post | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)
  const pagination = ref({
    page: 1,
    limit: 10,
    total: 0,
    totalPages: 0,
  })

  // Actions
  const fetchPosts = async (params?: PostQueryParams) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await getPosts(params)
      if (response.success) {
        posts.value = response.data.posts
        pagination.value = response.data.pagination
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '게시글 목록을 불러오는데 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const fetchPost = async (id: number) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await getPost(id)
      if (response.success) {
        currentPost.value = response.data
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '게시글을 불러오는데 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const createNewPost = async (data: CreatePostRequest) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await createPost(data)
      if (response.success) {
        // 목록에 새 게시글 추가
        posts.value.unshift(response.data)
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '게시글 작성에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const updateExistingPost = async (id: number, data: UpdatePostRequest) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await updatePost(id, data)
      if (response.success) {
        // 목록에서 게시글 업데이트
        const index = posts.value.findIndex((p) => p.id === id)
        if (index !== -1) {
          posts.value[index] = response.data
        }
        // 현재 게시글도 업데이트
        if (currentPost.value?.id === id) {
          currentPost.value = response.data
        }
        return response.data
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '게시글 수정에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const removePost = async (id: number) => {
    isLoading.value = true
    error.value = null

    try {
      const response = await deletePost(id)
      if (response.success) {
        // 목록에서 게시글 제거
        posts.value = posts.value.filter((p) => p.id !== id)
        // 현재 게시글도 초기화
        if (currentPost.value?.id === id) {
          currentPost.value = null
        }
        return response
      }
    } catch (err: any) {
      error.value = err.response?.data?.error?.message || '게시글 삭제에 실패했습니다.'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const clearCurrentPost = () => {
    currentPost.value = null
  }

  return {
    // State
    posts,
    currentPost,
    isLoading,
    error,
    pagination,
    // Actions
    fetchPosts,
    fetchPost,
    createNewPost,
    updateExistingPost,
    removePost,
    clearCurrentPost,
  }
})


