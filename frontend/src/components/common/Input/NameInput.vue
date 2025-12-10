<template>
  <FormInput
    :id="id || 'name'"
    type="text"
    label="이름"
    placeholder="이름을 입력해주세요"
    :model-value="modelValue"
    :error="error"
    :required="required"
    :disabled="disabled"
    max-length="20"
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





