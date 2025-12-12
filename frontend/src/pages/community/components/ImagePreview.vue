<template>
  <div class="image-preview">
    <div class="image-preview__image-wrapper">
      <img
        :src="imageUrl"
        :alt="`Uploaded image ${index + 1}`"
        class="image-preview__image"
      />
      <button
        class="image-preview__delete-button"
        @click="handleDelete"
        aria-label="이미지 삭제"
      >
        <Icon name="close-white" :size="16" color="white" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  imageUrl: string
  index: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  delete: [index: number]
}>()

const handleDelete = () => {
  emit('delete', props.index)
}
</script>

<style scoped>
.image-preview {
  position: relative;
  width: 60px;
  height: 60px;
  flex-shrink: 0;
}

.image-preview__image-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 4px;
  overflow: hidden;
}

.image-preview__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
}

.image-preview__delete-button {
  position: absolute;
  top: 4px;
  right: 4px;
  background-color: rgba(75, 85, 99, 0.5);
  width: 20px;
  height: 20px;
  border-radius: 10px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.818px;
  cursor: pointer;
  transition: opacity 0.2s;
}

.image-preview__delete-button:hover {
  opacity: 0.8;
}
</style>

