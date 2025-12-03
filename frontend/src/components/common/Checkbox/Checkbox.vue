<template>
  <label
    :class="[
      'checkbox-wrapper',
      disabled && 'checkbox-disabled',
      error && 'checkbox-error'
    ]"
  >
    <input
      :id="id"
      type="checkbox"
      :checked="modelValue"
      :disabled="disabled"
      class="checkbox-input"
      @change="handleChange"
    />
    <span class="checkbox-box" :class="{ 'checkbox-checked': modelValue }">
      <svg
        v-if="modelValue"
        class="checkbox-checkmark"
        viewBox="0 0 12 12"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M10 3L4.5 8.5L2 6"
          stroke="white"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </span>
    <span class="checkbox-label">
      <slot>{{ label }}</slot>
    </span>
  </label>
</template>

<script setup lang="ts">
interface Props {
  id?: string
  label?: string
  modelValue: boolean
  disabled?: boolean
  error?: boolean
}

withDefaults(defineProps<Props>(), {
  disabled: false,
  error: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const handleChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  emit('update:modelValue', target.checked)
}
</script>

<style scoped>
.checkbox-wrapper {
  @apply flex items-start gap-2 cursor-pointer;
}

.checkbox-disabled {
  @apply cursor-not-allowed opacity-50;
}

.checkbox-input {
  @apply sr-only;
}

.checkbox-box {
  @apply flex-shrink-0 w-5 h-5 border-2 rounded border-gray-300 bg-white transition-all;
  @apply flex items-center justify-center;
}

.checkbox-box.checkbox-checked {
  @apply bg-[#8C704E] border-[#8C704E];
}

.checkbox-wrapper:hover:not(.checkbox-disabled) .checkbox-box:not(.checkbox-checked) {
  @apply border-[#8C704E];
}

.checkbox-error .checkbox-box {
  @apply border-[#B0003A];
}

.checkbox-checkmark {
  @apply w-3 h-3;
}

.checkbox-label {
  @apply text-sm text-gray-700 flex-1;
}

.checkbox-wrapper:focus-within .checkbox-box {
  @apply ring-2 ring-[#8C704E] ring-offset-2;
}
</style>

