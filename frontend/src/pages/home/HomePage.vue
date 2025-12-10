<template>
  <div class="home-page">
    <HeroBanner />
    <div class="home-page__content">
      <BannerSection />
      <ReportsSection />
      <CommunitySection />
    </div>
  </div>
</template>

<script setup lang="ts">
import HeroBanner from './components/HeroBanner.vue'
import BannerSection from './components/BannerSection.vue'
import ReportsSection from './components/ReportsSection.vue'
import CommunitySection from './components/CommunitySection.vue'
</script>

<style scoped>
.home-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  position: relative;
}

.home-page__content {
  padding: 27px 16px 40px;
  display: flex;
  flex-direction: column;
  gap: 28px;
  position: relative;
  z-index: 1;
}
</style>
