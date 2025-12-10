<template>
  <section class="reports-section">
    <div class="reports-section__header">
      <div class="reports-section__title-group">
        <h2 class="reports-section__title">보고서 관리({{ reports.length }})</h2>
        <div class="reports-section__badge">
          <span class="reports-section__badge-text">NEW</span>
        </div>
      </div>
      <RouterLink to="/reports" class="reports-section__more">
        <span class="reports-section__more-text">More</span>
      </RouterLink>
    </div>
    <div class="reports-section__list">
      <ReportCard
        v-for="report in reports"
        :key="report.id"
        :report="report"
        @click="handleReportClick(report.id)"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import ReportCard from './ReportCard.vue'

const router = useRouter()

const reports = ref([
  {
    id: 1,
    skinAge: 27,
    skinType: 'DS (민감 건성)',
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
    skinType: 'DS (민감 건성)',
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
    skinType: 'DS (민감 건성)',
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
    skinType: 'DS (민감 건성)',
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
])

const handleReportClick = (reportId: number) => {
  router.push(`/reports/${reportId}`)
}
</script>

<style scoped>
.reports-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.reports-section__header {
  display: flex;
  height: 40px;
  align-items: center;
  justify-content: space-between;
  padding: 0 2px;
}

.reports-section__title-group {
  display: flex;
  align-items: center;
  gap: 4px;
}

.reports-section__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
}

.reports-section__badge {
  background-color: var(--graysacle-box2);
  height: 14px;
  padding: 4px 10px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 31px;
}

.reports-section__badge-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 10px;
  line-height: 1.35;
  color: var(--beige3);
  text-align: center;
  white-space: nowrap;
}

.reports-section__more {
  display: flex;
  height: 40px;
  align-items: center;
  justify-content: flex-end;
  width: 60px;
  text-decoration: none;
}

.reports-section__more-text {
  border: 1px solid var(--navy3);
  height: 22px;
  padding: 0 12px;
  border-radius: 99px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 12px;
  line-height: 1.35;
  color: var(--navy3);
  text-align: center;
  white-space: nowrap;
}

.reports-section__list {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.reports-section__list::-webkit-scrollbar {
  height: 6px;
}

.reports-section__list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.reports-section__list::-webkit-scrollbar-thumb {
  background: var(--color-digilog-brown);
  border-radius: 10px;
}
</style>
