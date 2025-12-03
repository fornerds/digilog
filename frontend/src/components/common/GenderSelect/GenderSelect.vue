<template>
  <div class="gender-select-wrapper">
    <label v-if="label" class="form-label">
      {{ label }}
      <span v-if="required" class="text-[#B59A79] relative top-[-2px] inline-block">*</span>
    </label>
    <div class="gender-buttons">
      <button
        type="button"
        :class="[
          'gender-button',
          modelValue === 'female' && 'gender-button-active'
        ]"
        @click="$emit('update:modelValue', 'female')"
      >
        여성
      </button>
      <button
        type="button"
        :class="[
          'gender-button',
          modelValue === 'male' && 'gender-button-active'
        ]"
        @click="$emit('update:modelValue', 'male')"
      >
        남성
      </button>
    </div>
    <p v-if="error" class="form-error">{{ error }}</p>
  </div>
</template>

<script setup lang="ts">
interface Props {
  label?: string
  modelValue: 'female' | 'male' | ''
  error?: string
  required?: boolean
}

withDefaults(defineProps<Props>(), {
  label: '성별',
  required: true,
})

defineEmits<{
  'update:modelValue': [value: 'female' | 'male']
}>()
</script>

<style scoped>
.gender-select-wrapper {
  @apply mb-4;
}

.form-label {
  @apply block text-sm font-medium text-gray-700 mb-2;
}

.gender-buttons {
  @apply flex;
  gap: 8px;
  align-self: stretch;
}

.gender-button {
  @apply flex-1 py-3.5 text-base font-medium transition-all;
  @apply border-0;
  @apply focus:outline-none;
  @apply flex items-center justify-center;
  border-radius: 6px;
  gap: 8px;
  align-self: stretch;
}

.gender-button:not(.gender-button-active) {
  background-color: #F3F4F6;
  color: #959BA9;
}

.gender-button-active {
  background-color: #B59A79;
  color: #FFFBF4;
}

.gender-button:not(.gender-button-active):hover {
  background-color: #E5E7EB;
}

.form-error {
  @apply mt-1 text-sm text-[#B0003A];
}
</style>

