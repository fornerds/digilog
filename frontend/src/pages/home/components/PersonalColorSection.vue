<template>
  <section 
    class="personal-color-section"
    @click="handleClick"
  >
    <div class="personal-color-section__image">
      <img 
        :src="imageUrl" 
        alt="퍼스널 컬러"
        class="personal-color-section__img"
      />
    </div>
    <div class="personal-color-section__content">
      <p class="personal-color-section__text-line1">
        더 자연스럽고, 더 생기있게
      </p>
      <div class="personal-color-section__text-line2">
        <p class="personal-color-section__text-bold">나의 퍼스널컬러 확인하기</p>
        <Icon name="chevron-right" :size="12" color="#374151" />
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import Icon from '@/components/common/Icon/Icon.vue'
import mainImage from '@/assets/images/main.png'

const router = useRouter()
const imageUrl = mainImage

const handleClick = () => {
  router.push('/onboarding')
}
</script>

<style scoped>
.personal-color-section {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line);
  border-radius: 6px;
  height: 88px;
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 2px;
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.personal-color-section__image {
  position: absolute;
  left: -1px;
  top: -1px;
  width: 129px;
  height: 88px;
  overflow: hidden;
}

.personal-color-section__img {
  position: absolute;
  left: -18px;
  top: -53px;
  width: 167px;
  height: 167px;
  object-fit: cover;
  object-position: 50% 50%;
}

.personal-color-section__content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  justify-content: flex-end;
  flex: 1;
}

.personal-color-section__text-line1 {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-text);
  margin: 0;
}

.personal-color-section__text-line2 {
  display: flex;
  align-items: center;
  gap: 4px;
}

.personal-color-section__text-bold {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
}
</style>
