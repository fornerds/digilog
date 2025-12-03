<template>
  <div class="login-page py-8">
    <div class="w-full px-4">
      <!-- 상단 인사말 -->
      <div class="mb-12">
        <h1 class="greeting-text font-bold">안녕하세요!<br />디지로그입니다.</h1>
      </div>
      
      <form @submit.prevent="handleSubmit" class="space-y-12">
        <div>
          <!-- 이메일 -->
          <EmailInput
            v-model="formData.email"
            :error="errors.email"
            :required="false"
            @blur="validateField('email')"
          />
          
          <!-- 비밀번호 -->
          <PasswordInput
            v-model="formData.password"
            :error="errors.password"
            :required="false"
            label=""
            @blur="validateField('password')"
          />
          
          <!-- 로그인 버튼 -->
          <div class="pt-4">
            <button
              type="submit"
              :disabled="!isFormValid"
              class="w-full py-3.5 px-4 rounded-lg text-base font-bold text-white transition-all disabled:opacity-50 disabled:cursor-not-allowed"
              :style="{ backgroundColor: isFormValid ? '#B59A79' : '#9CA3AF' }"
            >
              로그인
            </button>
          </div>
          
          <!-- 비밀번호 찾기와 회원가입 링크 -->
          <div class="flex items-center justify-between pt-2 ">
            <RouterLink
              to="/auth/find-password"
              class="text-sm hover:opacity-80 transition-opacity underline"
              style="color: #8C857C; text-underline-offset: 4px"
            >
              비밀번호 찾기
            </RouterLink>
            <RouterLink
              to="/auth/signup"
              class="text-sm hover:opacity-80 transition-opacity underline"
              style="color: #8C857C; text-underline-offset: 4px"
            >
              회원가입
            </RouterLink>
          </div>
        </div>
        
        <div>
        <!-- 소셜 로그인 구분선 -->
        <div class="relative py-4">
          <div class="absolute inset-0 flex items-center">
            <div class="w-full border-t" style="border-color: #8C857C"></div>
          </div>
          <div class="relative flex justify-center text-sm">
            <span class="px-2 bg-white" style="color: #8C857C">소셜 계정으로 간편 로그인</span>
          </div>
        </div>
        
        <!-- 소셜 로그인 버튼들 -->
        <div class="space-y-3">
          <button
            type="button"
            @click="handleNaverLogin"
            class="w-full py-3.5 px-4 rounded-lg text-base font-bold font-[16px] text-white transition-all flex items-center justify-center"
            style="background-color: #03C75A; gap: 16px"
          >
            <Icon name="naver" :size="20" color="#FFFFFF" />
            네이버로 로그인
          </button>
          
          <button
            type="button"
            @click="handleKakaoLogin"
            class="w-full py-3.5 px-4 rounded-lg text-base font-bold font-[16px] transition-all flex items-center justify-center"
            style="background-color: #FEE500; color: #000000; gap: 16px"
          >
            <Icon name="kakao" :size="18" color="#000000" />
            카카오로 로그인
          </button>
        </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { EmailInput, PasswordInput } from '@/components/common/Input'
import { useAuth } from '@/composables/useAuth'
import { validateEmail } from '@/utils/validation'
import Icon from '@/components/common/Icon/Icon.vue'
import { RouterLink } from 'vue-router'

const router = useRouter()
const { login, loginWithSocial } = useAuth()

interface FormData {
  email: string
  password: string
}

const formData = ref<FormData>({
  email: '',
  password: '',
})

const errors = ref<Partial<Record<keyof FormData, string>>>({})

const validateField = (field: keyof FormData) => {
  switch (field) {
    case 'email':
      const emailResult = validateEmail(formData.value.email)
      errors.value.email = emailResult.isValid ? undefined : emailResult.error
      break
    case 'password':
      errors.value.password = formData.value.password
        ? undefined
        : '비밀번호를 입력해주세요.'
      break
  }
}

const isFormValid = computed(() => {
  return (
    formData.value.email &&
    formData.value.password &&
    !errors.value.email &&
    !errors.value.password
  )
})

const handleSubmit = async () => {
  // 모든 필드 검증
  Object.keys(formData.value).forEach((key) => {
    validateField(key as keyof FormData)
  })

  if (!isFormValid.value) {
    return
  }

  try {
    await login({
      email: formData.value.email,
      password: formData.value.password,
    })
    // 로그인 성공 시 홈으로 이동 (useAuth에서 처리)
  } catch (error: any) {
    console.error('로그인 실패:', error)
    if (error.response?.data?.message) {
      errors.value.email = error.response.data.message
    } else {
      errors.value.email = '이메일 또는 비밀번호가 올바르지 않습니다.'
    }
  }
}

const handleNaverLogin = async () => {
  try {
    // TODO: 네이버 로그인 SDK 연동
    console.log('네이버 로그인')
    // await loginWithSocial({ provider: 'naver', accessToken: '...' })
  } catch (error) {
    console.error('네이버 로그인 실패:', error)
  }
}

const handleKakaoLogin = async () => {
  try {
    // TODO: 카카오 로그인 SDK 연동
    console.log('카카오 로그인')
    // await loginWithSocial({ provider: 'kakao', accessToken: '...' })
  } catch (error) {
    console.error('카카오 로그인 실패:', error)
  }
}
</script>

<style scoped>
.login-page {
  font-family: 'SUIT', sans-serif;
}

.greeting-text {
  color: #374151;
  font-family: SUIT, sans-serif;
  font-size: 24px;
  font-style: normal;
  font-weight: 700;
  line-height: 150%; /* 36px */
}
</style>
