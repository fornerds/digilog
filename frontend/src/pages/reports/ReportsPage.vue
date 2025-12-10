<template>
  <div class="reports-page">
    <div class="reports-page__content">
      <div class="reports-page__header">
        <h1 class="reports-page__title">김지은님의<br>피부분석 내역입니다.</h1>
      </div>
      
      <div class="reports-page__section">
        <div class="reports-page__toolbar">
          <div class="reports-page__count">총 {{ reports.length }}건</div>
          <button class="reports-page__sort-button">
            <span class="reports-page__sort-text">최신 순</span>
            <Icon name="chevron-down" :size="12" color="#6b7280" />
          </button>
        </div>
        
        <div class="reports-page__list">
          <ReportCard
            v-for="report in reports"
            :key="report.id"
            :report="report"
            @click="handleReportClick(report.id)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ReportCard from '@/pages/home/components/ReportCard.vue'
import Icon from '@/components/common/Icon/Icon.vue'
import type { AnalysisScores } from '@/types/report.types'

interface Report {
  id: number
  skinAge: number
  skinType: string
  createdAt: string
  scores?: Partial<AnalysisScores>
}

const router = useRouter()

const reports = ref<Report[]>([
  {
    id: 1,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(),
    scores: {
      pores: 70,
      redness: 50,
      blackheads: 60,
      keratin: 55,
      darkCircles: 45,
      porphyrin: 55,
    },
  },
  {
    id: 2,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(),
    scores: {
      pores: 65,
      redness: 48,
      blackheads: 55,
      keratin: 52,
      darkCircles: 50,
      porphyrin: 50,
    },
  },
  {
    id: 3,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date('2024-05-21').toISOString(),
    scores: {
      pores: 75,
      redness: 45,
      blackheads: 65,
      keratin: 58,
      darkCircles: 40,
      porphyrin: 60,
    },
  },
  {
    id: 4,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date('2024-05-21').toISOString(),
    scores: {
      pores: 68,
      redness: 52,
      blackheads: 58,
      keratin: 54,
      darkCircles: 48,
      porphyrin: 57,
    },
  },
  {
    id: 5,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date('2024-05-20').toISOString(),
    scores: {
      pores: 72,
      redness: 49,
      blackheads: 62,
      keratin: 56,
      darkCircles: 43,
      porphyrin: 58,
    },
  },
  {
    id: 6,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date('2024-05-19').toISOString(),
    scores: {
      pores: 66,
      redness: 51,
      blackheads: 56,
      keratin: 53,
      darkCircles: 47,
      porphyrin: 52,
    },
  },
  {
    id: 7,
    skinAge: 27,
    skinType: 'DS 민감 건성',
    createdAt: new Date('2024-05-18').toISOString(),
    scores: {
      pores: 71,
      redness: 47,
      blackheads: 61,
      keratin: 55,
      darkCircles: 46,
      porphyrin: 56,
    },
  },
])

const handleReportClick = (reportId: number) => {
  router.push(`/reports/${reportId}`)
}
</script>

<style scoped>
.reports-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 16px 40px;
}

.reports-page__content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.reports-page__header {
  display: flex;
  align-items: center;
  width: 100%;
}

.reports-page__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 24px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
}

.reports-page__section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reports-page__toolbar {
  display: flex;
  height: 31px;
  align-items: flex-end;
  justify-content: space-between;
  padding: 0 4px;
}

.reports-page__count {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.reports-page__sort-button {
  background-color: var(--graysacle-box3);
  border: 1px solid var(--graysacle-subtext3);
  border-radius: 99px;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px 6px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.reports-page__sort-button:hover {
  background-color: var(--graysacle-box2);
}

.reports-page__sort-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.reports-page__list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 10px;
  align-items: flex-start;
}
</style>
