<template>
  <div class="terms-checkbox-wrapper">
    <Checkbox
      :id="id"
      :model-value="modelValue"
      :disabled="disabled"
      :error="error"
      @update:model-value="$emit('update:modelValue', $event)"
    >
      <span class="terms-text">
        {{ label }}
        <button
          v-if="showLink"
          type="button"
          class="terms-link"
          @click.stop="handleLinkClick"
        >
          {{ linkText }}
        </button>
      </span>
    </Checkbox>
    <p v-if="error" class="terms-error">{{ error }}</p>
  </div>
</template>

<script setup lang="ts">
import Checkbox from './Checkbox.vue'

interface Props {
  id?: string
  label: string
  linkText?: string
  modelValue: boolean
  disabled?: boolean
  error?: string
  showLink?: boolean
}

withDefaults(defineProps<Props>(), {
  linkText: '보기',
  disabled: false,
  showLink: true,
})

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'link-click': []
}>()

const handleLinkClick = () => {
  emit('link-click')
}
</script>

<style scoped>
.terms-checkbox-wrapper {
  @apply mb-3;
}

.terms-text {
  @apply text-sm text-gray-700;
}

.terms-link {
  @apply text-[#8C704E] underline ml-1 hover:text-[#6B5638] transition-colors;
}

.terms-error {
  @apply mt-1 text-sm text-[#B0003A] ml-7;
}
</style>

