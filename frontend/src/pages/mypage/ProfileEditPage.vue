<template>
  <div class="profile-edit-page">
    <div class="profile-edit-page__content">
      <!-- 프로필 이미지 섹션 -->
      <div class="profile-edit-page__image-section">
        <div class="profile-edit-page__image-wrapper">
          <img
            v-if="profileImage"
            :src="profileImage"
            :alt="formData.name"
            class="profile-edit-page__image"
          />
          <div v-else class="profile-edit-page__image-placeholder"></div>
          <button
            class="profile-edit-page__camera-button"
            @click="handleImageUpload"
            aria-label="프로필 이미지 업로드"
          >
            <Icon name="camera-gray" :size="19" color="#6B7280" />
          </button>
        </div>
      </div>
      
      <!-- 입력 폼 섹션 -->
      <div class="profile-edit-page__form-section">
        <!-- 이름 -->
        <div class="profile-edit-page__field">
          <label class="profile-edit-page__label">
            이름
            <span class="profile-edit-page__required">*</span>
          </label>
          <input
            v-model="formData.name"
            type="text"
            class="profile-edit-page__input"
            placeholder="이름을 입력해주세요"
          />
        </div>
        
        <!-- 성별 -->
        <div class="profile-edit-page__field">
          <label class="profile-edit-page__label">
            성별
            <span class="profile-edit-page__required">*</span>
          </label>
          <div class="profile-edit-page__gender-buttons">
            <button
              type="button"
              :class="[
                'profile-edit-page__gender-button',
                { 'profile-edit-page__gender-button--active': formData.gender === 'female' }
              ]"
              @click="formData.gender = 'female'"
            >
              여성
            </button>
            <button
              type="button"
              :class="[
                'profile-edit-page__gender-button',
                { 'profile-edit-page__gender-button--active': formData.gender === 'male' }
              ]"
              @click="formData.gender = 'male'"
            >
              남성
            </button>
          </div>
        </div>
        
        <!-- 이메일 -->
        <div class="profile-edit-page__field">
          <label class="profile-edit-page__label">
            이메일
            <span class="profile-edit-page__required">*</span>
          </label>
          <div class="profile-edit-page__email-wrapper">
            <input
              v-model="emailLocal"
              type="text"
              class="profile-edit-page__input profile-edit-page__input--email-local"
              placeholder="이메일 주소"
            />
            <div class="profile-edit-page__email-at">
              <Icon name="at" :size="20" color="#4B5563" />
            </div>
            <div class="profile-edit-page__email-domain-wrapper">
              <select
                v-model="emailDomain"
                class="profile-edit-page__input profile-edit-page__input--select"
                @change="handleDomainChange"
              >
                <option value="">선택</option>
                <option value="gmail.com">gmail.com</option>
                <option value="naver.com">naver.com</option>
                <option value="daum.net">daum.net</option>
                <option value="kakao.com">kakao.com</option>
                <option value="custom">직접 입력</option>
              </select>
              <div class="profile-edit-page__select-icon">
                <Icon name="chevron-down" :size="17" color="#BAC2D0" />
              </div>
              <input
                v-if="emailDomain === 'custom'"
                v-model="emailCustomDomain"
                type="text"
                class="profile-edit-page__input profile-edit-page__input--email-domain"
                placeholder="도메인 입력"
                @input="handleCustomDomainInput"
              />
            </div>
          </div>
        </div>
        
        <!-- 휴대폰 번호 -->
        <div class="profile-edit-page__field">
          <label class="profile-edit-page__label">
            휴대폰 번호
            <span class="profile-edit-page__required">*</span>
          </label>
          <input
            v-model="formData.phone"
            type="tel"
            class="profile-edit-page__input"
            placeholder="010-1234-5678"
            @input="handlePhoneInput"
          />
        </div>
        
        <!-- 생년월일 -->
        <div class="profile-edit-page__field">
          <label class="profile-edit-page__label">
            생년월일
            <span class="profile-edit-page__required">*</span>
          </label>
          <div class="profile-edit-page__date-wrapper">
            <input
              v-model="formData.birthDate"
              type="date"
              class="profile-edit-page__input profile-edit-page__input--date"
              @change="handleDateChange"
            />
            <button
              type="button"
              class="profile-edit-page__calendar-button"
              @click="handleCalendarClick"
              aria-label="날짜 선택"
            >
              <Icon name="calendar" :size="20" color="#4B5563" />
            </button>
          </div>
        </div>
      </div>
      
      <!-- 숨겨진 파일 입력 -->
      <input
        ref="fileInputRef"
        type="file"
        accept="image/*"
        class="profile-edit-page__file-input"
        @change="handleFileChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Icon from '@/components/common/Icon/Icon.vue'
