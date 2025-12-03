<template>
  <div class="form-input-wrapper">
    <label v-if="label" :for="id || 'email'" class="form-label">
      {{ label }}
      <span v-if="required" class="text-[#B59A79] relative top-[-2px] inline-block">*</span>
    </label>
    
    <!-- 이메일 입력: 앞부분과 도메인 부분 분리 -->
    <div class="flex items-center gap-2">
      <!-- 이메일 앞부분 -->
      <div class="flex-1 relative">
        <input
          :id="`${id || 'email'}-local`"
          type="text"
          :value="localPart"
          placeholder="이메일 주소"
          :disabled="disabled"
          :class="inputClass"
          @input="handleLocalInput"
          @blur="$emit('blur')"
        />
      </div>
      
      <!-- @ 아이콘 -->
      <div class="flex-shrink-0 flex items-center justify-center">
        <Icon name="at" :size="20" color="#4B5563" />
      </div>
      
      <!-- 도메인 선택 드롭다운 -->
      <div class="flex-1 relative">
        <select
          :id="`${id || 'email'}-domain`"
          :value="domain"
          :disabled="disabled"
          :class="selectClass"
          @change="handleDomainChange"
          @blur="$emit('blur')"
        >
          <option value="">선택</option>
          <option value="gmail.com">gmail.com</option>
          <option value="naver.com">naver.com</option>
          <option value="daum.net">daum.net</option>
          <option value="kakao.com">kakao.com</option>
          <option value="custom">직접 입력</option>
        </select>
        
        <!-- 드롭다운 화살표 아이콘 -->
        <div class="absolute right-0 top-1/2 -translate-y-1/2 pointer-events-none">
          <Icon name="chevron-down" :size="17" color="#BAC2D0" />
        </div>
        
        <!-- 커스텀 도메인 입력 -->
        <input
          v-if="domain === 'custom'"
          :id="`${id || 'email'}-domain-custom`"
          type="text"
          :value="customDomain"
          placeholder="도메인 입력"
          :disabled="disabled"
          :class="inputClass"
          @input="handleCustomDomainInput"
          @blur="$emit('blur')"
        />
      </div>
    </div>
    
    <p v-if="error" class="form-error">{{ error }}</p>
    <p v-else-if="hint" class="form-hint">{{ hint }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Props {
  id?: string
  label?: string
  modelValue: string
  error?: string
  hint?: string
  required?: boolean
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  label: '이메일',
  required: true,
  disabled: false,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'blur': []
}>()

// 이메일을 localPart@domain으로 분리
const localPart = ref('')
const domain = ref('')
const customDomain = ref('')

// modelValue를 파싱하여 초기값 설정
watch(() => props.modelValue, (value) => {
  if (value && value.includes('@')) {
    const parts = value.split('@')
    localPart.value = parts[0] || ''
    const domainPart = parts[1] || ''
    
    // 기본 도메인 목록에 있는지 확인
    const defaultDomains = ['gmail.com', 'naver.com', 'daum.net', 'kakao.com']
    if (defaultDomains.includes(domainPart)) {
      domain.value = domainPart
      customDomain.value = ''
    } else if (domainPart) {
      domain.value = 'custom'
      customDomain.value = domainPart
    } else {
      domain.value = ''
      customDomain.value = ''
    }
  } else {
    localPart.value = value || ''
  }
}, { immediate: true })

// 전체 이메일 주소 업데이트
const updateEmail = () => {
  let fullEmail = ''
  
  if (localPart.value) {
    if (domain.value === 'custom' && customDomain.value) {
      fullEmail = `${localPart.value}@${customDomain.value}`
    } else if (domain.value && domain.value !== 'custom') {
      fullEmail = `${localPart.value}@${domain.value}`
    } else {
      fullEmail = localPart.value
    }
  }
  
  emit('update:modelValue', fullEmail)
}

const handleLocalInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  localPart.value = target.value
  updateEmail()
}

const handleDomainChange = (event: Event) => {
  const target = event.target as HTMLSelectElement
  domain.value = target.value
  if (domain.value !== 'custom') {
    customDomain.value = ''
  }
  updateEmail()
}

const handleCustomDomainInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  customDomain.value = target.value
  updateEmail()
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
  
  return base.join(' ')
})

const selectClass = computed(() => {
  const base = [
    'w-full',
    'px-0',
    'py-3.5',
    'pr-6', // 아이콘 공간 확보
    'border-0',
    'border-b-2',
    'rounded-none',
    'text-base',
    'transition-all',
    'focus:outline-none',
    'bg-transparent',
    'text-gray-900',
    'appearance-none',
    'cursor-pointer',
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

.form-hint {
  @apply mt-1.5 text-xs text-gray-500;
}

</style>
