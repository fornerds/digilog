<template>
  <Teleport to="body">
    <div
      v-if="isOpen"
      class="event-modal"
      @click.self="handleClose"
    >
      <div class="event-modal__content">
        <div class="event-modal__header">
          <div class="event-modal__title-group">
            <div class="event-modal__title">{{ notice.label }}</div>
            <div class="event-modal__time">{{ notice.time }}</div>
          </div>
          <button
            class="event-modal__close-button"
            @click="handleClose"
            aria-label="닫기"
          >
            <Icon name="close" :size="20" color="#4B5563" />
          </button>
        </div>
        
        <div class="event-modal__body">
          <div class="event-modal__scroll-container">
            <div class="event-modal__main-title">{{ notice.title }}</div>
            <div class="event-modal__divider"></div>
            <div class="event-modal__text-content">
              <p v-for="(line, index) in notice.contentLines" :key="index" class="event-modal__text-line">
                {{ line }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Notice {
  label: string
  text: string
  time: string
  title?: string
  content?: string
  contentLines?: string[]
}

interface Props {
  isOpen: boolean
  notice: Notice
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
}>()

const handleClose = () => {
  emit('close')
}
</script>

<style scoped>
.event-modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 0;
}

.event-modal__content {
  background-color: white;
  border-radius: 16px;
  width: calc(100% - 32px);
  max-width: 343px;
  max-height: calc(100vh - 80px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.event-modal__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  flex-shrink: 0;
}

.event-modal__title-group {
  display: flex;
  gap: 12px;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.event-modal__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  white-space: nowrap;
  max-width: 220px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.event-modal__time {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-subtext3);
  white-space: nowrap;
  flex-shrink: 0;
}

.event-modal__close-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  flex-shrink: 0;
  transition: opacity 0.2s;
}

.event-modal__close-button:hover {
  opacity: 0.7;
}

.event-modal__body {
  display: flex;
  gap: 6px;
  padding: 12px 16px;
  flex: 1;
  min-height: 0;
}

.event-modal__scroll-container {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 2px;
}

.event-modal__scroll-container::-webkit-scrollbar {
  width: 4px;
}

.event-modal__scroll-container::-webkit-scrollbar-track {
  background: transparent;
}

.event-modal__scroll-container::-webkit-scrollbar-thumb {
  background-color: var(--graysacle-line-weak);
  border-radius: 999px;
}

.event-modal__main-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-black);
  padding: 6px 2px;
  margin-bottom: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.event-modal__divider {
  height: 0.5px;
  background-color: var(--graysacle-line-highlight);
  width: 100%;
  margin: 0;
}

.event-modal__text-content {
  padding: 10px 2px;
}

.event-modal__text-line {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-text);
  margin: 0 0 0 0;
  white-space: pre-wrap;
  word-break: break-word;
}

.event-modal__text-line:last-child {
  margin-bottom: 0;
}
</style>

