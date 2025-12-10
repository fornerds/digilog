<template>
  <FormInput
    :id="id || 'passwordConfirm'"
    type="password"
    label="비밀번호 확인"
    placeholder="비밀번호를 다시 입력해주세요"
    :model-value="modelValue"
    :error="error"
    :required="required"
    :disabled="disabled"
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





