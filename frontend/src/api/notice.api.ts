import apiClient from './client'
import type {
  Notice,
  NoticeListResponse,
  NoticeQueryParams,
} from '@/types/notice.types'

/**
 * 공지/이벤트 목록 조회
 */
export const getNotices = async (
  params?: NoticeQueryParams
): Promise<NoticeListResponse> => {
  const response = await apiClient.get<NoticeListResponse>('/notices', { params })
  return response.data
}

/**
 * 공지/이벤트 상세 조회
 */
export const getNotice = async (id: number): Promise<{ success: boolean; data: Notice }> => {
  const response = await apiClient.get<{ success: boolean; data: Notice }>(`/notices/${id}`)
  return response.data
}


