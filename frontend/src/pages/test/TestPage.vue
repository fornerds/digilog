<template>
  <div style="padding: 2rem; background: #f5f5f5; min-height: 100vh;">
    <h1 style="font-size: 2rem; margin-bottom: 2rem;">버튼 테스트 페이지</h1>
    
    <!-- 인라인 스타일 버튼 -->
    <div style="margin-bottom: 2rem;">
      <button 
        style="background: #8C704E; color: white; padding: 0.5rem 1rem; border-radius: 0.5rem; border: none; cursor: pointer;"
      >
        인라인 스타일 버튼
      </button>
    </div>
    
    <!-- Tailwind 클래스 버튼 -->
    <div style="margin-bottom: 2rem;">
      <button class="px-4 py-2 bg-[#8C704E] text-white rounded-lg">
        Tailwind 직접 색상 버튼
      </button>
    </div>
    
    <!-- Button 컴포넌트 -->
    <div style="margin-bottom: 2rem;">
      <h2 style="font-size: 1.5rem; margin-bottom: 1rem;">Button 컴포넌트</h2>
      <Button variant="primary">Primary Button</Button>
      <Button variant="secondary" style="margin-left: 1rem;">Secondary Button</Button>
    </div>
    
    <!-- 디버그 정보 -->
    <div style="background: white; padding: 1rem; border-radius: 0.5rem; margin-top: 2rem;">
      <h3 style="font-size: 1.25rem; margin-bottom: 0.5rem;">디버그 정보</h3>
      <p>페이지가 렌더링되었습니다.</p>
      <p>Button 컴포넌트가 import되었습니다: {{ buttonImported ? 'Yes' : 'No' }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Button from '@/components/common/Button/Button.vue'

const buttonImported = ref(true)
</script>

