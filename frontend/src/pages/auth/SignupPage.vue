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
    
    <!-- 이용약관 모달 -->
    <TermsModal
      :is-open="isTermsModalOpen"
      title="이용약관"
      content-title="이용약관입니다."
      :content="termsContent"
      @close="isTermsModalOpen = false"
      @agree="handleTermsAgree"
    />
    
    <!-- 개인정보처리방침 모달 -->
    <TermsModal
      :is-open="isPrivacyModalOpen"
      title="개인정보처리방침"
      content-title="개인정보처리방침입니다."
      :content="privacyContent"
      @close="isPrivacyModalOpen = false"
      @agree="handlePrivacyAgree"
    />
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
import TermsModal from './components/TermsModal.vue'
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
  gender: 'female',
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

const isTermsModalOpen = ref(false)
const isPrivacyModalOpen = ref(false)

const termsContent = `제1조 (목적 및 정의)
본 약관은 '디지로그'(이하 '회사')가 제공하는 디지털과 아날로그의 경계에 서 있는 모든 서비스(이하 '서비스')의 이용 조건 및 절차에 관한 사항을 정함을 목적으로 합니다.

'디지로그'란, 디지털 기술의 편리함과 아날로그 감성의 불편함(?)을 오묘하게 결합한 미지의 영역을 뜻합니다.

제2조 (약관의 효력 및 레트로 변경)
이용자가 서비스를 이용하는 순간, 이 약관은 '즉석 사진처럼 빠르게' 효력이 발생합니다.

회사는 약관을 변경할 수 있으며, 변경된 약관은 '느린 우편' 방식으로 공지될 수 있습니다. (하지만 아마도 공지 안 할 겁니다. 찾아서 읽으세요.)

제3조 (서비스의 제공 및 고장) 💾
서비스는 24시간 제공을 목표로 하지만, 가끔 'CD-ROM 드라이브 고장 난 것처럼' 멈추거나, '카세트테이프 늘어지는 것처럼' 속도가 느려질 수 있습니다.

서비스의 내용은 디지털(최신 기술)과 아날로그(종이 냄새, 폰트의 향수 등)를 무작위로 혼합하여 제공합니다.

제4조 (이용자의 권리 및 불편) ✏️
이용자는 서비스를 자유롭게 이용할 수 있으나, 가끔 '모뎀 연결음 같은' 지연과 불편함을 감수해야 합니다. 이것이 디지로그의 감성입니다.

이용자는 본인의 게시물에 대해 '손글씨로 쓴 것과 같은' 무한한 애착을 가질 의무가 있습니다.`

const privacyContent = `제1조 (개인정보의 처리 목적)
회사는 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경되는 경우에는 개인정보 보호법 제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.

1. 회원 가입 및 관리
회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리, 서비스 부정이용 방지, 각종 고지·통지, 고충처리 목적

2. 재화 또는 서비스 제공
서비스 제공, 콘텐츠 제공, 맞춤 서비스 제공, 본인인증, 요금결제·정산

3. 마케팅 및 광고에의 활용
신규 서비스(제품) 개발 및 맞춤 서비스 제공, 이벤트 및 광고성 정보 제공 및 참여기회 제공, 인구통계학적 특성에 따른 서비스 제공 및 광고 게재, 서비스의 유효성 확인, 접속빈도 파악 또는 회원의 서비스 이용에 대한 통계

제2조 (개인정보의 처리 및 보유기간)
회사는 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집 시에 동의받은 개인정보 보유·이용기간 내에서 개인정보를 처리·보유합니다.

각각의 개인정보 처리 및 보유 기간은 다음과 같습니다.

1. 회원 가입 및 관리: 회원 탈퇴 시까지 (다만, 관계 법령 위반에 따른 수사·조사 등이 진행중인 경우에는 해당 수사·조사 종료 시까지)

2. 재화 또는 서비스 제공: 재화·서비스 공급완료 및 요금결제·정산 완료 시까지

3. 마케팅 및 광고에의 활용: 회원 탈퇴 시까지 또는 동의 철회 시까지

제3조 (개인정보의 제3자 제공)
회사는 정보주체의 개인정보를 제1조(개인정보의 처리 목적)에서 명시한 범위 내에서만 처리하며, 정보주체의 동의, 법률의 특별한 규정 등 개인정보 보호법 제17조 및 제18조에 해당하는 경우에만 개인정보를 제3자에게 제공합니다.

제4조 (정보주체의 권리·의무 및 행사방법)
정보주체는 회사에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할 수 있습니다.

1. 개인정보 처리정지 요구
2. 개인정보 열람 요구
3. 개인정보 정정·삭제 요구
4. 개인정보 처리정지 요구`

const handleTermsClick = () => {
  isTermsModalOpen.value = true
}

const handlePrivacyClick = () => {
  isPrivacyModalOpen.value = true
}

const handleTermsAgree = () => {
  formData.value.termsAgreed = true
}

const handlePrivacyAgree = () => {
  formData.value.privacyAgreed = true
}
</script>

<style scoped>
.signup-page {
  font-family: 'SUIT', sans-serif;
}
</style>
