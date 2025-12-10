<template>
  <span
    v-if="iconSvg"
    v-html="iconSvg"
    :class="iconClass"
    :style="iconStyle"
    role="img"
    :aria-label="name"
  />
  <svg
    v-else
    :class="iconClass"
    :style="iconStyle"
    :width="size"
    :height="size"
    :viewBox="viewBox"
    :fill="fill"
    xmlns="http://www.w3.org/2000/svg"
    role="img"
    :aria-label="name"
  >
    <slot />
  </svg>
</template>

<script setup lang="ts">
import { computed, ref, watch, onMounted } from 'vue'

interface Props {
  name?: string
  size?: string | number
  color?: string
  viewBox?: string
  fill?: string
}

const props = withDefaults(defineProps<Props>(), {
  size: '16',
  color: 'currentColor',
  viewBox: '0 0 24 24',
  fill: 'none',
})

const iconSvg = ref<string | null>(null)

const loadIcon = async () => {
  if (props.name) {
    try {
      // SVG 파일을 ?raw로 import하여 텍스트로 가져오기
      const iconModule = await import(`@/assets/icons/${props.name}.svg?raw`)
      let svgContent = iconModule.default
      
      // clipPath ID를 고유하게 만들기 (여러 인스턴스 충돌 방지)
      const uniqueId = `${props.name}_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
      svgContent = svgContent.replace(/clip-path="url\(#([^)]+)\)"/g, (match, id) => {
        return `clip-path="url(#${id}_${uniqueId})"`
      })
      svgContent = svgContent.replace(/id="([^"]*clip[^"]*)"/g, (match, id) => {
        return `id="${id}_${uniqueId}"`
      })
      svgContent = svgContent.replace(/url\(#([^)]+)\)/g, (match, id) => {
        if (!id.includes(uniqueId)) {
          return `url(#${id}_${uniqueId})`
        }
        return match
      })
      
      // SVG의 fill과 stroke를 currentColor로 변경 (이미 currentColor인 경우는 제외)
      svgContent = svgContent.replace(/fill="[^"]*"/g, (match) => {
        if (match.includes('currentColor') || match.includes('none')) {
          return match
        }
        return 'fill="currentColor"'
      })
      svgContent = svgContent.replace(/stroke="[^"]*"/g, (match) => {
        if (match.includes('currentColor')) {
          return match
        }
        return 'stroke="currentColor"'
      })
      
      // width와 height 속성 제거 (CSS로 제어)
      svgContent = svgContent.replace(/\s+width="[^"]*"/g, '')
      svgContent = svgContent.replace(/\s+height="[^"]*"/g, '')
      
      // SVG 태그에 스타일 추가
      const sizeValue = typeof props.size === 'number' ? `${props.size}px` : props.size
      const colorStyle = props.color && props.color !== 'currentColor' 
        ? `color: ${props.color};` 
        : ''
      const sizeStyle = `width: ${sizeValue}; height: ${sizeValue};`
      const combinedStyle = `${sizeStyle} ${colorStyle} display: block; vertical-align: middle;`
      
      if (svgContent.includes('style=')) {
        svgContent = svgContent.replace(
          /style="([^"]*)"/,
          `style="$1; ${combinedStyle}"`
        )
      } else {
        svgContent = svgContent.replace(
          /<svg([^>]*)>/,
          `<svg$1 style="${combinedStyle}">`
        )
      }
      
      // SVG 루트에 color 속성 추가 (currentColor가 아닌 경우)
      if (props.color && props.color !== 'currentColor') {
        svgContent = svgContent.replace(
          /<svg([^>]*)>/,
          `<svg$1 color="${props.color}">`
        )
      }
      
      iconSvg.value = svgContent
    } catch (error) {
      console.error(`Icon "${props.name}" not found:`, error)
      iconSvg.value = null
    }
  } else {
    iconSvg.value = null
  }
}

onMounted(() => {
  loadIcon()
})

watch(() => [props.name, props.color], () => {
  loadIcon()
}, { immediate: false })

const iconClass = computed(() => {
  return [
    'inline-block',
    'flex-shrink-0',
  ].join(' ')
})

const iconStyle = computed(() => {
  const sizeValue = typeof props.size === 'number' ? `${props.size}px` : props.size
  return {
    width: sizeValue,
    height: sizeValue,
    color: props.color,
    display: 'inline-block',
    verticalAlign: 'middle',
  }
})
</script>
