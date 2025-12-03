<template>
  <div class="input-wrapper">
    <label v-if="label" :for="id" class="block text-sm font-medium text-gray-700 mb-1">
      {{ label }}
    </label>
    <input
      :id="id"
      :type="type"
      :value="modelValue"
      :placeholder="placeholder"
      :disabled="disabled"
      :class="inputClass"
      @input="$emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
    <p v-if="error" class="mt-1 text-sm text-red-600">{{ error }}</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  id?: string
  type?: string
  label?: string
  placeholder?: string
  modelValue: string
  disabled?: boolean
  error?: string
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  disabled: false,
})

defineEmits<{
  'update:modelValue': [value: string]
}>()

const inputClass = computed(() => {
  const base = 'w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500'
  const errorClass = props.error ? 'border-red-500' : 'border-gray-300'
  const disabledClass = props.disabled ? 'bg-gray-100 cursor-not-allowed' : ''
  return `${base} ${errorClass} ${disabledClass}`
})
</script>


