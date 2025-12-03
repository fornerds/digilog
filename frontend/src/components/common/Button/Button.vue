<template>
  <component
    :is="tag"
    :type="tag === 'button' ? type : undefined"
    :disabled="disabled || loading"
    :class="buttonClass"
    @click="handleClick"
  >
    <span v-if="loading" class="inline-flex items-center">
      <svg
        class="animate-spin -ml-1 mr-2 h-4 w-4"
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
      >
        <circle
          class="opacity-25"
          cx="12"
          cy="12"
          r="10"
          stroke="currentColor"
          stroke-width="4"
        />
        <path
          class="opacity-75"
          fill="currentColor"
          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
        />
      </svg>
      <span v-if="loadingText">{{ loadingText }}</span>
      <span v-else><slot /></span>
    </span>
    <span v-else class="inline-flex items-center justify-center gap-2">
      <Icon
        v-if="icon && !iconRight"
        :name="icon"
        :size="iconSize"
        class="flex-shrink-0"
      />
      <slot />
      <Icon
        v-if="icon && iconRight"
        :name="icon"
        :size="iconSize"
        class="flex-shrink-0"
      />
    </span>
  </component>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Icon from '../Icon/Icon.vue'

interface Props {
  // HTML 속성
  type?: 'button' | 'submit' | 'reset'
  tag?: 'button' | 'a' | 'router-link'
  href?: string
  to?: string | object
  disabled?: boolean
  
  // 스타일 변형
  variant?: 
    | 'primary' 
    | 'secondary' 
    | 'tertiary'
    | 'danger' 
    | 'outline' 
    | 'ghost'
    | 'text'
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  
  // 상태
  loading?: boolean
  loadingText?: string
  
  // 아이콘
  icon?: string
  iconRight?: boolean
  
  // 스타일 옵션
  fullWidth?: boolean
  rounded?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'button',
  tag: 'button',
  variant: 'primary',
  size: 'md',
  disabled: false,
  loading: false,
  iconRight: false,
  fullWidth: false,
  rounded: false,
})

const emit = defineEmits<{
  click: [event: MouseEvent]
}>()

const handleClick = (event: MouseEvent) => {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}

const buttonClass = computed(() => {
  const base = [
    'inline-flex',
    'items-center',
    'justify-center',
    'font-medium',
    'transition-all',
    'duration-200',
    'focus:outline-none',
    'focus:ring-2',
    'focus:ring-offset-2',
    'disabled:opacity-50',
    'disabled:cursor-not-allowed',
    'disabled:pointer-events-none',
  ]

  // Rounded 옵션
  if (props.rounded) {
    base.push('rounded-full')
  } else {
    base.push('rounded-lg')
  }

  // Variant 스타일
  const variants = {
    primary: [
      'bg-[#8C704E]',
      '!bg-[#8C704E]',
      'text-white',
      'hover:bg-[#A9745A]',
      'active:bg-[#8C704E]',
      'focus:ring-[#8C704E]',
      'shadow-sm',
      'hover:shadow-md',
      'min-h-[40px]',
      'min-w-[80px]',
    ],
    secondary: [
      'bg-[#C9D7F8]',
      'text-[#61627F]',
      'hover:bg-[#FFF5CC]',
      'active:bg-[#C9D7F8]',
      'focus:ring-[#C9D7F8]',
      'shadow-sm',
      'hover:shadow-md',
    ],
    tertiary: [
      'bg-[#FFF5CC]',
      'text-[#8C704E]',
      'hover:bg-[#FFEDED]',
      'active:bg-[#FFEBEB]',
      'focus:ring-[#FFF5CC]',
    ],
    danger: [
      'bg-[#B0003A]',
      'text-white',
      'hover:bg-red-700',
      'active:bg-red-800',
      'focus:ring-[#B0003A]',
      'shadow-sm',
      'hover:shadow-md',
    ],
    outline: [
      'border-2',
      'border-[#8C704E]',
      'bg-transparent',
      'text-[#8C704E]',
      'hover:bg-[#FFF5CC]',
      'active:bg-[#FFEDED]',
      'focus:ring-[#8C704E]',
      'hover:border-[#A9745A]',
    ],
    ghost: [
      'bg-transparent',
      'text-[#61627F]',
      'hover:bg-[#FFF5CC]',
      'active:bg-[#FFEDED]',
      'focus:ring-[#C9D7F8]',
    ],
    text: [
      'bg-transparent',
      'text-[#61627F]',
      'hover:text-[#8C704E]',
      'hover:bg-[#FFF5CC]',
      'active:bg-[#FFEDED]',
      'focus:ring-[#C9D7F8]',
      'underline-offset-4',
      'hover:underline',
    ],
  }

  // Size 스타일
  const sizes = {
    xs: ['px-2', 'py-1', 'text-xs', 'gap-1'],
    sm: ['px-3', 'py-1.5', 'text-sm', 'gap-1.5'],
    md: ['px-4', 'py-2', 'text-base', 'gap-2'],
    lg: ['px-6', 'py-3', 'text-lg', 'gap-2'],
    xl: ['px-8', 'py-4', 'text-xl', 'gap-3'],
  }

  // Full width 옵션
  if (props.fullWidth) {
    base.push('w-full')
  }

  return [
    ...base,
    ...variants[props.variant],
    ...sizes[props.size],
  ].join(' ')
})

const iconSize = computed(() => {
  const sizeMap = {
    xs: '12',
    sm: '14',
    md: '16',
    lg: '18',
    xl: '20',
  }
  return sizeMap[props.size]
})
</script>
