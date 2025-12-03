<template>
  <div class="min-h-screen bg-white">
    <Header 
      v-if="route.path !== '/auth/login'"
      :show-back-button="true" 
      :title="pageTitle" 
    />
    <div class="flex items-center justify-center">
      <div class="w-full" style="max-width: 848px">
        <RouterView />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, RouterView } from 'vue-router'
import Header from '@/components/layout/Header/Header.vue'

const route = useRoute()

// 현재 라우트에 따라 페이지 타이틀 설정
const pageTitle = computed(() => {
  if (route.path === '/auth/signup') {
    return '회원가입'
  }
  if (route.path === '/auth/login') {
    return '로그인'
  }
  return ''
})
</script>


