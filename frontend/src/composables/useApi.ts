import { ref } from 'vue'
import type { AxiosError } from 'axios'

export interface UseApiOptions {
  immediate?: boolean
  onError?: (error: AxiosError) => void
}

export const useApi = <T>(
  apiFunction: () => Promise<T>,
  options: UseApiOptions = {}
) => {
  const { immediate = false, onError } = options

  const data = ref<T | null>(null)
  const isLoading = ref(false)
  const error = ref<AxiosError | null>(null)

  const execute = async () => {
    isLoading.value = true
    error.value = null

    try {
      const result = await apiFunction()
      data.value = result
      return result
    } catch (err) {
      const axiosError = err as AxiosError
      error.value = axiosError
      if (onError) {
        onError(axiosError)
      }
      throw axiosError
    } finally {
      isLoading.value = false
    }
  }

  if (immediate) {
    execute()
  }

  return {
    data,
    isLoading,
    error,
    execute,
  }
}


