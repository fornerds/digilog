import { ref, computed } from 'vue'

export interface PaginationParams {
  page: number
  limit: number
}

export interface PaginationMeta {
  page: number
  limit: number
  total: number
  totalPages: number
}

export const usePagination = (initialLimit = 10) => {
  const page = ref(1)
  const limit = ref(initialLimit)
  const total = ref(0)
  const totalPages = ref(0)

  const pagination = computed<PaginationMeta>(() => ({
    page: page.value,
    limit: limit.value,
    total: total.value,
    totalPages: totalPages.value,
  }))

  const setPage = (newPage: number) => {
    page.value = newPage
  }

  const setLimit = (newLimit: number) => {
    limit.value = newLimit
    page.value = 1 // limit 변경 시 첫 페이지로
  }

  const setPagination = (meta: PaginationMeta) => {
    page.value = meta.page
    limit.value = meta.limit
    total.value = meta.total
    totalPages.value = meta.totalPages
  }

  const reset = () => {
    page.value = 1
    limit.value = initialLimit
    total.value = 0
    totalPages.value = 0
  }

  const hasNextPage = computed(() => page.value < totalPages.value)
  const hasPrevPage = computed(() => page.value > 1)

  const nextPage = () => {
    if (hasNextPage.value) {
      page.value++
    }
  }

  const prevPage = () => {
    if (hasPrevPage.value) {
      page.value--
    }
  }

  return {
    page,
    limit,
    total,
    totalPages,
    pagination,
    hasNextPage,
    hasPrevPage,
    setPage,
    setLimit,
    setPagination,
    reset,
    nextPage,
    prevPage,
  }
}


