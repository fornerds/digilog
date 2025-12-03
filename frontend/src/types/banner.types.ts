export interface Banner {
  id: number
  title: string
  description: string
  imageUrl: string
  createdAt: string
  updatedAt: string
}

export interface BannerListResponse {
  success: boolean
  data: {
    banners: Banner[]
  }
}


