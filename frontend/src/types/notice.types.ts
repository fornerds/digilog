export interface Notice {
  id: number
  type: 'notice' | 'event'
  title: string
  content: string
  images: string[]
  links: string[]
  startDate?: string | null
  endDate?: string | null
  createdAt: string
  updatedAt: string
}

export interface NoticeListResponse {
  success: boolean
  data: {
    notices: Notice[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}

export interface NoticeQueryParams {
  page?: number
  limit?: number
  search?: string
  type?: 'notice' | 'event'
  sortBy?: 'createdAt' | 'startDate' | 'endDate' | 'title'
  order?: 'asc' | 'desc'
}


