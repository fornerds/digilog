<template>
  <div class="p-8 space-y-12">
    <section>
      <h2 class="text-2xl font-bold mb-4">Variants</h2>
      <div class="flex flex-wrap gap-4">
        <Button variant="primary">Primary</Button>
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
        <Button size="xs">Extra Small</Button>
        <Button size="sm">Small</Button>
        <Button size="md">Medium</Button>
        <Button size="lg">Large</Button>
        <Button size="xl">Extra Large</Button>
      </div>
    </section>

    <section>
      <h2 class="text-2xl font-bold mb-4">With Icons</h2>
      <div class="flex flex-wrap gap-4">
        <Button icon="arrow-right">다음</Button>
        <Button icon="arrow-left" icon-right>이전</Button>
        <Button icon="close" variant="ghost" size="sm">닫기</Button>
      </div>
    </section>

    <section>
      <h2 class="text-2xl font-bold mb-4">States</h2>
      <div class="flex flex-wrap gap-4">
        <Button>Normal</Button>
        <Button disabled>Disabled</Button>
        <Button loading>Loading</Button>
        <Button loading loading-text="저장 중...">Save</Button>
      </div>
    </section>

    <section>
      <h2 class="text-2xl font-bold mb-4">Button Groups</h2>
      <div class="space-y-4">
        <ButtonGroup>
          <Button variant="outline">취소</Button>
          <Button>확인</Button>
        </ButtonGroup>

        <ButtonGroup attached>
          <Button variant="outline">이전</Button>
          <Button variant="outline">다음</Button>
        </ButtonGroup>

        <ButtonGroup variant="segmented">
          <Button variant="ghost">목록</Button>
          <Button variant="primary">그리드</Button>
          <Button variant="ghost">카드</Button>
        </ButtonGroup>
      </div>
    </section>

    <section>
      <h2 class="text-2xl font-bold mb-4">Full Width</h2>
      <div class="max-w-md">
        <Button full-width class="mb-2">Full Width Button</Button>
        <Button full-width variant="outline">Full Width Outline</Button>
      </div>
    </section>

    <section>
      <h2 class="text-2xl font-bold mb-4">Rounded</h2>
      <div class="flex flex-wrap gap-4">
        <Button rounded>Rounded Primary</Button>
        <Button rounded variant="outline">Rounded Outline</Button>
        <Button rounded variant="ghost">Rounded Ghost</Button>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import Button from './Button.vue'
import ButtonGroup from './ButtonGroup.vue'
</script>

