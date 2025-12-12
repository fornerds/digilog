<template>
  <header :class="['header', { 'header--transparent': isHomePage }]">
    <nav class="header__nav">
      <div class="header__content">
        <!-- 뒤로가기 버튼이 있는 경우 -->
        <div v-if="showBackButton" class="header__left">
          <button
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
        
        <!-- 타이틀만 있는 경우 (뒤로가기 버튼 없음) -->
        <div v-if="!showBackButton && title" class="header__left">
          <h1 class="header__title">
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
        
        <!-- 좋아요 버튼 (showLikeButton이 true일 때 표시) -->
        <div v-if="showLikeButton" class="header__like-section">
          <button
            @click="handleLikeToggle"
            class="header__like-button"
            aria-label="좋아요"
          >
            <svg 
              class="header__like-icon"
              width="23" 
              height="21" 
              viewBox="0 0 23 21" 
              fill="none" 
              xmlns="http://www.w3.org/2000/svg"
            >
              <path d="M20.3807 2.65027C19.8676 2.12709 19.2583 1.71206 18.5878 1.4289C17.9172 1.14574 17.1985 1 16.4727 1C15.7468 1 15.0281 1.14574 14.3576 1.4289C13.687 1.71206 13.0778 2.12709 12.5646 2.65027L11.4997 3.73555L10.4348 2.65027C9.39834 1.59397 7.99258 1.00055 6.52679 1.00055C5.06099 1.00055 3.65523 1.59397 2.61876 2.65027C1.58229 3.70657 1 5.13922 1 6.63305C1 8.12688 1.58229 9.55953 2.61876 10.6158L11.4997 19.6667L20.3807 10.6158C20.8941 10.0929 21.3013 9.472 21.5791 8.78862C21.857 8.10524 22 7.37277 22 6.63305C22 5.89333 21.857 5.16086 21.5791 4.47748C21.3013 3.7941 20.8941 3.17321 20.3807 2.65027Z" fill="#374151" stroke="#374151" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span v-if="likedCount > 0" class="header__like-count">{{ likedCount }}</span>
          </button>
        </div>
        
        <!-- 업로드 버튼 (showUploadButton이 true일 때 표시) -->
        <button
          v-if="showUploadButton"
          @click="handleUploadClick"
          class="header__upload-button"
          aria-label="업로드"
        >
          <span class="header__upload-text">업로드</span>
        </button>
        
        <!-- 작성하기 버튼 (showWriteButton이 true일 때 표시) -->
        <button
          v-if="showWriteButton && !showUploadButton"
          @click="handleWriteClick"
          class="header__write-button"
          aria-label="작성하기"
        >
          <Icon name="edit" :size="24" color="#B59A79" />
        </button>
        
        <!-- 저장 버튼 (showSaveButton이 true일 때 표시) -->
        <button
          v-if="showSaveButton"
          @click="handleSaveClick"
          class="header__save-button"
          aria-label="저장"
        >
          <span class="header__save-text">저장</span>
        </button>
        
        <!-- 로그아웃 버튼 (showLogoutButton이 true일 때 표시) -->
        <button
          v-if="showLogoutButton && !showSaveButton"
          @click="handleLogoutClick"
          class="header__logout-button"
          aria-label="로그아웃"
        >
          <span class="header__logout-text">로그아웃</span>
        </button>
        
        <!-- 언어 변경 버튼 (메인페이지일 때만 표시) -->
        <button
          v-if="isHomePage && !showLikeButton && !showWriteButton && !showUploadButton && !showLogoutButton && !showSaveButton"
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
  showLikeButton?: boolean
  likedCount?: number
  showWriteButton?: boolean
  showUploadButton?: boolean
  showLogoutButton?: boolean
  showSaveButton?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showBackButton: false,
  title: '',
  showLikeButton: false,
  likedCount: 0,
  showWriteButton: false,
  showUploadButton: false,
  showLogoutButton: false,
  showSaveButton: false,
})

const emit = defineEmits<{
  'write-click': []
  'upload-click': []
  'logout-click': []
  'save-click': []
}>()

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

const handleLikeToggle = () => {
  // 좋아요 토글 로직은 부모 컴포넌트에서 처리
  // emit('like-toggle')
}

const handleWriteClick = () => {
  emit('write-click')
}

const handleUploadClick = () => {
  emit('upload-click')
}

const handleLogoutClick = () => {
  emit('logout-click')
}

const handleSaveClick = () => {
  emit('save-click')
}
</script>

<style scoped>
.header {
  background-color: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header--transparent {
  background: transparent !important;
  background-color: transparent !important;
  box-shadow: none !important;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
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

.header__like-section {
  display: flex;
  align-items: center;
  position: relative;
}

.header__like-button {
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  position: relative;
}

.header__like-icon {
  width: 23px;
  height: 21px;
  display: block;
}

.header__like-count {
  position: absolute;
  bottom: -8px;
  right: -4px;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 10px;
  line-height: 1.2;
  color: var(--graysacle-text);
  background-color: white;
  padding: 1px 3px;
  border-radius: 4px;
  min-width: 14px;
  text-align: center;
}

.header__write-button {
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

.header__write-button:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.header__upload-button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  padding: 0 2px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: opacity 0.2s;
}

.header__upload-button:hover {
  opacity: 0.8;
}

.header__upload-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--beige3);
  white-space: nowrap;
}

.header__logout-button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  padding: 0 2px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: opacity 0.2s;
}

.header__logout-button:hover {
  opacity: 0.8;
}

.header__logout-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--beige3);
  white-space: nowrap;
}

.header__save-button {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  padding: 0 2px;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: opacity 0.2s;
}

.header__save-button:hover {
  opacity: 0.8;
}

.header__save-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--beige3);
  white-space: nowrap;
}
</style>
