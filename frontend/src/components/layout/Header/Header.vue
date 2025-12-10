<template>
  <header :class="['header', { 'header--transparent': isHomePage }]">
    <nav class="header__nav">
      <div class="header__content">
        <!-- 뒤로가기 버튼이 있는 경우 -->
        <div v-if="showBackButton || title" class="header__left">
          <button
            v-if="showBackButton"
            @click="handleBack"
            class="header__back-button"
            aria-label="뒤로가기"
          >
            <Icon name="arrow-left" :size="24" color="#374151" />
          </button>
          <h1 v-if="title" class="header__title">
            {{ title }}
          </h1>
        </div>
        
        <!-- 기본 로고 (뒤로가기 버튼이나 타이틀이 없을 때만 표시) -->
        <RouterLink 
          v-if="!showBackButton && !title" 
          to="/" 
          class="header__logo"
        >
          <img 
            :src="logoUrl" 
            alt="Digilog" 
            class="header__logo-img"
          />
        </RouterLink>
        
        <!-- 언어 변경 버튼 (메인페이지일 때만 표시) -->
        <button
          v-if="isHomePage"
          @click="handleLanguageToggle"
          class="header__language-button"
          aria-label="언어 변경"
        >
          <svg 
            class="header__language-icon-svg"
            width="24" 
            height="24" 
            viewBox="0 0 24 24" 
            fill="none" 
            xmlns="http://www.w3.org/2000/svg"
          >
            <g clip-path="url(#clip0_translate)">
              <path d="M5 5.8H11M9.28571 4V5.8C9.28571 9.7762 7.36657 13 5 13" stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M6 9C6 10.9491 8.5584 12.5527 11.8067 12.6364M12.0667 19L15.5333 10.8182L19 19M18.22 17.1818H12.8467" stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M22.2589 7.76731C21.2684 5.36226 19.4654 3.38106 17.164 2.16908C16.1405 1.63009 15.0473 1.25843 13.9251 1.05972M22.2589 7.76731L18.0206 6.93346M22.2589 7.76731L23.0982 3.50115M1.73716 16.2791C2.73424 18.6778 4.54053 20.6512 6.8419 21.8561C8.08158 22.5051 9.42267 22.909 10.7931 23.058M1.73716 16.2791L5.95609 17.1091M1.73716 16.2791L0.903312 20.5174" stroke="white" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </g>
            <defs>
              <clipPath id="clip0_translate">
                <rect width="24" height="24" fill="white"/>
              </clipPath>
            </defs>
          </svg>
          <span class="header__language-text">{{ languageName }}</span>
        </button>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, useRouter, useRoute } from 'vue-router'
import logoUrl from '@/assets/images/digilog.png'
import Icon from '@/components/common/Icon/Icon.vue'
import { useUIStore } from '@/stores/ui.store'

interface Props {
  showBackButton?: boolean
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  showBackButton: false,
  title: '',
})

const router = useRouter()
const route = useRoute()
const uiStore = useUIStore()

const isHomePage = computed(() => route.name === 'Home')

const languageName = computed(() => uiStore.getLanguageName())

const handleBack = () => {
  router.back()
}

const handleLanguageToggle = () => {
  uiStore.toggleLanguage()
}
</script>

<style scoped>
.header {
  background-color: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.header--transparent {
  background-color: transparent;
  box-shadow: none;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 20;
}

.header__nav {
  width: 100%;
  max-width: 848px;
  margin: 0 auto;
  padding: 10px 16px;
}

.header__content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header__left {
  display: flex;
  align-items: center;
  flex: 1;
  gap: 8px;
}

.header__back-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 999px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: background-color 0.2s;
}

.header__back-button:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.header__title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  margin: 0;
}

.header__logo {
  display: flex;
  align-items: center;
  transition: opacity 0.2s;
}

.header__logo:hover {
  opacity: 0.8;
}

.header__logo-img {
  height: 36px;
  width: auto;
  object-fit: contain;
}

.header__language-button {
  background-color: var(--graysacle-black);
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-radius: 999px;
  border: none;
  cursor: pointer;
  transition: opacity 0.2s;
}

.header__language-button:hover {
  opacity: 0.9;
}

.header__language-icon-svg {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  display: block;
}

.header__language-text {
  font-family: 'Pretendard Variable', sans-serif;
  font-weight: 700;
  font-size: 14px;
  line-height: 20px;
  color: white;
  white-space: nowrap;
}
</style>
