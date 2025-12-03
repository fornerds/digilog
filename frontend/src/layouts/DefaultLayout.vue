<template>
  <div class="min-h-screen bg-gray-50 pb-20">
    <Header />
    <main class="container mx-auto px-4 py-8">
      <RouterView />
    </main>
    <Footer />
    <BottomNavigation />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import Header from '@/components/layout/Header/Header.vue'
import Footer from '@/components/layout/Footer/Footer.vue'
import BottomNavigation from '@/components/layout/BottomNavigation/BottomNavigation.vue'
</script>

