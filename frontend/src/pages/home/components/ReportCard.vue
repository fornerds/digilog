<template>
  <div 
    class="report-card"
    @click="$emit('click')"
  >
    <div class="report-card__top">
      <div class="report-card__date-group">
        <span class="report-card__date">{{ formatRelativeTime(report.createdAt) }}</span>
        <div class="report-card__icon">
          <Icon name="chevron-right" :size="12" color="#1f2937" />
        </div>
      </div>
      <div class="report-card__chart">
        <RadarChart :scores="normalizedScores" />
      </div>
    </div>
    <div class="report-card__divider"></div>
    <div class="report-card__bottom">
      <div class="report-card__info-line">
        <span class="report-card__info-label">피부나이</span>
        <span class="report-card__info-value">{{ report.skinAge }}</span>
      </div>
      <div class="report-card__info-line">
        <span class="report-card__info-label">피부타입</span>
        <span class="report-card__info-value">{{ report.skinType }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { formatRelativeTime } from '@/utils/formatters'
import Icon from '@/components/common/Icon/Icon.vue'
import { RadarChart } from '@/components/common/RadarChart'
import type { AnalysisScores } from '@/types/report.types'

interface Report {
  id: number
  skinAge: number
  skinType: string
  createdAt: string
  scores?: Partial<AnalysisScores>
}

interface Props {
  report: Report
}

const props = defineProps<Props>()
defineEmits<{
  click: []
}>()

const normalizedScores = computed(() => {
  const scores = props.report.scores || {}
  const normalized: Partial<AnalysisScores & { redness?: number; keratin?: number }> = {}
  
  const scoreKeys: (keyof (AnalysisScores & { redness?: number; keratin?: number }))[] = [
    'pores',
    'redness',
    'blackheads',
    'keratin',
    'darkCircles',
    'porphyrin',
  ]
  
  scoreKeys.forEach((key) => {
    const value = scores[key as keyof AnalysisScores]
    normalized[key] = value !== undefined && value !== null ? Math.max(0, value) : 0
  })
  
  return normalized
})
</script>

<style scoped>
.report-card {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line);
  border-radius: 8px;
  width: 188px;
  min-width: 188px;
  max-width: 250px;
  padding: 12px 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
  overflow: hidden;
  cursor: pointer;
  flex-shrink: 0;
}

.report-card__top {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 0 12px;
}

.report-card__date-group {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-left: 4px;
  padding-right: 0;
}

.report-card__date {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext3);
  text-align: center;
  white-space: nowrap;
}

.report-card__icon {
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1.333px;
  border-radius: 5.333px;
}

.report-card__chart {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}


.report-card__divider {
  height: 0;
  border-top: 1px solid var(--graysacle-line);
  margin: 0 -1px;
}

.report-card__bottom {
  display: flex;
  flex-direction: column;
  gap: 3px;
  padding: 0 12px;
}

.report-card__info-line {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  text-align: center;
  white-space: nowrap;
}

.report-card__info-label {
  color: var(--graysacle-subtext1);
}

.report-card__info-value {
  color: var(--beige3);
}
</style>
