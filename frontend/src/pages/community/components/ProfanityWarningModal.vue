<template>
  <Transition name="fade">
    <div v-if="isOpen" class="profanity-warning-modal__overlay" @click.self="handleClose">
      <div class="profanity-warning-modal__container">
        <!-- 헤더 -->
        <div class="profanity-warning-modal__header">
          <h2 class="profanity-warning-modal__title">팝업메시지</h2>
          <button
            class="profanity-warning-modal__close-button"
            @click="handleClose"
            aria-label="닫기"
          >
            <Icon name="close" :size="24" color="#4B5563" />
          </button>
        </div>
        
        <!-- 본문 -->
        <div class="profanity-warning-modal__body">
          <div class="profanity-warning-modal__content">
            <!-- 경고 아이콘 -->
            <div class="profanity-warning-modal__icon-wrapper">
              <Icon name="alert" :size="45" color="#374151" />
            </div>
            
            <!-- 텍스트 -->
            <div class="profanity-warning-modal__text-wrapper">
              <h3 class="profanity-warning-modal__heading">비속어가 감지되었습니다.</h3>
              <div class="profanity-warning-modal__description">
                <p>작성하신 내용에 부적절한 표현이 포함되어</p>
                <p>있을 수 있습니다. 그래도 게시하시겠습니까?</p>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 버튼 -->
        <div class="profanity-warning-modal__footer">
          <button
            class="profanity-warning-modal__submit-button"
            @click="handleSubmit"
          >
            게시하기
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup lang="ts">
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  isOpen: boolean
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
  submit: []
}>()

const handleClose = () => {
  emit('close')
}

const handleSubmit = () => {
  emit('submit')
}
</script>

<style scoped>
.profanity-warning-modal__overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.profanity-warning-modal__container {
  background-color: white;
  border-radius: 16px;
  width: 315px;
  max-width: calc(100% - 32px);
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  position: relative;
}

.profanity-warning-modal__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  width: 100%;
}

.profanity-warning-modal__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-text);
  margin: 0;
  flex: 1;
}

.profanity-warning-modal__close-button {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: absolute;
  right: 20px;
  top: 20px;
}

.profanity-warning-modal__body {
  padding: 12px 20px;
  width: 100%;
}

.profanity-warning-modal__content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  justify-content: center;
  padding: 20px 6px;
  min-height: 0;
  overflow-x: hidden;
  overflow-y: auto;
}

.profanity-warning-modal__icon-wrapper {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.profanity-warning-modal__text-wrapper {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
  text-align: center;
  width: 263px;
}

.profanity-warning-modal__heading {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  width: 100%;
  white-space: pre-wrap;
}

.profanity-warning-modal__description {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext1);
  width: 100%;
}

.profanity-warning-modal__description p {
  margin: 0;
  white-space: pre-wrap;
}

.profanity-warning-modal__footer {
  padding: 20px;
  width: 100%;
}

.profanity-warning-modal__submit-button {
  background-color: var(--beige3);
  border: none;
  border-radius: 6px;
  height: 48px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: opacity 0.2s;
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-box3);
}

.profanity-warning-modal__submit-button:hover {
  opacity: 0.9;
}

/* Transition styles */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-active .profanity-warning-modal__container,
.fade-leave-active .profanity-warning-modal__container {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.fade-enter-from .profanity-warning-modal__container,
.fade-leave-to .profanity-warning-modal__container {
  transform: scale(0.95);
  opacity: 0;
}
</style>

