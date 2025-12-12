<template>
  <Transition name="fade">
    <div v-if="isOpen" class="terms-modal" @click.self="handleClose">
      <div class="terms-modal__container">
        <div class="terms-modal__header">
          <div class="terms-modal__title-wrapper">
            <h2 class="terms-modal__title">{{ title }}</h2>
          </div>
          <button
            class="terms-modal__close-button"
            @click="handleClose"
            aria-label="닫기"
          >
            <Icon name="close" :size="20" color="#4B5563" />
          </button>
        </div>
        
        <div class="terms-modal__content">
          <div class="terms-modal__scroll-area">
            <div class="terms-modal__text-content">
              <div class="terms-modal__text-title">{{ contentTitle }}</div>
              <div class="terms-modal__divider"></div>
              <div class="terms-modal__text-body" v-html="content"></div>
            </div>
          </div>
        </div>
        
        <div class="terms-modal__footer">
          <button
            class="terms-modal__agree-button"
            @click="handleAgree"
          >
            동의하기
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  isOpen: boolean
  title: string
  contentTitle: string
  content: string
}

const props = defineProps<Props>()

const emit = defineEmits<{
  'close': []
  'agree': []
}>()

const handleClose = () => {
  emit('close')
}

const handleAgree = () => {
  emit('agree')
  emit('close')
}
</script>

<style scoped>
.terms-modal {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.terms-modal__container {
  background-color: white;
  border-radius: 16px;
  width: calc(100% - 32px);
  max-width: 343px;
  height: 589px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.terms-modal__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  flex-shrink: 0;
}

.terms-modal__title-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
}

.terms-modal__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  text-align: center;
  max-width: 220px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.terms-modal__close-button {
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
}

.terms-modal__content {
  flex: 1;
  display: flex;
  gap: 6px;
  align-items: flex-start;
  justify-content: center;
  padding: 12px 16px;
  min-height: 0;
  overflow: hidden;
}

.terms-modal__scroll-area {
  flex: 1;
  overflow-x: hidden;
  overflow-y: auto;
  width: 300px;
  max-height: 100%;
  position: relative;
}

.terms-modal__scroll-area::-webkit-scrollbar {
  width: 4px;
}

.terms-modal__scroll-area::-webkit-scrollbar-track {
  background: transparent;
}

.terms-modal__scroll-area::-webkit-scrollbar-thumb {
  background-color: var(--graysacle-line-weak);
  border-radius: 999px;
}

.terms-modal__text-content {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-start;
  width: 100%;
}

.terms-modal__text-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-black);
  padding: 6px 2px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.terms-modal__divider {
  height: 0;
  border-top: 0.5px solid var(--graysacle-line);
  width: 100%;
}

.terms-modal__text-body {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-text);
  padding: 10px 2px;
  white-space: pre-wrap;
  word-break: break-word;
}

.terms-modal__text-body p {
  margin: 0 0 0.5em 0;
}

.terms-modal__text-body p:last-child {
  margin-bottom: 0;
}

.terms-modal__footer {
  padding: 20px;
  flex-shrink: 0;
}

.terms-modal__agree-button {
  width: 100%;
  height: 48px;
  background-color: var(--beige3);
  border: none;
  border-radius: 6px;
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-box3);
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.terms-modal__agree-button:hover {
  background-color: var(--beige3);
  opacity: 0.9;
}

.terms-modal__agree-button:active {
  opacity: 0.8;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-active .terms-modal__container,
.fade-leave-active .terms-modal__container {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.fade-enter-from .terms-modal__container,
.fade-leave-to .terms-modal__container {
  transform: scale(0.95);
  opacity: 0;
}
</style>

