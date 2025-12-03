<template>
  <Button
    :type="type"
    :variant="variant"
    :size="size"
    :disabled="disabled"
    :loading="loading"
    :full-width="fullWidth"
    :rounded="rounded"
    :icon="icon"
    :icon-right="iconRight"
    :class="buttonClass"
    @click="$emit('click', $event)"
  >
    <slot />
  </Button>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Button from './Button.vue'

interface Props {
  type?: 'button' | 'submit' | 'reset'
  variant?: 'primary' | 'secondary' | 'tertiary' | 'danger' | 'outline' | 'ghost' | 'text'
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  disabled?: boolean
  loading?: boolean
  icon?: string
  iconRight?: boolean
  fullWidth?: boolean
  rounded?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'button',
  variant: 'primary',
  size: 'md',
  disabled: false,
  loading: false,
  iconRight: false,
  fullWidth: false,
  rounded: false,
})

defineEmits<{
  click: [event: MouseEvent]
}>()

const buttonClass = computed(() => {
  return ''
})
</script>

