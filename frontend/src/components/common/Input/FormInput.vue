<template>
  <div class="form-input-wrapper">
    <label v-if="label" :for="id" class="form-label">
      {{ label }}
      <span v-if="required" class="text-[#B59A79] relative top-[-2px] inline-block">*</span>
    </label>
    <div class="relative">
      <input
        :id="id"
        :type="inputType"
        :value="modelValue"
        :placeholder="placeholder"
        :disabled="disabled"
        :maxlength="maxLength"
        :class="inputClass"
        @input="handleInput"
        @blur="handleBlur"
      />
      <!-- 비밀번호 표시/숨김 토글 -->
      <button
        v-if="type === 'password'"
        type="button"
        @click="togglePasswordVisibility"
        class="absolute right-0 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
        tabindex="-1"
      >
        <Icon
          :name="showPassword ? 'eye' : 'eye-off'"
          :size="20"
          color="#9CA3AF"
        />
      </button>
    </div>
    <p v-if="error" class="form-error">{{ error }}</p>
    <p v-else-if="hint" class="form-hint">{{ hint }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  id?: string
  type?: 'text' | 'email' | 'tel' | 'date' | 'password'
  label?: string
  placeholder?: string
  modelValue: string
  disabled?: boolean
  error?: string
  hint?: string
  required?: boolean
  maxLength?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  disabled: false,
  required: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'blur': []
}>()

const showPassword = ref(false)

const inputType = computed(() => {
  if (props.type === 'password') {
    return showPassword.value ? 'text' : 'password'
  }
  return props.type
})

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}

const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  let value = target.value

  // 휴대폰번호 자동 포맷팅 (010-1234-5678)
  if (props.type === 'tel') {
    value = formatPhoneNumber(value)
  }

  emit('update:modelValue', value)
}

const handleBlur = () => {
  emit('blur')
}

const formatPhoneNumber = (value: string): string => {
  // 숫자만 추출
  const numbers = value.replace(/\D/g, '')
  
  // 11자리 제한
  const limited = numbers.slice(0, 11)
  
  // 포맷팅: 010-1234-5678
  if (limited.length <= 3) {
    return limited
  } else if (limited.length <= 7) {
    return `${limited.slice(0, 3)}-${limited.slice(3)}`
  } else {
    return `${limited.slice(0, 3)}-${limited.slice(3, 7)}-${limited.slice(7)}`
  }
}

const inputClass = computed(() => {
  const base = [
    'w-full',
    'px-0',
    'py-3.5',
    'border-0',
    'border-b-2',
    'rounded-none',
    'text-base',
    'transition-all',
    'focus:outline-none',
    'bg-transparent',
    'text-gray-900',
    'placeholder:text-gray-400',
    'disabled:bg-transparent',
    'disabled:cursor-not-allowed',
    'disabled:text-gray-500',
  ]
  
  if (props.error) {
    base.push('border-b-[#B0003A]', 'focus:border-b-[#B0003A]')
  } else {
    base.push('border-b-gray-300', 'focus:border-b-[#8C704E]')
  }
  
  if (props.type === 'password') {
    base.push('pr-8')
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

.form-hint {
  @apply mt-1.5 text-xs text-gray-500;
}
</style>

