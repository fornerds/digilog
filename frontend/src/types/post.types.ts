export interface Post {
  id: number
  title: string
  content: string
  authorId: number
  authorName: string
  authorSkinType?: string | null
  hashtags: string[]
  images: string[]
  commentCount?: number
  likeCount?: number
  createdAt: string
  updatedAt: string
}

export interface PostListResponse {
  success: boolean
  data: {
    posts: Post[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}

export interface PostQueryParams {
  page?: number
  limit?: number
  search?: string
  sortBy?: 'createdAt' | 'updatedAt' | 'title'
  order?: 'asc' | 'desc' | 'popularity'
}

export interface CreatePostRequest {
  title: string
  content: string
  hashtags?: string[]
  imageUrls?: string[]
  forcePublish?: boolean
}

export interface CreatePostResponse {
  success: boolean
  data: Post
}

export interface UpdatePostRequest {
  title?: string
  content?: string
  hashtags?: string[]
  imageUrls?: string[]
  forcePublish?: boolean
}

export interface UpdatePostResponse {
  success: boolean
  data: Post
}

export interface CheckProfanityRequest {
  title: string
  content: string
  hashtags?: string[]
}

export interface CheckProfanityResponse {
  success: boolean
  data: {
    hasProfanity: boolean
    detectedWords: string[]
    message: string
  }
}

export interface UploadImagesResponse {
  success: boolean
  data: {
    imageUrls: string[]
  }
}


