export interface AnalysisScores {
  pores: number
  blackheads: number
  pigmentation: number
  wrinkles: number
  porphyrin: number
  sensitivity: number
  darkCircles: number
}

export interface SkinAnalysisReport {
  id: number
  userId: number
  userName: string
  userAge: number
  skinAge: number
  skinCondition: string
  skinConditionDescription: string
  skinCode: string
  skinType: string
  analysisScores: AnalysisScores
  skinTags: string[]
  skinCodeDescription: string
  careTips: string[]
  createdAt: string
  updatedAt: string
}

export interface ReportListResponse {
  success: boolean
  data: {
    reports: Omit<SkinAnalysisReport, 'userAge' | 'skinCondition' | 'skinConditionDescription' | 'analysisScores' | 'skinTags' | 'skinCodeDescription' | 'careTips'>[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}

export interface ReportQueryParams {
  page?: number
  limit?: number
  sortBy?: 'createdAt' | 'updatedAt'
  order?: 'asc' | 'desc'
}


