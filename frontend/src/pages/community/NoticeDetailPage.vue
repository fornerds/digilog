<template>
  <div class="notice-detail-page">
    <h1>공지/이벤트 상세</h1>
    <!-- 공지/이벤트 상세 컨텐츠 -->
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const noticeId = route.params.id as string
</script>

<style scoped>
.notice-detail-page {
  /* 스타일 */
}
</style>


