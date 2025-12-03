<template>
  <div style="padding: 2rem; background: #f9fafb; min-height: 400px;">
    <h1 style="font-size: 2.5rem; font-weight: bold; margin-bottom: 2rem; color: #1f2937;">홈 페이지</h1>
    
    <!-- 간단한 테스트 버튼 -->
    <div style="margin-bottom: 2rem;">
      <button 
        style="padding: 0.75rem 1.5rem; background: #3b82f6; color: white; border-radius: 0.5rem; border: none; cursor: pointer; font-size: 1rem; font-weight: 500;"
        @click="testClick"
      >
        기본 HTML 버튼 (클릭 테스트)
      </button>
    </div>
    
    <div style="display: flex; flex-direction: column; gap: 2rem;">
      <section style="background: white; padding: 1.5rem; border-radius: 0.5rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
        <h2 style="font-size: 1.5rem; font-weight: bold; margin-bottom: 1rem; color: #374151;">Button 컴포넌트 테스트</h2>
        <div style="display: flex; flex-wrap: wrap; gap: 1rem;">
          <Button variant="primary" @click="() => alert('Primary clicked')">Primary</Button>
          <Button variant="secondary">Secondary</Button>
          <Button variant="tertiary">Tertiary</Button>
          <Button variant="danger">Danger</Button>
          <Button variant="outline">Outline</Button>
          <Button variant="ghost">Ghost</Button>
          <Button variant="text">Text</Button>
        </div>
      </section>

      <section>
        <h2 class="text-2xl font-bold mb-4">Sizes</h2>
        <div class="flex flex-wrap items-center gap-4">
          <Button size="xs">XS</Button>
          <Button size="sm">SM</Button>
          <Button size="md">MD</Button>
          <Button size="lg">LG</Button>
          <Button size="xl">XL</Button>
        </div>
      </section>

      <section>
        <h2 class="text-2xl font-bold mb-4">States</h2>
        <div class="flex flex-wrap gap-4">
          <Button>Normal</Button>
          <Button disabled>Disabled</Button>
          <Button loading>Loading</Button>
        </div>
      </section>

      <section>
        <h2 class="text-2xl font-bold mb-4">Button Groups</h2>
        <ButtonGroup>
          <Button variant="outline">취소</Button>
          <Button>확인</Button>
        </ButtonGroup>
      </section>

      <div class="mt-8">
        <a href="/components/buttons" class="text-blue-600 hover:underline">
          전체 버튼 쇼케이스 보기 →
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import Button from '@/components/common/Button/Button.vue'
import ButtonGroup from '@/components/common/Button/ButtonGroup.vue'

const testClick = () => {
  alert('HTML 버튼이 클릭되었습니다!')
}
</script>

<style scoped>
.home-page {
  /* 스타일 */
}
</style>