import profile01 from '@/assets/images/profile01.png'

const router = useRouter()

interface FormData {
  name: string
  gender: 'female' | 'male' | ''
  email: string
  phone: string
  birthDate: string
}

const formData = ref<FormData>({
  name: '김지은',
  gender: 'female',
  email: 'duswl335@naver.com',
  phone: '010-1235-1257',
  birthDate: '2025-10-23',
})

const profileImage = ref<string>(profile01)
const fileInputRef = ref<HTMLInputElement | null>(null)

// 이메일 분리
const emailLocal = ref('Jieun99')
const emailDomain = ref('naver.com')
const emailCustomDomain = ref('')

// 이메일 업데이트
watch([emailLocal, emailDomain, emailCustomDomain], () => {
  if (emailLocal.value) {
    if (emailDomain.value === 'custom' && emailCustomDomain.value) {
      formData.value.email = `${emailLocal.value}@${emailCustomDomain.value}`
    } else if (emailDomain.value && emailDomain.value !== 'custom') {
      formData.value.email = `${emailLocal.value}@${emailDomain.value}`
    } else {
      formData.value.email = emailLocal.value
    }
  }
})

// 초기 이메일 파싱
onMounted(() => {
  if (formData.value.email && formData.value.email.includes('@')) {
    const parts = formData.value.email.split('@')
    emailLocal.value = parts[0] || ''
    const domainPart = parts[1] || ''
    
    const defaultDomains = ['gmail.com', 'naver.com', 'daum.net', 'kakao.com']
    if (defaultDomains.includes(domainPart)) {
      emailDomain.value = domainPart
      emailCustomDomain.value = ''
    } else if (domainPart) {
      emailDomain.value = 'custom'
      emailCustomDomain.value = domainPart
    }
  }
})

const handleImageUpload = () => {
  fileInputRef.value?.click()
}

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader()
    reader.onload = (e) => {
      const result = e.target?.result as string
      if (result) {
        profileImage.value = result
      }
    }
    reader.readAsDataURL(file)
  }
  
  if (target) {
    target.value = ''
  }
}

const handleDomainChange = () => {
  if (emailDomain.value !== 'custom') {
    emailCustomDomain.value = ''
  }
}

const handleCustomDomainInput = () => {
  // 이미 watch에서 처리됨
}

const handlePhoneInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  let value = target.value.replace(/\D/g, '')
  
  if (value.length > 11) {
    value = value.slice(0, 11)
  }
  
  if (value.length <= 3) {
    formData.value.phone = value
  } else if (value.length <= 7) {
    formData.value.phone = `${value.slice(0, 3)}-${value.slice(3)}`
  } else {
    formData.value.phone = `${value.slice(0, 3)}-${value.slice(3, 7)}-${value.slice(7)}`
  }
}

const handleDateChange = () => {
  // 날짜 변경 처리
}

const handleCalendarClick = () => {
  const dateInput = document.querySelector('.profile-edit-page__input--date') as HTMLInputElement
  if (dateInput) {
    dateInput.showPicker?.() || dateInput.click()
  }
}

// 헤더의 저장 버튼 클릭 이벤트 리스너
const handleHeaderSave = () => {
  handleSave()
}

