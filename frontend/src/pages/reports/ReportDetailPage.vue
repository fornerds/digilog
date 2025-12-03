<template>
  <div class="report-detail-page">
    <h1>보고서 상세</h1>
    <!-- 보고서 상세 컨텐츠 -->
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const reportId = route.params.reportId as string
</script>

<style scoped>
.report-detail-page {
  /* 스타일 */
}
</style>


