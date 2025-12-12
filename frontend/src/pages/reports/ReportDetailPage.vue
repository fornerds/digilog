<template>
  <div class="report-detail-page">
    <div class="report-detail-page__content">
      <!-- 상단 정보 섹션 -->
      <div class="report-detail-page__top-section">
        <div class="report-detail-page__top-block">
          <div class="report-detail-page__date">2024년 1월 15일</div>
          <div class="report-detail-page__title">
            <span class="report-detail-page__title-name">김지은</span>
            <span class="report-detail-page__title-text">님의 피부 측정 결과입니다.</span>
          </div>
        </div>
        <div class="report-detail-page__info-cards">
          <div class="report-detail-page__info-card">
            <div class="report-detail-page__info-label">피부나이</div>
            <div class="report-detail-page__info-value-group">
              <div class="report-detail-page__info-value">27</div>
              <div class="report-detail-page__info-subtext">1살 젊음</div>
            </div>
          </div>
          <div class="report-detail-page__info-card">
            <div class="report-detail-page__info-label">피부상태</div>
            <div class="report-detail-page__info-value-group">
              <div class="report-detail-page__info-value">중성</div>
              <div class="report-detail-page__info-subtext">건강한 상태</div>
            </div>
          </div>
          <div class="report-detail-page__info-card">
            <div class="report-detail-page__info-label">피부코드</div>
            <div class="report-detail-page__info-value-group">
              <div class="report-detail-page__info-value">DS</div>
              <div class="report-detail-page__info-subtext">건성 민감성</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 피부 상태 분석 섹션 -->
      <div class="report-detail-page__analysis-section">
        <div class="report-detail-page__section-header">
          <h2 class="report-detail-page__section-title">피부 상태 분석</h2>
          <button class="report-detail-page__question-button">
            <span class="report-detail-page__question-text">?</span>
          </button>
        </div>
        <div class="report-detail-page__analysis-content">
          <div class="report-detail-page__chart-container">
            <RadarChart :scores="analysisScores" :width="160" :height="160" />
            <div class="report-detail-page__chart-labels">
              <div class="report-detail-page__chart-label report-detail-page__chart-label--top">모공</div>
              <div class="report-detail-page__chart-label report-detail-page__chart-label--top-right">홍조</div>
              <div class="report-detail-page__chart-label report-detail-page__chart-label--bottom-right">블랙헤드</div>
              <div class="report-detail-page__chart-label report-detail-page__chart-label--bottom">포르피린</div>
              <div class="report-detail-page__chart-label report-detail-page__chart-label--bottom-left">각질</div>
              <div class="report-detail-page__chart-label report-detail-page__chart-label--top-left">다크서클</div>
            </div>
          </div>
          <div class="report-detail-page__analysis-list">
          <div class="report-detail-page__analysis-note">
            <div class="report-detail-page__info-icon">
              <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8 14C11.3137 14 14 11.3137 14 8C14 4.68629 11.3137 2 8 2C4.68629 2 2 4.68629 2 8C2 11.3137 4.68629 14 8 14Z" stroke="#6b7280" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M8 11V8" stroke="#6b7280" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M8 5H8.0075" stroke="#6b7280" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <span>점수가 높을수록 피부 상태가 우수함을 의미합니다.</span>
          </div>
            <div
              v-for="(item, index) in analysisItems"
              :key="index"
              class="report-detail-page__analysis-item"
            >
              <div class="report-detail-page__analysis-item-label">
                <span class="report-detail-page__analysis-item-name">{{ item.name }}</span>
                <span
                  class="report-detail-page__analysis-item-score"
                  :class="{ 'report-detail-page__analysis-item-score--warning': item.isWarning }"
                >
                  {{ item.score }}
                </span>
              </div>
              <div 
                class="report-detail-page__analysis-bar"
                :ref="el => setBarRef(el, index)"
              >
                <div
                  class="report-detail-page__analysis-bar-fill"
                  :class="{ 'report-detail-page__analysis-bar-fill--warning': item.isWarning }"
                ></div>
              </div>
            </div>
            <div class="report-detail-page__divider"></div>
            <div class="report-detail-page__tags">
              <span
                v-for="(tag, index) in skinTags"
                :key="index"
                class="report-detail-page__tag"
              >
                {{ tag }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 피부코드 설명 섹션 -->
      <div class="report-detail-page__code-section">
        <div class="report-detail-page__code-header">
          <div class="report-detail-page__code-title">
            <div class="report-detail-page__code-title-line">김지은 님의 피부코드는</div>
            <div class="report-detail-page__code-title-main">DS타입(건성 민감성)</div>
          </div>
        </div>
        <div class="report-detail-page__divider"></div>
        <div class="report-detail-page__code-description">
          수분 부족과 민감한 피부가 특징인 피부로, 수분 부족 형 건성피부라고도 불러요.
        </div>
        <div class="report-detail-page__care-tips">
          <div
            v-for="(tip, index) in careTips"
            :key="index"
            class="report-detail-page__care-tip"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none" class="report-detail-page__care-tip-icon">
              <path d="M8.5835 2H4.50016C4.19074 2 3.894 2.12643 3.6752 2.35147C3.45641 2.57652 3.3335 2.88174 3.3335 3.2V12.8C3.3335 13.1183 3.45641 13.4235 3.6752 13.6485C3.894 13.8736 4.19074 14 4.50016 14H11.5002C11.8096 14 12.1063 13.8736 12.3251 13.6485C12.5439 13.4235 12.6668 13.1183 12.6668 12.8V6.2M8.5835 2L12.6668 6.2M8.5835 2V6.2H12.6668" stroke="#959BA9" stroke-width="1.16667" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <span>{{ tip }}</span>
          </div>
        </div>
      </div>

      <!-- 추천 제품 섹션 -->
      <div class="report-detail-page__products-section">
        <div class="report-detail-page__products-header">
          <div class="report-detail-page__products-title">
            <p>DS타입에겐</p>
            <p>이 제품을 추천해요!</p>
          </div>
          <button 
            class="report-detail-page__products-more"
            @click="handleMoreClick"
          >
            <Icon name="chevron-right" :size="16" color="#4b5563" />
          </button>
        </div>
        <div class="report-detail-page__products-list">
          <div
            v-for="(product, index) in recommendedProducts"
            :key="index"
            class="report-detail-page__product-card"
            @click="handleProductClick(product)"
          >
            <div class="report-detail-page__product-image-wrapper">
              <button 
                class="report-detail-page__product-heart"
                @click.stop="toggleProductLike(index)"
              >
                <span 
                  class="report-detail-page__heart-icon"
                  v-html="product.isLiked ? heartFilledIcon : heartOutlineIcon"
                ></span>
              </button>
              <img
                :src="product.image"
                :alt="product.name"
                class="report-detail-page__product-image"
              />
            </div>
            <div class="report-detail-page__product-info">
              <div class="report-detail-page__product-brand">{{ product.brand }}</div>
              <div class="report-detail-page__product-name">{{ product.name }}</div>
              <div class="report-detail-page__product-price">
                <span class="report-detail-page__product-price-value">{{ formatPrice(product.price) }}</span>
                <span class="report-detail-page__product-price-unit">원</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import * as d3 from 'd3'
import Icon from '@/components/common/Icon/Icon.vue'
import { RadarChart } from '@/components/common/RadarChart'
import example01 from '@/assets/images/example01.png'
import example02 from '@/assets/images/example02.png'
import example03 from '@/assets/images/example03.png'
import heartFilledIcon from '@/assets/icons/heart-filled.svg?raw'
import heartOutlineIcon from '@/assets/icons/heart-outline.svg?raw'
import type { AnalysisScores } from '@/types/report.types'

const route = useRoute()
const router = useRouter()

const reportId = computed(() => route.params.reportId as string)

const analysisScores = ref<Partial<AnalysisScores & { redness: number; keratin: number }>>({
  pores: 38,
  redness: 50,
  blackheads: 27,
  keratin: 60,
  darkCircles: 92,
  porphyrin: 76,
})

const analysisItems = ref([
  { name: '모공', score: 38, isWarning: false },
  { name: '블랙헤드', score: 27, isWarning: false },
  { name: '색소스팟', score: 89, isWarning: false },
  { name: '주름', score: 80, isWarning: false },
  { name: '포르피린', score: 76, isWarning: false },
  { name: '민감도', score: 74, isWarning: true },
  { name: '다크서클', score: 92, isWarning: true },
])

const skinTags = ref([
  '건성피부',
  '민감피부',
  '피부장벽강화',
  '수분진정보습',
  '피부결복원',
  '저자극크림',
])

const careTips = ref([
  '하루 2-3회 수분 크림을 충분히 발라 주세요.',
  '세안 시 미지근한 물을 사용하고 강하게 문지르지 마세요',
  '알코올 성분이 포함된 제품은 피해주세요',
])

const recommendedProducts = ref([
  {
    id: 1,
    brand: '라운드랩',
    name: '무기자차 선크림',
    price: 22000,
    image: example01,
    isLiked: false,
  },
  {
    id: 2,
    brand: '폴리초이스',
    name: '나이아신마이드 세럼',
    price: 45000,
    image: example02,
    isLiked: false,
  },
  {
    id: 3,
    brand: '라로슈포제',
    name: '세라마이드 수분 크림',
    price: 42000,
    image: example03,
    isLiked: false,
  },
])

const handleProductClick = (product: typeof recommendedProducts.value[0]) => {
  console.log('Product clicked:', product)
}

const handleMoreClick = () => {
  router.push(`/reports/${reportId.value}/recommendations`)
}

const toggleProductLike = (index: number) => {
  recommendedProducts.value[index].isLiked = !recommendedProducts.value[index].isLiked
}

const formatPrice = (price: number) => {
  return price.toLocaleString('ko-KR')
}

const barRefs = ref<(HTMLElement | null)[]>([])

const setBarRef = (el: HTMLElement | null, index: number) => {
  if (el) {
    barRefs.value[index] = el
  }
}

const animateBars = () => {
  nextTick(() => {
    analysisItems.value.forEach((item, index) => {
      const barElement = barRefs.value[index]
      if (!barElement) return

      const fillElement = barElement.querySelector('.report-detail-page__analysis-bar-fill') as HTMLElement
      if (!fillElement) return

      const targetWidthPercent = item.score

      d3.select(fillElement)
        .style('width', '0%')
        .transition()
        .duration(1000)
        .delay(index * 50)
        .ease(d3.easeCubicOut)
        .style('width', `${targetWidthPercent}%`)
    })
  })
}

onMounted(() => {
  setTimeout(() => {
    animateBars()
  }, 500)
})
</script>

<style scoped>
.report-detail-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 54px 16px 100px;
}

