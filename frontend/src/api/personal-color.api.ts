import apiClient from './client'
import type {
  PersonalColor,
  PersonalColorColor,
  PersonalColorListResponse,
} from '@/types/personal-color.types'

/**
 * 퍼스널컬러 색상 조회
 */
export const getPersonalColorColors = async (): Promise<{
  success: boolean
  data: { colors: PersonalColorColor[] }
}> => {
  const response = await apiClient.get<{
    success: boolean
    data: { colors: PersonalColorColor[] }
  }>('/personal-colors/colors')
  return response.data
}

/**
 * 퍼스널컬러 상세 진단 조회
 */
export const getPersonalColor = async (
  id: number
): Promise<{ success: boolean; data: PersonalColor }> => {
  const response = await apiClient.get<{ success: boolean; data: PersonalColor }>(
    `/personal-colors/${id}`
  )
  return response.data
}


