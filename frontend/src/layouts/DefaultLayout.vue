<template>
  <div class="layout" :class="{ 'layout--home': isHomePage }">
    <Header :show-back-button="showBackButton" />
    <main class="layout__main">
      <RouterView />
    </main>
    <BottomNavigation />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { RouterView } from 'vue-router'
import Header from '@/components/layout/Header/Header.vue'
import BottomNavigation from '@/components/layout/BottomNavigation/BottomNavigation.vue'

const route = useRoute()
const isHomePage = computed(() => route.name === 'Home')
const isReportDetailPage = computed(() => route.name === 'ReportDetail')
const isReportsPage = computed(() => route.name === 'Reports')
const showBackButton = computed(() => isReportDetailPage.value || isReportsPage.value)
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #f9fafb;
  padding-bottom: 80px;
  position: relative;
}

.layout--home {
  background-color: var(--graysacle-box3);
  padding-bottom: 80px;
}

.layout__main {
  width: 100%;
}
</style>

