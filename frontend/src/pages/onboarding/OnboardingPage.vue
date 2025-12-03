<template>
  <div class="onboarding-page min-h-screen flex flex-col relative">
    <!-- 상단 헤더 -->
    <div class="absolute top-0 left-0 right-0 z-20 flex justify-end items-center px-6 pt-6">
      <!-- 로그인 링크 -->
      <RouterLink
        to="/auth/login"
        class="login-link hover:opacity-80 transition-opacity"
      >
        로그인
      </RouterLink>
    </div>

    <!-- 메인 컨텐츠 -->
    <div class="flex-1 flex items-center justify-center overflow-hidden pt-20 pb-32">
      <div
        class="w-full transition-transform duration-300 ease-in-out"
        :style="{ transform: `translateX(-${currentStep * 100}%)` }"
      >
        <div class="flex w-full" style="width: calc(100% * 3)">
          <!-- 슬라이드 1 -->
          <div class="w-full flex-shrink-0 px-6 flex flex-col items-start justify-center">
            <div class="max-w-md w-full text-left space-y-8">
              <!-- 로고 영역 -->
              <div class="flex justify-start mb-12">
                <img 
                  :src="logoUrl" 
                  alt="Digilog" 
                  class="h-16 w-auto object-contain"
                />
              </div>

              <!-- 페이지 인디케이터 -->
              <div class="flex justify-start gap-2 mb-8">
                <button
                  v-for="(step, index) in totalSteps"
                  :key="index"
                  @click="goToStep(index)"
                  :class="[
                    'rounded-full transition-all duration-300',
                    currentStep === index
                      ? 'bg-[#B59A79] w-8 h-2'
                      : 'bg-gray-300 w-2 h-2'
                  ]"
                  :aria-label="`${index + 1}번째 페이지`"
                />
              </div>

              <!-- 설명 텍스트 -->
              <div class="space-y-4">
                <p class="onboarding-description">
                  단 몇 분만에,<br /> 
                  당신의 피부를 정확히 분석하고<br />
                  개인화된 케어 솔루션을 받아보세요!
                </p>
              </div>
            </div>
          </div>

          <!-- 슬라이드 2 -->
          <div class="w-full flex-shrink-0 px-6 flex flex-col items-start justify-center">
            <div class="max-w-md w-full text-left space-y-8">
              <!-- 로고 영역 -->
              <div class="flex justify-start mb-12">
                <img 
                  :src="logoUrl" 
                  alt="Digilog" 
                  class="h-16 w-auto object-contain"
                />
              </div>

              <!-- 페이지 인디케이터 -->
              <div class="flex justify-start gap-2 mb-8">
                <button
                  v-for="(step, index) in totalSteps"
                  :key="index"
                  @click="goToStep(index)"
                  :class="[
                    'rounded-full transition-all duration-300',
                    currentStep === index
                      ? 'bg-[#B59A79] w-8 h-2'
                      : 'bg-gray-300 w-2 h-2'
                  ]"
                  :aria-label="`${index + 1}번째 페이지`"
                />
              </div>

              <!-- 설명 텍스트 -->
              <div class="space-y-4">
                <p class="onboarding-description">
                  퍼스널 컬러를 분석하여<br />
                  당신에게 어울리는 스타일을<br />
                  추천해드립니다!
                </p>
              </div>
            </div>
          </div>

          <!-- 슬라이드 3 -->
          <div class="w-full flex-shrink-0 px-6 flex flex-col items-start justify-center">
            <div class="max-w-md w-full text-left space-y-8">
              <!-- 로고 영역 -->
              <div class="flex justify-start mb-12">
                <img 
                  :src="logoUrl" 
                  alt="Digilog" 
                  class="h-16 w-auto object-contain"
                />
              </div>

              <!-- 페이지 인디케이터 -->
              <div class="flex justify-start gap-2 mb-8">
                <button
                  v-for="(step, index) in totalSteps"
                  :key="index"
                  @click="goToStep(index)"
                  :class="[
                    'rounded-full transition-all duration-300',
                    currentStep === index
                      ? 'bg-[#B59A79] w-8 h-2'
                      : 'bg-gray-300 w-2 h-2'
                  ]"
                  :aria-label="`${index + 1}번째 페이지`"
                />
              </div>

              <!-- 설명 텍스트 -->
              <div class="space-y-4">
                <p class="onboarding-description">
                  커뮤니티에서 다른 사용자들과<br />
                  스타일을 공유하고<br />
                  영감을 받아보세요!
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 하단 버튼 -->
    <div class="absolute bottom-0 left-0 right-0 pb-12 px-6">
      <button
        @click="goToSignup"
        class="w-full py-3.5 px-4 rounded-lg text-base font-bold text-white transition-all"
        style="background-color: #B59A79"
      >
        디지로그 회원가입
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import logoUrl from '@/assets/images/digilog.png'

const router = useRouter()
const currentStep = ref(0)
const totalSteps = 3

const goToStep = (index: number) => {
  currentStep.value = index
}

const goToSignup = () => {
  router.push('/auth/signup')
}
</script>

<style scoped>
.onboarding-page {
  font-family: 'SUIT', sans-serif;
  /* 그라데이션 배경 */
  background: linear-gradient(180deg, #F9FAFB 38%, #EEE9E4 100%);
  background-attachment: fixed;
}

.login-link {
  color: #8C857C;
  font-family: SUIT, sans-serif;
  font-size: 18px;
  font-style: normal;
  font-weight: 700;
  line-height: 150%; /* 27px */
}

.onboarding-description {
  color: #493926;
  font-family: SUIT, sans-serif;
  font-size: 22px;
  font-style: normal;
  font-weight: 700;
  line-height: 140%; /* 30.8px */
}
</style>
