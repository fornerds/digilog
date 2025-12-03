import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

/**
 * 인증이 필요한 라우트 가드
 */
export const authGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()

  if (!authStore.isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
}

/**
 * 게스트 전용 라우트 가드 (로그인한 사용자는 접근 불가)
 */
export const guestGuard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalized,
  next: NavigationGuardNext
) => {
  const authStore = useAuthStore()

  if (authStore.isAuthenticated) {
    next({ name: 'Home' })
  } else {
    next()
  }
}


