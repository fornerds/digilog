<template>
  <div class="form-input-wrapper">
    <label v-if="label" :for="id || 'birthDate'" class="form-label">
      {{ label }}
      <span v-if="required" class="text-[#B59A79] relative top-[-2px] inline-block">*</span>
    </label>
    <div class="relative">
      <input
        :id="id || 'birthDate'"
        type="date"
        :value="modelValue"
        :error="error"
        :required="required"
        :disabled="disabled"
        :class="inputClass"
        @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
        @blur="emit('blur')"
      />
      <!-- 캘린더 아이콘 -->
      <button
        type="button"
        class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
        @click="handleCalendarClick"
        tabindex="-1"
      >
        <Icon name="calendar" :size="20" color="#9CA3AF" />
      </button>
    </div>
    <p v-if="error" class="form-error">{{ error }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  id?: string
  label?: string
  modelValue: string
  error?: string
  required?: boolean
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  label: '생년월일',
  required: true,
  disabled: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'blur': []
}>()

const handleCalendarClick = () => {
  const input = document.getElementById(props.id || 'birthDate') as HTMLInputElement
  if (input) {
    input.showPicker?.() || input.click()
  }
}

const inputClass = computed(() => {
  const base = [
    'w-full',
    'px-0',
    'py-3.5',
    'pr-8',
    'border-0',
    'border-b-2',
    'rounded-none',
    'text-base',
    'transition-all',
    'focus:outline-none',
    'bg-transparent',
    'text-gray-900',
    'disabled:bg-transparent',
    'disabled:cursor-not-allowed',
    'disabled:text-gray-500',
  ]
  
  if (props.error) {
    base.push('border-b-[#B0003A]', 'focus:border-b-[#B0003A]')
  } else {
    base.push('border-b-gray-300', 'focus:border-b-[#8C704E]')
  }
  
  return base.join(' ')
})
</script>

<style scoped>
.form-input-wrapper {
  @apply mb-4;
}

.form-label {
  @apply block text-sm font-medium text-gray-700 mb-2;
}

.form-error {
  @apply mt-1 text-sm text-[#B0003A];
}

/* 날짜 입력 필드 스타일 */
input[type="date"]::-webkit-calendar-picker-indicator {
  opacity: 0;
  position: absolute;
  right: 0;
  width: 100%;
  height: 100%;
  cursor: pointer;
}
</style>


