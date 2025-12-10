<template>
  <div 
    class="community-post-card"
    @click="$emit('click')"
  >
    <p class="community-post-card__text">{{ post.content }}</p>
  </div>
</template>

<script setup lang="ts">
interface Post {
  id: number
  content: string
  createdAt: string
}

interface Props {
  post: Post
}

defineProps<Props>()
defineEmits<{
  click: []
}>()
</script>

<style scoped>
.community-post-card {
  border-bottom: 1px solid var(--graysacle-line);
  padding: 4px 0;
  width: 100%;
  cursor: pointer;
}

.community-post-card__text {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-subtext2);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
