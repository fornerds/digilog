<template>
  <div class="mypage">
    <h1>마이페이지</h1>
    <!-- 마이페이지 컨텐츠 -->
  </div>
</template>

<script setup lang="ts">
import { useAuth } from '@/composables/useAuth'

const { user } = useAuth()
</script>

<style scoped>
.mypage {
  /* 스타일 */
}
</style>