.report-detail-page__content {
  display: flex;
  flex-direction: column;
  gap: 70px;
}

.report-detail-page__top-section {
  display: flex;
  flex-direction: column;
  gap: 13px;
}

.report-detail-page__top-block {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.report-detail-page__date {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
}

.report-detail-page__title {
  display: flex;
  align-items: center;
  font-size: 20px;
  line-height: 1.5;
  white-space: nowrap;
}

.report-detail-page__title-name {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  color: var(--beige3);
}

.report-detail-page__title-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  color: var(--graysacle-text);
}

.report-detail-page__info-cards {
  background-color: var(--graysacle-box2);
  display: flex;
  gap: 10px;
  padding: 20px 16px;
  border-radius: 8px;
}

.report-detail-page__info-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: center;
  justify-content: center;
  padding: 0 10px;
  height: 112px;
}

.report-detail-page__info-label {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 13px;
  line-height: 1.35;
  color: var(--graysacle-text);
  width: 100%;
}

.report-detail-page__info-value-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  width: 100%;
}

.report-detail-page__info-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 20px;
  line-height: 1.5;
  color: var(--graysacle-black);
}

.report-detail-page__info-subtext {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-text);
}

.report-detail-page__analysis-section {
  display: flex;
  flex-direction: column;
  gap: 50px;
  position: relative;
  isolation: isolate;
}

