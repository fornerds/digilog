<template>
  <section class="banner-section">
    <div
      v-if="banner"
      class="banner-section__banner"
      @click="handleBannerClick(banner)"
    >
      <img 
        :src="banner.imageUrl" 
        :alt="banner.title"
        class="banner-section__image"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getBanners } from '@/api/banner.api'
import bannerImage from '@/assets/images/banner.png'

interface Banner {
  id: number
  title: string
  description: string
  imageUrl: string
}

const banner = ref<Banner | null>(null)

const handleBannerClick = (banner: Banner) => {
  // 배너 클릭 시 처리 로직 (필요시 추가)
}

onMounted(async () => {
  try {
    const response = await getBanners()
    if (response.data?.items && response.data.items.length > 0) {
      banner.value = response.data.items[0]
    } else {
      // API에서 데이터가 없을 경우 기본 이미지 사용
      banner.value = {
        id: 0,
        title: '배너',
        description: '',
        imageUrl: bannerImage,
      }
    }
  } catch (error) {
    console.error('배너 조회 실패:', error)
    // 에러 발생 시 기본 이미지 사용
    banner.value = {
      id: 0,
      title: '배너',
      description: '',
      imageUrl: bannerImage,
    }
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
}

.banner-section__banner {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 6px;
  border: 1px solid var(--GraySacle-Line, #E5E7EB);
  background-color: #F3F4F6;
  cursor: pointer;
}

.banner-section__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: left center;
}
</style>

