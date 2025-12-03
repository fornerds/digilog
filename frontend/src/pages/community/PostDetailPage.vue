<template>
  <div class="post-detail-page">
    <h1>게시글 상세</h1>
    <!-- 게시글 상세 컨텐츠 -->
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()
const postId = route.params.postId as string
</script>

<style scoped>
.post-detail-page {
  /* 스타일 */
}
</style>


