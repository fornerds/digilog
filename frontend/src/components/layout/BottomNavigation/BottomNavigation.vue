<template>
  <nav
    class="fixed bottom-0 left-0 right-0 z-50 bg-white border-t border-gray-200 shadow-lg"
    aria-label="하단 네비게이션"
  >
    <div class="container mx-auto px-4">
      <div class="flex justify-around items-center h-16">
        <button
          v-for="item in navItems"
          :key="item.name"
          @click="navigateTo(item.route)"
          :class="[
            'flex flex-col items-center justify-center flex-1 h-full transition-colors',
            'hover:bg-gray-50 active:bg-gray-100'
          ]"
          :style="{
            color: isActive(item.route) ? '#B59A79' : '#E5E7EB'
          }"
          :aria-label="item.label"
          :aria-current="isActive(item.route) ? 'page' : undefined"
        >
          <!-- 아이콘 -->
          <div 
            class="relative mb-1"
            :style="{
              transform: (item.icon === 'community' || item.icon === 'user') ? 'scale(0.9)' : 'none'
            }"
          >
            <Icon
              :name="item.icon"
              :size="24"
            />
          </div>
          <!-- 라벨 -->
          <span class="text-xs font-medium">
            {{ item.label }}
          </span>
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Icon from '@/components/common/Icon/Icon.vue'

interface NavItem {
  name: string
  label: string
  route: string
  icon: string
}

const route = useRoute()
const router = useRouter()

// 네비게이션 메뉴 아이템 (Figma 디자인에 맞게 수정 필요)
const navItems: NavItem[] = [
  {
    name: 'home',
    label: '홈',
    route: '/',
    icon: 'home',
  },
  {
    name: 'reports',
    label: '보고서',
    route: '/reports',
    icon: 'report',
  },
  {
    name: 'community',
    label: '커뮤니티',
    route: '/community',
    icon: 'community',
  },
  {
    name: 'mypage',
    label: '마이페이지',
    route: '/mypage',
    icon: 'user',
  },
]

// 현재 활성화된 라우트 확인
const isActive = (routePath: string): boolean => {
  const currentPath = route.path
  
  // 홈 경로는 정확히 일치해야 함
  if (routePath === '/') {
    return currentPath === '/' || currentPath === ''
  }
  
  // 다른 경로들은 해당 경로로 시작하는지 확인
  // 예: /reports, /reports/123, /reports/123/personal-color 모두 보고서 탭 활성화
  // 예: /community, /community/123, /community/notice/123 모두 커뮤니티 탭 활성화
  // 예: /mypage, /mypage/profile 모두 마이페이지 탭 활성화
  return currentPath.startsWith(routePath)
}

// 네비게이션 함수
const navigateTo = (routePath: string) => {
  // 현재 경로와 동일하면 이동하지 않음
  if (route.path === routePath) {
    return
  }
  
  // 라우터로 이동 (에러 처리 포함)
  router.push(routePath).catch((err) => {
    // 같은 경로로 이동하려는 경우 무시
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}
</script>

<style scoped>
/* 바텀 네비게이션을 위한 패딩 추가 (컨텐츠가 가려지지 않도록) */
</style>

