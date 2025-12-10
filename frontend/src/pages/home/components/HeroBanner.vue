<template>
  <section class="hero-banner">
    <div class="hero-banner__container">
      <!-- 이미지 슬라이더 -->
      <div 
        class="hero-banner__slider"
        :style="{ 
          transform: `translateX(-${currentIndex * 100}%)`,
          width: `${banners.length * 100}%`
        }"
      >
        <div
          v-for="(banner, index) in banners"
          :key="index"
          class="hero-banner__slide"
          :style="{ width: '100%' }"
        >
          <div class="hero-banner__image">
            <img 
              :src="banner.image" 
              alt=""
              class="hero-banner__img"
              style="image-rendering: -webkit-optimize-contrast; image-rendering: crisp-edges;"
            />
          </div>
        </div>
      </div>

      <!-- 텍스트 (왼쪽 하단, 고정) -->
      <div class="hero-banner__text">
        <div class="hero-banner__title">
          <p class="hero-banner__title-line">Creating</p>
          <p class="hero-banner__title-line">Beautiful Tales</p>
          <p class="hero-banner__title-line">for Your Skin</p>
        </div>
        <div class="hero-banner__subtitle">
          <p>피부과 전문의의 노하우와 AI 기술이 만나</p>
          <p>당신만의 아름다운 이야기를 만들어 갑니다.</p>
        </div>
      </div>

      <!-- 네비게이션 버튼 (오른쪽 하단, 고정) -->
      <div class="hero-banner__controls">
        <button
          @click.stop="prevBanner"
          class="hero-banner__button"
          aria-label="이전 배너"
        >
          <Icon name="arrow-left" :size="24" color="#ffffff" />
        </button>
        <button
          @click.stop="nextBanner"
          class="hero-banner__button"
          aria-label="다음 배너"
        >
          <Icon name="arrow-right" :size="24" color="#ffffff" />
        </button>
      </div>

      <!-- 구분선 -->
      <div class="hero-banner__divider"></div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'
import mainImage from '@/assets/images/main.png'

const banners = ref([
  { id: 1, image: mainImage },
  { id: 2, image: mainImage },
])

const currentIndex = ref(0)
const autoPlayInterval = ref<number | null>(null)

const prevBanner = () => {
  currentIndex.value = currentIndex.value === 0 ? banners.value.length - 1 : currentIndex.value - 1
  resetAutoPlay()
}

const nextBanner = () => {
  currentIndex.value = (currentIndex.value + 1) % banners.value.length
  resetAutoPlay()
}

const startAutoPlay = () => {
  if (banners.value.length > 1) {
    autoPlayInterval.value = window.setInterval(() => {
      nextBanner()
    }, 5000)
  }
}

const resetAutoPlay = () => {
  if (autoPlayInterval.value) {
    clearInterval(autoPlayInterval.value)
  }
  startAutoPlay()
}

onMounted(() => {
  if (banners.value.length > 1) {
    startAutoPlay()
  }
})

onUnmounted(() => {
  if (autoPlayInterval.value) {
    clearInterval(autoPlayInterval.value)
  }
})
</script>

<style scoped>
.hero-banner {
  position: relative;
  width: 100%;
}

.hero-banner__container {
  position: relative;
  width: 100%;
  height: 701px;
  overflow: hidden;
}

.hero-banner__slider {
  position: absolute;
  inset: 0;
  display: flex;
  height: 538px;
  transition: transform 700ms ease-in-out;
}

.hero-banner__slide {
  position: relative;
  flex-shrink: 0;
  height: 100%;
}

.hero-banner__image {
  position: absolute;
  inset: 0;
  border-radius: 0 0 99px 99px;
  overflow: hidden;
}

.hero-banner__img {
  width: 50%;
  height: 100%;
  object-fit: cover;
}

.hero-banner__text {
  position: absolute;
  left: 16px;
  bottom: 96px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  z-index: 10;
}

.hero-banner__title {
  display: flex;
  flex-direction: column;
  font-family: 'Playfair Display', serif;
  font-weight: 400;
  font-size: 36px;
  line-height: 1.24;
  color: #121212;
}

.hero-banner__title-line {
  margin: 0;
}

.hero-banner__subtitle {
  display: flex;
  flex-direction: column;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext1);
}

.hero-banner__subtitle p {
  margin: 0;
}

.hero-banner__controls {
  position: absolute;
  right: 16px;
  bottom: 28px;
  display: flex;
  align-items: center;
  gap: 0;
  z-index: 10;
}

.hero-banner__button {
  background-color: var(--graysacle-black);
  width: 32px;
  height: 32px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  border: none;
  cursor: pointer;
  margin: 4px;
  transition: opacity 0.2s;
}

.hero-banner__button:hover {
  opacity: 0.8;
}

.hero-banner__divider {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 0;
  border-top: 0.5px solid rgba(186, 194, 208, 1);
  z-index: 10;
}
</style>
