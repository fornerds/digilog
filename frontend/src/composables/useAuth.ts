import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import type { LoginRequest, SocialLoginRequest } from '@/types/auth.types'

export const useAuth = () => {
  const router = useRouter()
  const authStore = useAuthStore()

  const isAuthenticated = computed(() => authStore.isAuthenticated)
  const user = computed(() => authStore.user)
  const token = computed(() => authStore.token)

  const login = async (credentials: LoginRequest) => {
    try {
      const response = await authStore.loginUser(credentials)
      const redirect = router.currentRoute.value.query.redirect as string
      router.push(redirect || '/')
      return response
    } catch (error) {
      throw error
    }
  }

  const loginWithSocial = async (data: SocialLoginRequest) => {
    try {
      const response = await authStore.loginWithSocial(data)
      const redirect = router.currentRoute.value.query.redirect as string
      router.push(redirect || '/')
      return response
    } catch (error) {
      throw error
    }
  }

  const logout = () => {
    authStore.logout()
    router.push('/auth/login')
  }

  return {
    isAuthenticated,
    user,
    token,
    login,
    loginWithSocial,
    logout,
  }
}


