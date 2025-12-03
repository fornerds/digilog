import apiClient from './client'
import type {
  Comment,
  CommentListResponse,
  CreateCommentRequest,
  CreateCommentResponse,
  UpdateCommentRequest,
  UpdateCommentResponse,
  CommentQueryParams,
} from '@/types/comment.types'

/**
 * 게시글 댓글 조회
 */
export const getComments = async (
  postId: number,
  params?: CommentQueryParams
): Promise<CommentListResponse> => {
  const response = await apiClient.get<CommentListResponse>(
    `/posts/${postId}/comments`,
    { params }
  )
  return response.data
}

/**
 * 댓글 생성
 */
export const createComment = async (
  postId: number,
  data: CreateCommentRequest
): Promise<CreateCommentResponse> => {
  const response = await apiClient.post<CreateCommentResponse>(
    `/posts/${postId}/comments`,
    data
  )
  return response.data
}

/**
 * 댓글 수정
 * Note: API 명세서에 댓글 수정/삭제 엔드포인트가 명시되어 있지 않지만,
 * 일반적인 RESTful 패턴을 따라 구현
 */
export const updateComment = async (
  postId: number,
  commentId: number,
  data: UpdateCommentRequest
): Promise<UpdateCommentResponse> => {
  const response = await apiClient.put<UpdateCommentResponse>(
    `/posts/${postId}/comments/${commentId}`,
    data
  )
  return response.data
}

/**
 * 댓글 삭제
 */
export const deleteComment = async (
  postId: number,
  commentId: number
): Promise<{ success: boolean; message: string }> => {
  const response = await apiClient.delete<{ success: boolean; message: string }>(
    `/posts/${postId}/comments/${commentId}`
  )
  return response.data
}

