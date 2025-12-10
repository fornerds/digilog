<template>
  <FormInput
    :id="id || 'phone'"
    type="tel"
    label="휴대폰번호"
    placeholder="010-1234-5678"
    :model-value="modelValue"
    :error="error"
    :required="required"
    :disabled="disabled"
    max-length="13"
    hint="하이픈(-)을 포함하여 입력해주세요"
    @update:model-value="$emit('update:modelValue', $event)"
    @blur="$emit('blur')"
  />
</template>

<script setup lang="ts">
import FormInput from './FormInput.vue'

interface Props {
  id?: string
  modelValue: string
  error?: string
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





