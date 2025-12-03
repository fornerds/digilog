<template>
  <div class="personal-color-page">
    <h1>퍼스널컬러</h1>
    <!-- 퍼스널컬러 컨텐츠 -->
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const reportId = route.params.reportId as string
</script>

<style scoped>
.personal-color-page {
  /* 스타일 */
}
</style>


