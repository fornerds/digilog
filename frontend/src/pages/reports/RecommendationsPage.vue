<template>
  <div class="recommendations-page">
    <h1>추천 제품 리스트</h1>
    <!-- 추천 제품 목록 -->
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const reportId = route.params.reportId as string
</script>

<style scoped>
.recommendations-page {
  /* 스타일 */
}
</style>


