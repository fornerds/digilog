<template>
  <div class="layout" :class="{ 'layout--home': isHomePage }">
    <Header 
      :show-back-button="showBackButton" 
      :title="headerTitle"
      :show-like-button="showLikeButton"
      :liked-count="likedCount"
      :show-write-button="showWriteButton"
      :show-upload-button="showUploadButton"
      :show-logout-button="showLogoutButton"
      :show-save-button="showSaveButton"
      @write-click="handleWriteClick"
      @upload-click="handleUploadClick"
      @logout-click="handleLogoutClick"
      @save-click="handleSaveClick"
    />
    <main class="layout__main" :class="{ 'layout__main--home': isHomePage }">
      <RouterView />
    </main>
    <BottomNavigation />
  </div>
</template>

<script setup lang="ts">
import { computed, ref, provide } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { RouterView } from 'vue-router'
import Header from '@/components/layout/Header/Header.vue'
import BottomNavigation from '@/components/layout/BottomNavigation/BottomNavigation.vue'

const router = useRouter()

const route = useRoute()
const isHomePage = computed(() => route.name === 'Home')
const isReportDetailPage = computed(() => route.name === 'ReportDetail')
const isReportsPage = computed(() => route.name === 'Reports')
const isPersonalColorPage = computed(() => route.name === 'PersonalColor')
const isRecommendationsPage = computed(() => route.name === 'Recommendations')
const isCommunityPage = computed(() => route.name === 'Community')
const isPostWritePage = computed(() => route.name === 'PostWrite')
const isPostEditPage = computed(() => route.name === 'PostEdit')
const isMyPage = computed(() => route.name === 'MyPage')
const isProfileEditPage = computed(() => route.name === 'ProfileEdit')
const isLikedProductsPage = computed(() => route.name === 'LikedProducts')
const showBackButton = computed(() => isReportDetailPage.value || isReportsPage.value || isPersonalColorPage.value || isRecommendationsPage.value || isCommunityPage.value || isPostWritePage.value || isPostEditPage.value || isProfileEditPage.value || isLikedProductsPage.value)
const showLikeButton = computed(() => isRecommendationsPage.value)
const showWriteButton = computed(() => isCommunityPage.value)
const showUploadButton = computed(() => isPostWritePage.value || isPostEditPage.value)
const showLogoutButton = computed(() => isMyPage.value)
const showSaveButton = computed(() => isProfileEditPage.value)
const headerTitle = computed(() => {
  if (isCommunityPage.value) return '커뮤니티'
  if (isPostWritePage.value) return '새 게시글'
  if (isPostEditPage.value) return '게시글 수정'
  if (isMyPage.value) return '마이페이지'
  if (isProfileEditPage.value) return '개인정보 수정'
  if (isLikedProductsPage.value) return '찜한 제품'
  return ''
})

const likedCount = ref(0)

provide('likedCount', likedCount)

const handleWriteClick = () => {
  router.push('/community/write')
}

const handleUploadClick = () => {
  // PostWritePage에서 처리하도록 커스텀 이벤트 발생
  window.dispatchEvent(new CustomEvent('header-upload-click'))
}

const handleLogoutClick = () => {
  // TODO: 로그아웃 로직 구현 (세션/토큰 삭제 등)
  console.log('Logout clicked')
  router.push('/auth/login')
}

const handleSaveClick = () => {
  // ProfileEditPage에서 처리하도록 커스텀 이벤트 발생
  window.dispatchEvent(new CustomEvent('header-save-click'))
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #f9fafb;
  padding-bottom: 80px;
  position: relative;
}

.layout--home {
  background-color: var(--graysacle-box3);
  padding-bottom: 80px;
  padding-top: 0;
}

.layout__main {
  width: 100%;
  max-width: 848px;
  margin: 0 auto;
}

.layout--home .layout__main {
  margin-top: 0;
  padding-top: 0;
  position: relative;
}

.layout__main--home {
  margin-top: 0 !important;
  padding-top: 0 !important;
}

.layout--home {
  position: relative;
}
</style>

