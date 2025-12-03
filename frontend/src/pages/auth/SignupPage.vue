<template>
  <div class="signup-page py-8">
    <div class="w-full px-4">
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 이름 -->
        <NameInput
          v-model="formData.name"
          :error="errors.name"
          @blur="validateField('name')"
        />
        
        <!-- 성별 -->
        <GenderSelect
          v-model="formData.gender"
          :error="errors.gender"
          @update:model-value="formData.gender = $event"
        />
        
        <!-- 이메일 -->
        <EmailInput
          v-model="formData.email"
          :error="errors.email"
          @blur="validateField('email')"
        />
        
        <!-- 휴대폰번호 -->
        <PhoneInput
          v-model="formData.phone"
          :error="errors.phone"
          @blur="validateField('phone')"
        />
        
        <!-- 생년월일 -->
        <BirthDateInput
          v-model="formData.birthDate"
          :error="errors.birthDate"
          @blur="validateField('birthDate')"
        />
        
        <!-- 비밀번호 -->
        <PasswordInput
          v-model="formData.password"
          :error="errors.password"
          hint="영문, 숫자, 특수문자 중 2가지 이상 조합 (8-20자)"
          @blur="validateField('password')"
        />
        
        <!-- 비밀번호 확인 -->
        <PasswordConfirmInput
          v-model="formData.passwordConfirm"
          :error="errors.passwordConfirm"
          @blur="validateField('passwordConfirm')"
        />
        
        <!-- 이용약관 동의 -->
        <div class="pt-2 space-y-3">
          <TermsCheckbox
            id="terms"
            label="이용약관에 동의합니다."
            :model-value="formData.termsAgreed"
            :error="errors.termsAgreed"
            @update:model-value="formData.termsAgreed = $event"
            @link-click="handleTermsClick"
          />
          
          <TermsCheckbox
            id="privacy"
            label="개인정보처리방침에 동의합니다."
            :model-value="formData.privacyAgreed"
            :error="errors.privacyAgreed"
            @update:model-value="formData.privacyAgreed = $event"
            @link-click="handlePrivacyClick"
          />
          
          <TermsCheckbox
            id="marketing"
            label="마케팅 정보 수신에 동의합니다 (선택)"
            link-text=""
            :model-value="formData.marketingAgreed"
            :show-link="false"
            @update:model-value="formData.marketingAgreed = $event"
          />
        </div>
        
        <!-- 제출 버튼 -->
        <div class="pt-4">
          <button
            type="submit"
            :disabled="!isFormValid"
            class="w-full py-3.5 px-4 rounded-lg text-base font-medium text-white transition-all disabled:opacity-50 disabled:cursor-not-allowed"
            :style="{ backgroundColor: isFormValid ? '#374151' : '#9CA3AF' }"
          >
            회원가입
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  NameInput,
  EmailInput,
  PhoneInput,
  BirthDateInput,
  PasswordInput,
  PasswordConfirmInput,
} from '@/components/common/Input'
import { Button } from '@/components/common/Button'
import { TermsCheckbox } from '@/components/common/Checkbox'
import { GenderSelect } from '@/components/common/GenderSelect'
import {
  validateName,
  validateEmail,
  validatePhone,
  validateBirthDate,
  validatePassword,
  validatePasswordConfirm,
} from '@/utils/validation'

const router = useRouter()

interface FormData {
  name: string
  gender: 'female' | 'male' | ''
  email: string
  phone: string
  birthDate: string
  password: string
  passwordConfirm: string
  termsAgreed: boolean
  privacyAgreed: boolean
  marketingAgreed: boolean
}

const formData = ref<FormData>({
  name: '',
  gender: '',
  email: '',
  phone: '',
  birthDate: '',
  password: '',
  passwordConfirm: '',
  termsAgreed: false,
  privacyAgreed: false,
  marketingAgreed: false,
})

const errors = ref<Partial<Record<keyof FormData, string>>>({})

const validateField = (field: keyof FormData) => {
  switch (field) {
    case 'name':
      const nameResult = validateName(formData.value.name)
      errors.value.name = nameResult.isValid ? undefined : nameResult.error
      break
    case 'email':
      const emailResult = validateEmail(formData.value.email)
      errors.value.email = emailResult.isValid ? undefined : emailResult.error
      break
    case 'phone':
      const phoneResult = validatePhone(formData.value.phone)
      errors.value.phone = phoneResult.isValid ? undefined : phoneResult.error
      break
    case 'birthDate':
      const birthDateResult = validateBirthDate(formData.value.birthDate)
      errors.value.birthDate = birthDateResult.isValid ? undefined : birthDateResult.error
      break
    case 'password':
      const passwordResult = validatePassword(formData.value.password)
      errors.value.password = passwordResult.isValid ? undefined : passwordResult.error
      // 비밀번호가 변경되면 비밀번호 확인도 다시 검증
      if (passwordResult.isValid && formData.value.passwordConfirm) {
        validateField('passwordConfirm')
      }
      break
    case 'passwordConfirm':
      const passwordConfirmResult = validatePasswordConfirm(
        formData.value.password,
        formData.value.passwordConfirm
      )
      errors.value.passwordConfirm = passwordConfirmResult.isValid
        ? undefined
        : passwordConfirmResult.error
      break
  }
}

const isFormValid = computed(() => {
  // 모든 필드가 채워져 있고 에러가 없어야 함
  const hasAllFields =
    formData.value.name &&
    formData.value.gender &&
    formData.value.email &&
    formData.value.phone &&
    formData.value.birthDate &&
    formData.value.password &&
    formData.value.passwordConfirm

  // 필수 약관 동의 확인
  const hasRequiredAgreements =
    formData.value.termsAgreed && formData.value.privacyAgreed

  const hasNoErrors = Object.values(errors.value).every((error) => !error)

  return hasAllFields && hasRequiredAgreements && hasNoErrors
})

const handleSubmit = async () => {
  // 모든 필드 검증
  Object.keys(formData.value).forEach((key) => {
    validateField(key as keyof FormData)
  })

  if (!isFormValid.value) {
    return
  }

  // 필수 약관 동의 확인
  if (!formData.value.termsAgreed) {
    errors.value.termsAgreed = '이용약관에 동의해주세요.'
    return
  }
  if (!formData.value.privacyAgreed) {
    errors.value.privacyAgreed = '개인정보처리방침에 동의해주세요.'
    return
  }

  try {
    // TODO: API 호출
    console.log('회원가입 데이터:', formData.value)
    
    // 성공 시 로그인 페이지로 이동
    router.push('/auth/login')
  } catch (error) {
    console.error('회원가입 실패:', error)
  }
}

const handleTermsClick = () => {
  // TODO: 이용약관 모달 또는 페이지 열기
  console.log('이용약관 보기')
  // window.open('/terms', '_blank')
}

const handlePrivacyClick = () => {
  // TODO: 개인정보처리방침 모달 또는 페이지 열기
  console.log('개인정보처리방침 보기')
  // window.open('/privacy', '_blank')
}
</script>

<style scoped>
.signup-page {
  font-family: 'SUIT', sans-serif;
}
</style>