const handleSave = () => {
  // TODO: 실제 저장 로직 구현
  console.log('Save profile:', {
    ...formData.value,
    profileImage: profileImage.value,
  })
  
  // 저장 후 마이페이지로 이동
  router.push('/mypage')
}

onMounted(() => {
  // 커스텀 이벤트 리스너 등록
  window.addEventListener('header-save-click', handleHeaderSave)
})

import { onUnmounted } from 'vue'
onUnmounted(() => {
  window.removeEventListener('header-save-click', handleHeaderSave)
})
</script>

<style scoped>
.profile-edit-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 0 100px;
}

.profile-edit-page__content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.profile-edit-page__image-section {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 30px 0;
}

.profile-edit-page__image-wrapper {
  position: relative;
  width: 126px;
  height: 126px;
}

.profile-edit-page__image,
.profile-edit-page__image-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 366.98px;
  object-fit: cover;
  object-position: 50% 50%;
  background-color: var(--graysacle-box3);
}

.profile-edit-page__image-placeholder {
  background-color: var(--graysacle-box2);
}

.profile-edit-page__camera-button {
  position: absolute;
  bottom: 0;
  right: -0.5px;
  width: 36px;
  height: 36px;
  background-color: var(--graysacle-line);
  border-radius: 18px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 3.273px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.profile-edit-page__camera-button:hover {
  background-color: var(--graysacle-subtext3);
}

.profile-edit-page__form-section {
  display: flex;
  flex-direction: column;
  gap: 40px;
  width: 100%;
  padding: 30px 16px;
}

.profile-edit-page__field {
  display: flex;
  flex-direction: column;
  gap: 4px;
  width: 100%;
}

.profile-edit-page__label {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 16px;
  line-height: 1.5;
  color: var(--graysacle-subtext1);
  display: flex;
  gap: 4px;
  align-items: flex-start;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.profile-edit-page__required {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 14px;
  line-height: 1.35;
  color: var(--beige3);
  width: 11px;
}

.profile-edit-page__input {
  border: none;
  border-bottom: 1px solid var(--graysacle-line-highlight, #D1D5DB);
  border-radius: 0;
  padding: 16px 4px;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  background: transparent;
  outline: none;
  width: 100%;
  transition: border-color 0.2s;
}

.profile-edit-page__input:focus {
  border-bottom-color: var(--beige3);
}

.profile-edit-page__input::placeholder {
  color: var(--graysacle-subtext3);
}

.profile-edit-page__gender-buttons {
  display: flex;
  gap: 8px;
  width: 100%;
}

.profile-edit-page__gender-button {
  flex: 1;
  height: 48px;
  border: none;
  border-radius: 6px;
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.profile-edit-page__gender-button:not(.profile-edit-page__gender-button--active) {
  background-color: var(--graysacle-box2);
  color: var(--graysacle-subtext3);
}

.profile-edit-page__gender-button--active {
  background-color: var(--beige3);
  color: var(--beige5);
}

.profile-edit-page__email-wrapper {
  display: flex;
  gap: 12px;
  align-items: center;
  width: 100%;
}

.profile-edit-page__input--email-local {
  flex: 1;
  min-width: 0;
}

.profile-edit-page__email-at {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  padding: 4.168px;
  width: 20px;
  height: 20px;
}

.profile-edit-page__email-domain-wrapper {
  flex: 1;
  min-width: 0;
  position: relative;
}

.profile-edit-page__input--select {
  appearance: none;
  cursor: pointer;
  padding-right: 32px;
}

.profile-edit-page__select-icon {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.667px;
  width: 20px;
  height: 20px;
}

.profile-edit-page__input--email-domain {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
}

.profile-edit-page__date-wrapper {
  position: relative;
  width: 100%;
}

.profile-edit-page__input--date {
  padding-right: 32px;
}

.profile-edit-page__input--date::-webkit-calendar-picker-indicator {
  opacity: 0;
  position: absolute;
  right: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.profile-edit-page__calendar-button {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 1.667px;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  pointer-events: none;
}

.profile-edit-page__file-input {
  display: none;
}
</style>
