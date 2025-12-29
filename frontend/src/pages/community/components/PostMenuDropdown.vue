<template>
  <Teleport to="body">
    <div v-if="isOpen" class="post-menu-dropdown">
      <div class="post-menu-dropdown__overlay" @click="$emit('close')"></div>
      <div 
        ref="menuRef"
        class="post-menu-dropdown__menu"
        :style="menuStyle"
      >
        <button
          class="post-menu-dropdown__item"
          @click="$emit('edit')"
        >
          게시글 수정
        </button>
        <button
          class="post-menu-dropdown__item post-menu-dropdown__item--danger"
          @click="$emit('delete')"
        >
          게시글 삭제
        </button>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'

interface Props {
  isOpen: boolean
  postId?: number
  triggerElement?: HTMLElement | null
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
  edit: []
  delete: []
}>()

const menuRef = ref<HTMLElement | null>(null)
const menuPosition = ref({ top: 0, right: 16 })

const menuStyle = computed(() => {
  if (!props.triggerElement) {
    return {
      top: `${menuPosition.value.top}px`,
      right: `${menuPosition.value.right}px`,
    }
  }
  
  const rect = props.triggerElement.getBoundingClientRect()
  return {
    top: `${rect.bottom + 4}px`,
    right: `${window.innerWidth - rect.right}px`,
  }
})

watch(() => props.isOpen, (newVal) => {
  if (newVal && props.triggerElement) {
    nextTick(() => {
      // 위치 업데이트는 computed에서 처리
    })
  }
})
</script>

<style scoped>
.post-menu-dropdown {
  position: fixed;
  inset: 0;
  z-index: 1000;
}

.post-menu-dropdown__overlay {
  position: absolute;
  inset: 0;
  background-color: transparent;
}

.post-menu-dropdown__menu {
  position: fixed;
  display: flex;
  padding: 4px;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 4px;
  border-radius: 6px;
  border: 1px solid var(--GraySacle-Line-Weak, #EEEFF1);
  background: var(--GraySacle-Box3, #F9FAFB);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1001;
}

.post-menu-dropdown__item {
  background: transparent;
  border: none;
  border-radius: 6px;
  display: flex;
  padding: 6px 12px;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  overflow: hidden;
  color: var(--GraySacle-SubText2, #6B7280);
  text-align: center;
  text-overflow: ellipsis;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-style: normal;
  font-weight: 600;
  line-height: 135%; /* 18.9px */
  white-space: nowrap;
  transition: background-color 0.2s;
  width: 100%;
}

.post-menu-dropdown__item:hover {
  background-color: rgba(0, 0, 0, 0.05);
}
</style>

