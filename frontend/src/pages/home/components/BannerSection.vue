<template>
  <section class="banner-section">
    <div 
      class="banner-section__slider"
      :style="{ 
        transform: `translateX(-${currentIndex * 100}%)`,
        width: `${banners.length * 100}%`
      }"
    >
      <div
        v-for="(banner, index) in banners"
        :key="index"
        class="banner-section__slide"
        :style="{ width: '100%' }"
        @click="handleBannerClick(banner)"
      >
        <img 
          :src="banner.image" 
          :alt="banner.title"
          class="banner-section__image"
        />
      </div>
    </div>

    <!-- 인디케이터 -->
    <div v-if="banners.length > 1" class="banner-section__indicators">
      <button
        v-for="(banner, index) in banners"
        :key="index"
        @click="currentIndex = index"
        :class="[
          'banner-section__indicator',
          { 'banner-section__indicator--active': currentIndex === index }
        ]"
        :aria-label="`배너 ${index + 1}`"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import bannerImage from '@/assets/images/banner.png'

interface Banner {
  id: number
  image: string
  title: string
  link?: string
}

const banners = ref<Banner[]>([
  {
    id: 1,
    image: bannerImage,
    title: '배너 1',
  },
  {
    id: 2,
    image: bannerImage,
    title: '배너 2',
  },
])

const currentIndex = ref(0)
const autoPlayInterval = ref<number | null>(null)

const handleBannerClick = (banner: Banner) => {
  if (banner.link) {
    window.location.href = banner.link
  }
}

const startAutoPlay = () => {
  if (banners.value.length > 1) {
    autoPlayInterval.value = window.setInterval(() => {
      currentIndex.value = (currentIndex.value + 1) % banners.value.length
    }, 5000)
  }
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
.banner-section {
  position: relative;
  width: 100%;
  height: 88px;
  overflow: hidden;
  border-radius: 8px;
  cursor: pointer;
}

.banner-section__slider {
  position: absolute;
  inset: 0;
  display: flex;
  transition: transform 700ms ease-in-out;
}

.banner-section__slide {
  position: relative;
  flex-shrink: 0;
  height: 100%;
  width: 100%;
  border-radius: 6px;
  border: 1px solid var(--GraySacle-Line, #E5E7EB);
  background-color: #F3F4F6;
}

.banner-section__image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: left center;
}

.banner-section__indicators {
  display: none;
}

.banner-section__indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  border: none;
  background-color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s;
  padding: 0;
}

.banner-section__indicator--active {
  background-color: white;
  width: 24px;
  border-radius: 4px;
}
</style>

