<template>
  <header class="header">
    <nav class="w-full mx-auto py-[12px]" style="max-width: 848px">
      <div class="flex items-center justify-between">
        <!-- 뒤로가기 버튼이 있는 경우 -->
        <div v-if="showBackButton || title" class="flex items-center flex-1" style="gap: 8px">
          <button
            v-if="showBackButton"
            @click="handleBack"
            class="flex items-center justify-center w-10 h-10 rounded-full hover:bg-gray-100 transition-colors"
            aria-label="뒤로가기"
          >
            <Icon name="arrow-left" :size="24" color="#374151" />
          </button>
          <h1 v-if="title" class="text-lg font-semibold text-gray-900">
            {{ title }}
          </h1>
        </div>
        
        <!-- 기본 로고 (뒤로가기 버튼이나 타이틀이 없을 때만 표시) -->
        <RouterLink 
          v-if="!showBackButton && !title" 
          to="/" 
          class="flex items-center hover:opacity-80 transition-opacity"
        >
          <img 
            :src="logoUrl" 
            alt="Digilog" 
            class="h-8 w-auto object-contain"
          />
        </RouterLink>
        
        <div class="flex items-center gap-4">
          <!-- 네비게이션 메뉴 -->
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { RouterLink, useRouter } from 'vue-router'
import logoUrl from '@/assets/images/digilog.png'
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  showBackButton?: boolean
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  showBackButton: false,
  title: '',
})

const router = useRouter()

const handleBack = () => {
  router.back()
}
</script>

<style scoped>
.header {
  @apply bg-white shadow-sm;
}
</style>