.report-detail-page__section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 2;
}

.report-detail-page__section-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 20px;
  line-height: 1.5;
  color: var(--navy1);
  margin: 0;
  white-space: nowrap;
}

.report-detail-page__question-button {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line-weak);
  border-radius: 99px;
  width: 23px;
  height: 23px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
}

.report-detail-page__question-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
}

.report-detail-page__analysis-content {
  display: flex;
  flex-wrap: wrap;
  gap: 50px;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.report-detail-page__chart-container {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  height: 228px;
  width: 343px;
  padding: 34px 0;
}

.report-detail-page__chart-container :deep(.radar-chart) {
  width: 160px;
  height: 160px;
}

.report-detail-page__chart-labels {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.report-detail-page__chart-label {
  position: absolute;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext1);
  white-space: nowrap;
}

.report-detail-page__chart-label--top {
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.report-detail-page__chart-label--top-right {
  top: 26.67%;
  right: 11.82%;
  text-align: left;
}

.report-detail-page__chart-label--bottom-right {
  bottom: 27.78%;
  right: 11.82%;
  text-align: left;
  overflow: hidden;
  text-overflow: ellipsis;
}

.report-detail-page__chart-label--bottom {
  bottom: 5%;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.report-detail-page__chart-label--bottom-left {
  bottom: 27.78%;
  left: 11.82%;
  text-align: right;
}

.report-detail-page__chart-label--top-left {
  top: 26.67%;
  left: 11.82%;
  text-align: right;
}

.report-detail-page__analysis-list {
  flex: 1;
  min-width: 300px;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.report-detail-page__analysis-note {
  display: flex;
  gap: 4px;
  align-items: flex-start;
}

.report-detail-page__info-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 17px;
  height: 18px;
  flex-shrink: 0;
}

.report-detail-page__analysis-note span {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.report-detail-page__analysis-item {
  display: flex;
  gap: 18px;
  align-items: center;
}

.report-detail-page__analysis-item-label {
  display: flex;
  gap: 8px;
  align-items: center;
  width: 100px;
  white-space: nowrap;
  font-size: 18px;
  line-height: 1.5;
}

.report-detail-page__analysis-item-name {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  color: var(--graysacle-text);
}

.report-detail-page__analysis-item-score {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  color: var(--navy5);
}

.report-detail-page__analysis-item-score--warning {
  color: #ef6a68;
}

.report-detail-page__analysis-bar {
  flex: 1;
  background-color: var(--graysacle-box1);
  height: 22px;
  border-radius: 8px;
  overflow: hidden;
  min-width: 0;
}

.report-detail-page__analysis-bar-fill {
  height: 100%;
  background-color: var(--navy5);
  border-radius: 8px 4px 4px 8px;
  width: 0;
}

.report-detail-page__analysis-bar-fill--warning {
  background-color: #ef6a68;
}

.report-detail-page__divider {
  height: 0;
  border-top: 0.5px solid rgba(186, 194, 208, 1);
  width: 100%;
}

.report-detail-page__tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.report-detail-page__tag {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line);
  border-radius: 999px;
  padding: 5px 14px;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.6;
  color: var(--navy4);
  white-space: nowrap;
}

.report-detail-page__code-section {
  display: flex;
  flex-direction: column;
  gap: 30px;
  align-items: flex-start;
  padding: 30px 0;
}

.report-detail-page__code-header {
  display: flex;
  flex-direction: column;
  gap: 18px;
  padding: 0 2px;
  width: 100%;
}

.report-detail-page__code-title {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
}

.report-detail-page__code-title-line {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 20px;
  line-height: 1.5;
  color: var(--navy4);
  white-space: nowrap;
}

.report-detail-page__code-title-main {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 28px;
  line-height: 1.5;
  color: var(--graysacle-black);
  width: 100%;
}

.report-detail-page__code-description {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  padding: 0 2px;
}

.report-detail-page__care-tips {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}

.report-detail-page__care-tip {
  background-color: var(--graysacle-box2);
  display: flex;
  gap: 8px;
  align-items: flex-start;
  padding: 6px;
  border-radius: 6px;
}

.report-detail-page__care-tip-icon {
  flex-shrink: 0;
  width: 16px;
  height: 16px;
}

.report-detail-page__care-tip span {
  flex: 1;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext1);
}

.report-detail-page__products-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.report-detail-page__products-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  padding: 0 2px;
}

.report-detail-page__products-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
}

