export interface Comment {
  id: number
  userId: number
  userName: string
  userProfileImageUrl?: string | null
  userSkinType?: string | null
  content: string
  createdAt: string
  updatedAt: string
}

export interface CommentListResponse {
  success: boolean
  data: {
    comments: Comment[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}

export interface CommentQueryParams {
  page?: number
  limit?: number
  sortBy?: 'createdAt' | 'updatedAt'
  order?: 'asc' | 'desc'
}

export interface CreateCommentRequest {
  content: string
}

export interface CreateCommentResponse {
  success: boolean
  data: Comment
}

export interface UpdateCommentRequest {
  content: string
}

export interface UpdateCommentResponse {
  success: boolean
  data: Comment
}


