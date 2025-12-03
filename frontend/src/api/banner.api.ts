import apiClient from './client'
import type { Banner, BannerListResponse } from '@/types/banner.types'

/**
 * 배너 목록 조회
 */
export const getBanners = async (): Promise<BannerListResponse> => {
  const response = await apiClient.get<BannerListResponse>('/banners')
  return response.data
}


