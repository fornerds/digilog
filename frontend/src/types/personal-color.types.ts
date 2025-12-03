export interface PersonalColorColor {
  id: number
  name: string
  hexCode: string
  category: '봄웜톤' | '여름쿨톤' | '가을웜톤' | '겨울쿨톤'
}

export interface LabValues {
  l: number
  a: number
  b: number
}

export interface MatchingColors {
  title: string
  description: string
  colors: PersonalColorColor[]
}

export interface PersonalColor {
  id: number
  userId: number
  personalColor: '봄웜톤' | '여름쿨톤' | '가을웜톤' | '겨울쿨톤'
  typePercentage: number
  typeDescriptions: string[]
  labValues: LabValues
  matchingColors: MatchingColors
  nonMatchingColors: MatchingColors
  contouringGuideUrl?: string | null
  makeupTips: string[]
  createdAt: string
  updatedAt: string
}

export interface PersonalColorListResponse {
  success: boolean
  data: {
    diagnoses: Omit<PersonalColor, 'typeDescriptions' | 'labValues' | 'matchingColors' | 'nonMatchingColors' | 'contouringGuideUrl' | 'makeupTips'>[]
    pagination: {
      page: number
      limit: number
      total: number
      totalPages: number
    }
  }
}


