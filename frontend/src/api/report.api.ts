import apiClient from './client'
import type {
  SkinAnalysisReport,
  ReportListResponse,
  ReportQueryParams,
} from '@/types/report.types'

/**
 * 피부분석 보고서 목록 조회
 */
export const getReports = async (
  params?: ReportQueryParams
): Promise<ReportListResponse> => {
  const response = await apiClient.get<ReportListResponse>(
    '/skin-analysis-reports',
    { params }
  )
  return response.data
}

/**
 * 피부분석 보고서 상세 조회
 */
export const getReport = async (
  id: number
): Promise<{ success: boolean; data: SkinAnalysisReport }> => {
  const response = await apiClient.get<{ success: boolean; data: SkinAnalysisReport }>(
    `/skin-analysis-reports/${id}`
  )
  return response.data
}


