<template>
  <div :class="groupClass" role="group">
    <slot />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  orientation?: 'horizontal' | 'vertical'
  attached?: boolean
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  variant?: 'default' | 'outlined' | 'segmented'
}

const props = withDefaults(defineProps<Props>(), {
  orientation: 'horizontal',
  attached: false,
  size: 'md',
  variant: 'default',
})

const groupClass = computed(() => {
  const base = ['inline-flex']
  
  if (props.orientation === 'vertical') {
    base.push('flex-col')
  } else {
    base.push('flex-row')
  }

  if (props.attached) {
    if (props.orientation === 'vertical') {
      base.push('rounded-lg', 'overflow-hidden', 'border', 'border-gray-300')
    } else {
      base.push('rounded-lg', 'overflow-hidden', 'border', 'border-gray-300')
    }
  } else {
    base.push('gap-2')
  }

  if (props.variant === 'segmented') {
    base.push('bg-gray-100', 'p-1', 'rounded-lg')
  }

  return base.join(' ')
})
</script>

<style scoped>
/* 버튼 그룹 내 첫 번째 버튼 스타일 */
:deep(.button-group-item:first-child) {
  @apply rounded-l-lg;
}

:deep(.button-group-item:last-child) {
  @apply rounded-r-lg;
}

:deep(.button-group-item:not(:first-child):not(:last-child)) {
  @apply rounded-none;
}

/* Attached 스타일 */
:deep(.button-group-item.attached) {
  @apply rounded-none border-r-0;
}

:deep(.button-group-item.attached:last-child) {
  @apply rounded-r-lg border-r;
}

:deep(.button-group-item.attached:first-child) {
  @apply rounded-l-lg;
}
</style>