.report-detail-page__products-title p {
  margin: 0;
}

.report-detail-page__products-more {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
}

.report-detail-page__products-list {
  display: flex;
  gap: 6px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.report-detail-page__products-list::-webkit-scrollbar {
  height: 6px;
}

.report-detail-page__products-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.report-detail-page__products-list::-webkit-scrollbar-thumb {
  background: var(--color-digilog-brown);
  border-radius: 10px;
}

.report-detail-page__product-card {
  flex-shrink: 0;
  width: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.report-detail-page__product-image-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  margin-bottom: 10px;
}

.report-detail-page__product-heart {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 2;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  background: transparent;
  border: none;
  cursor: pointer;
}

.report-detail-page__heart-icon {
  width: 23px;
  height: 21px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 14.9px rgba(0, 0, 0, 0.12));
}

.report-detail-page__heart-icon :deep(svg) {
  width: 23px;
  height: 21px;
  display: block;
}

.report-detail-page__product-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
  border-radius: 4px;
}

.report-detail-page__product-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  width: 100%;
  padding-top: 10px;
}

.report-detail-page__product-brand {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 12px;
  line-height: 1.35;
  color: var(--beige3);
}

.report-detail-page__product-name {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
}

.report-detail-page__product-price {
  display: flex;
  gap: 2px;
  align-items: flex-start;
  font-size: 14px;
  line-height: 1.5;
  white-space: nowrap;
}

.report-detail-page__product-price-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  color: var(--graysacle-text);
}

.report-detail-page__product-price-unit {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  color: var(--graysacle-subtext1);
}
</style>
