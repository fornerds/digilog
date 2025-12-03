export interface Product {
  id: number
  name: string
  brand: string
  price: number
  imageUrl: string
  url: string
  category?: string | null
  tags: string[]
  skinCodes: string[]
  personalColors: string[]
  isWished?: boolean
  createdAt: string
  updatedAt: string
}

export interface ProductListResponse {
  success: boolean
  data: {
    products: Product[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}

export interface ProductQueryParams {
  page?: number
  limit?: number
  skinCode?: string
  personalColor?: string
}


