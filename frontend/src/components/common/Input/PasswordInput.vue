<template>
  <FormInput
    :id="id || 'password'"
    type="password"
    :label="label !== undefined ? label : '비밀번호'"
    :placeholder="placeholder || '비밀번호를 입력해주세요'"
    :model-value="modelValue"
    :error="error"
    :required="required"
    :disabled="disabled"
    :hint="hint"
    @update:model-value="$emit('update:modelValue', $event)"
    @blur="$emit('blur')"
  />
</template>

<script setup lang="ts">
import FormInput from './FormInput.vue'

interface Props {
  id?: string
  label?: string
  placeholder?: string
  modelValue: string
  error?: string
  hint?: string
  required?: boolean
  disabled?: boolean
}

withDefaults(defineProps<Props>(), {
  required: true,
  disabled: false,
})

defineEmits<{
  'update:modelValue': [value: string]
  'blur': []
}>()
</script>

