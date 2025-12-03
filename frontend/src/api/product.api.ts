import apiClient from './client'
import type {
  Product,
  ProductListResponse,
  ProductQueryParams,
} from '@/types/product.types'

/**
 * 피부코드 기반 제품 추천 조회
 */
export const getProductsBySkinCode = async (
  skinCode: string,
  params?: Omit<ProductQueryParams, 'skinCode'>
): Promise<ProductListResponse> => {
  const response = await apiClient.get<ProductListResponse>(
    '/products/recommended-by-skin-code',
    {
      params: { skinCode, ...params },
    }
  )
  return response.data
}

/**
 * 퍼스널컬러 기반 제품 추천 조회
 */
export const getProductsByPersonalColor = async (
  personalColor: string,
  params?: Omit<ProductQueryParams, 'personalColor'>
): Promise<ProductListResponse> => {
  const response = await apiClient.get<ProductListResponse>(
    '/products/recommended-by-personal-color',
    {
      params: { personalColor, ...params },
    }
  )
  return response.data
}

/**
 * 제품 찜 추가/취소
 */
export const toggleProductWish = async (
  id: number,
  isWished: boolean
): Promise<{
  success: boolean
  data: { productId: number; isWished: boolean; message: string }
}> => {
  const response = await apiClient.put<{
    success: boolean
    data: { productId: number; isWished: boolean; message: string }
  }>(`/products/${id}/wish`, { isWished })
  return response.data
}


