<template>
  <div class="personal-color-page">
    <div class="personal-color-page__content">
      <div class="personal-color-page__section">
        <div class="personal-color-page__card">
          <button 
            class="personal-color-page__question-button"
            @click="toggleQuestionTooltip"
          >
            <span class="personal-color-page__question-text">?</span>
          </button>
          <div 
            v-if="showQuestionTooltip"
            class="personal-color-page__tooltip"
          >
            <div class="personal-color-page__tooltip-content">
              퍼스널컬러는 개인의 피부톤, 눈동자, 머리카락 색상과 조화를 이루어 자연스럽고 건강하게 보이도록 하는 색상군을 말합니다. 올바른 퍼스널컬러를 알면 더욱 매력적이고 생기있어 보일 수 있어요.
            </div>
          </div>
          <div class="personal-color-page__card-content">
            <div class="personal-color-page__card-title">
              <div class="personal-color-page__card-title-line">
                <span class="personal-color-page__card-title-name">김지은</span>
                <span class="personal-color-page__card-title-text">님의 <span class="personal-color-page__card-title-underline">퍼스널컬러</span>는</span>
              </div>
              <div class="personal-color-page__card-title-main">봄 웜 브라이트.</div>
            </div>
            <div class="personal-color-page__card-divider"></div>
            <div class="personal-color-page__card-description">
              따뜻하고 깊은 색감이 어울리는 타입으로,전체의 3%가 이 타입에 해당해요.
            </div>
            <div class="personal-color-page__card-list">
              <div 
                v-for="(item, index) in personalColorFeatures"
                :key="index"
                class="personal-color-page__card-item"
              >
                <div class="personal-color-page__card-item-icon">
                  <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M13.3333 4L6 11.3333L2.66667 8" stroke="#61627f" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                </div>
                <span>{{ item }}</span>
              </div>
            </div>
          </div>
          <div class="personal-color-page__card-gradient"></div>
        </div>
        
        <div class="personal-color-page__lab-section">
          <div class="personal-color-page__lab-header">
            <h2 class="personal-color-page__lab-title">Lab 분석값</h2>
            <div class="personal-color-page__lab-charts">
              <div 
                v-for="(item, index) in labData"
                :key="index"
                class="personal-color-page__lab-chart"
              >
                <div 
                  class="personal-color-page__lab-bar-container"
                  :ref="el => setLabBarRef(el, index)"
                >
                  <div 
                    class="personal-color-page__lab-bar-fill"
                    :style="{ backgroundColor: item.color }"
                  ></div>
                </div>
                <div class="personal-color-page__lab-chart-label">
                  <div class="personal-color-page__lab-chart-name">{{ item.label }}</div>
                  <div class="personal-color-page__lab-chart-value">{{ item.displayValue }}</div>
                </div>
              </div>
            </div>
          </div>
          <div class="personal-color-page__lab-info">
            <div class="personal-color-page__lab-info-icon">
              <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M6 10.5C8.48528 10.5 10.5 8.48528 10.5 6C10.5 3.51472 8.48528 1.5 6 1.5C3.51472 1.5 1.5 3.51472 1.5 6C1.5 8.48528 3.51472 10.5 6 10.5Z" stroke="#6b7280" stroke-width="1.125" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M6 8.25V6" stroke="#6b7280" stroke-width="1.125" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M6 3.75H6.00563" stroke="#6b7280" stroke-width="1.125" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <p class="personal-color-page__lab-info-text">
              Lab 값은 색상, 밝기, 채도, 명도 등을 수치로 표현하는 색상 공간의 값으로, L값이 높으면 밝고 화사한 피부톤, a, b값으로 붉은기·노란기 정도를 파악합니다.
            </p>
          </div>
        </div>
      </div>
      
      <div class="personal-color-page__divider"></div>
      
      <div class="personal-color-page__color-recommendations">
        <div class="personal-color-page__color-recommendation">
          <h3 class="personal-color-page__color-title">🙆 따뜻하고 깊은 톤의 색상이 어울려요!</h3>
          <div class="personal-color-page__color-list">
            <div 
              v-for="(color, index) in warmColors"
              :key="index"
              class="personal-color-page__color-item"
              :style="{ backgroundColor: color }"
            ></div>
          </div>
          <p class="personal-color-page__color-description">
            따뜻하고 깊은 톤의 색상들이 당신의 피부톤을 더욱 빛나게 해줍니다.
          </p>
        </div>
        <div class="personal-color-page__color-recommendation">
          <h3 class="personal-color-page__color-title">🙅 차가운 톤의 색상은 피하세요.</h3>
          <div class="personal-color-page__color-list">
            <div 
              v-for="(color, index) in coolColors"
              :key="index"
              class="personal-color-page__color-item"
              :style="{ backgroundColor: color }"
            ></div>
          </div>
          <p class="personal-color-page__color-description">
            차가운 톤의 색상들은 안색이 안 좋아보이는 역효과를 불러올 수 있어요.
          </p>
        </div>
      </div>
      
      <div class="personal-color-page__divider"></div>
      
      <div class="personal-color-page__contouring-section">
        <div class="personal-color-page__contouring-header">
          <h2 class="personal-color-page__contouring-title">컨투어링 가이드</h2>
          <button class="personal-color-page__contouring-question-button">
            <span class="personal-color-page__question-text">?</span>
          </button>
        </div>
        <div class="personal-color-page__contouring-content">
          <div class="personal-color-page__contouring-image-wrapper">
            <img 
              :src="contouringImage" 
              alt="컨투어링 가이드"
              class="personal-color-page__contouring-image"
            />
            <div 
              v-for="(point, index) in contouringPoints"
              :key="index"
              class="personal-color-page__contouring-point"
              :style="{ 
                left: point.left, 
                top: point.top 
              }"
            >
              <div class="personal-color-page__contouring-point-icon">
                <Icon :name="point.iconName" :size="24" color="#374151" />
              </div>
            </div>
          </div>
          <div class="personal-color-page__contouring-list">
            <div 
              v-for="(item, index) in contouringItems"
              :key="index"
              class="personal-color-page__contouring-item"
            >
              <div class="personal-color-page__contouring-item-icon">
                <Icon :name="item.iconName" :size="24" color="#374151" />
              </div>
              <p class="personal-color-page__contouring-item-text">
                <span class="personal-color-page__contouring-item-bold">{{ item.name }}</span>
                <span>{{ item.description }}</span>
              </p>
            </div>
          </div>
        </div>
      </div>
      
      <div class="personal-color-page__products-section">
        <div class="personal-color-page__products-header">
          <div class="personal-color-page__products-title">
            <p>봄 웜 브라이트와</p>
            <p>어울리는 색조 제품은?</p>
          </div>
          <button class="personal-color-page__products-more">
            <Icon name="chevron-right" :size="16" color="#4b5563" />
          </button>
        </div>
        <div class="personal-color-page__products-list">
          <div
            v-for="(product, index) in recommendedProducts"
            :key="index"
            class="personal-color-page__product-card"
            @click="handleProductClick(product)"
          >
            <div class="personal-color-page__product-image-wrapper">
              <button 
                class="personal-color-page__product-heart"
                @click.stop="toggleProductLike(index)"
              >
                <span 
                  class="personal-color-page__heart-icon"
                  v-html="product.isLiked ? heartFilledIcon : heartOutlineIcon"
                ></span>
              </button>
              <img
                :src="product.image"
                :alt="product.name"
                class="personal-color-page__product-image"
              />
            </div>
            <div class="personal-color-page__product-info">
              <div class="personal-color-page__product-brand">{{ product.brand }}</div>
              <div class="personal-color-page__product-name">{{ product.name }}</div>
              <div class="personal-color-page__product-price">
                <span class="personal-color-page__product-price-value">{{ formatPrice(product.price) }}</span>
                <span class="personal-color-page__product-price-unit">원</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import * as d3 from 'd3'
import Icon from '@/components/common/Icon/Icon.vue'
import example01 from '@/assets/images/example01.png'
import example02 from '@/assets/images/example02.png'
import example03 from '@/assets/images/example03.png'
import example04 from '@/assets/images/example04.png'
import heartFilledIcon from '@/assets/icons/heart-filled.svg?raw'
import heartOutlineIcon from '@/assets/icons/heart-outline.svg?raw'

const route = useRoute()
const reportId = route.params.reportId as string

const showQuestionTooltip = ref(false)

const labData = ref([
  { label: 'L 명도', value: 65, displayValue: '65', color: '#bac2d0', maxValue: 100 },
  { label: 'a 적-녹축', value: 12, displayValue: '+12', color: '#f36c76', maxValue: 50 },
  { label: 'b 황-청축', value: 18, displayValue: '+18', color: '#73a5f6', maxValue: 50 },
])

const labBarRefs = ref<(HTMLElement | null)[]>([])

const setLabBarRef = (el: Element | null, index: number) => {
  if (el) {
    labBarRefs.value[index] = el as HTMLElement
  }
}

const animateLabBars = () => {
  nextTick(() => {
    labData.value.forEach((item, index) => {
      const containerElement = labBarRefs.value[index]
      if (!containerElement) return

      const fillElement = containerElement.querySelector('.personal-color-page__lab-bar-fill') as HTMLElement
      if (!fillElement) return

      const maxHeight = 105
      const targetHeight = (item.value / item.maxValue) * maxHeight

      d3.select(fillElement)
        .style('height', '0px')
        .transition()
        .duration(1000)
        .delay(index * 100)
        .ease(d3.easeCubicOut)
        .style('height', `${targetHeight}px`)
    })
  })
}

const personalColorFeatures = ref([
  '황색 베이스의 따뜻한 피부톤',
  '깊고 진한 색상이 잘 어울림',
  '골드 악세사리가 더 잘 어울림',
  '자연스럽고 성숙한 이미지',
])

const warmColors = ref(['#e8b4b8', '#d4b896', '#a67b5b', '#8b4513', '#cd853f'])
const coolColors = ref(['#87ceeb', '#4169e1', '#8a2be2', '#ff1493', '#00ff7f'])

const contouringImage = example04

const contouringPoints = ref([
  { type: 'blusher', iconName: 'blusher', left: '65%', top: '60%' },
  { type: 'shading', iconName: 'shading', left: '55%', top: '78%' },
  { type: 'highlight', iconName: 'highlight', left: '50%', top: '28%' },
])

const contouringItems = ref([
  { type: 'blusher', iconName: 'blusher', name: '블러셔', description: ' 광대뼈 가장 높은 부분에서 관자놀이 방향으로' },
  { type: 'shading', iconName: 'shading', name: '쉐딩', description: ' 턱선과 헤어라인을 따라 자연스럽게' },
  { type: 'highlight', iconName: 'highlight', name: '하이라이트', description: ' 이마, 코끝, 턱 끝에 포인트로 ' },
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

const toggleQuestionTooltip = () => {
  showQuestionTooltip.value = !showQuestionTooltip.value
}

const handleProductClick = (product: typeof recommendedProducts.value[0]) => {
  console.log('Product clicked:', product)
}

const toggleProductLike = (index: number) => {
  recommendedProducts.value[index].isLiked = !recommendedProducts.value[index].isLiked
}

const formatPrice = (price: number) => {
  return price.toLocaleString('ko-KR')
}

onMounted(() => {
  setTimeout(() => {
    animateLabBars()
  }, 500)
})
</script>

<style scoped>
.personal-color-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 16px 40px;
}

.personal-color-page__content {
  display: flex;
  flex-direction: column;
  gap: 50px;
}

.personal-color-page__section {
  display: flex;
  flex-wrap: wrap;
  gap: 26px;
  align-items: center;
  justify-content: center;
}

.personal-color-page__card {
  background-color: #c9d7f8;
  position: relative;
  width: 343px;
  height: 352px;
  border-radius: 16px;
  overflow: hidden;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.personal-color-page__question-button {
  position: absolute;
  top: 23.5px;
  right: 20px;
  width: 23px;
  height: 23px;
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line-weak);
  border-radius: 99px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 4;
}

.personal-color-page__question-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
}

.personal-color-page__tooltip {
  position: absolute;
  top: calc(23.5px + 23px + 6px);
  right: 20px;
  z-index: 5;
}

.personal-color-page__tooltip-content {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line-weak);
  border-radius: 8px;
  padding: 16px;
  width: 303px;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext1);
}

.personal-color-page__card-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 26px;
  width: 100%;
}

.personal-color-page__card-title {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.personal-color-page__card-title-line {
  display: flex;
  align-items: center;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 20px;
  line-height: 1.5;
  color: #61627f;
}

.personal-color-page__card-title-name {
  max-width: 128px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.personal-color-page__card-title-text {
  white-space: nowrap;
}

.personal-color-page__card-title-underline {
  text-decoration: underline;
  text-underline-position: from-font;
}

.personal-color-page__card-title-main {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 32px;
  line-height: 1.5;
  color: #61627f;
}

.personal-color-page__card-divider {
  height: 0;
  border-top: 0.5px solid #61627f;
  width: 100%;
}

.personal-color-page__card-description {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.35;
  color: #61627f;
}

.personal-color-page__card-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.personal-color-page__card-item {
  display: flex;
  gap: 8px;
  align-items: center;
}

.personal-color-page__card-item-icon {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.personal-color-page__card-item span {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.35;
  color: #61627f;
  white-space: nowrap;
}

.personal-color-page__card-gradient {
  position: absolute;
  inset: 8px;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.6));
  mix-blend-mode: overlay;
  border-radius: 12px;
  pointer-events: none;
  z-index: 1;
}

.personal-color-page__lab-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 330px;
  flex: 1;
}

.personal-color-page__lab-header {
  display: flex;
  gap: 16px;
  align-items: flex-start;
  justify-content: center;
  padding: 16px 2px;
}

.personal-color-page__lab-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  flex: 1;
}

.personal-color-page__lab-charts {
  display: flex;
  gap: 12px;
  align-items: center;
  padding: 0 10px;
}

.personal-color-page__lab-chart {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
  width: 60px;
}

.personal-color-page__lab-bar-container {
  background-color: var(--graysacle-box1);
  width: 60px;
  height: 105px;
  border-radius: 12px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  padding: 0;
}

.personal-color-page__lab-bar-fill {
  width: 60px;
  border-radius: 6px 6px 12px 12px;
  height: 0;
  transition: height 0.3s ease;
}

.personal-color-page__lab-chart-label {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
  width: 100%;
}

.personal-color-page__lab-chart-name {
  display: flex;
  gap: 4px;
  align-items: center;
  justify-content: center;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
  white-space: nowrap;
  width: 100%;
}

.personal-color-page__lab-chart-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 20px;
  line-height: 1.5;
  color: var(--graysacle-text);
  text-align: center;
  width: 100%;
}

.personal-color-page__lab-info {
  display: flex;
  gap: 2px;
  align-items: flex-start;
  padding: 0 2px;
}

.personal-color-page__lab-info-icon {
  width: 19px;
  height: 20px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.personal-color-page__lab-info-text {
  flex: 1;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.3;
  color: var(--graysacle-subtext2);
}

.personal-color-page__divider {
  height: 0;
  border-top: 0.5px solid var(--graysacle-line-highlight);
  width: 100%;
}

.personal-color-page__color-recommendations {
  display: flex;
  flex-wrap: wrap;
  gap: 60px;
  align-items: flex-start;
  justify-content: flex-end;
}

.personal-color-page__color-recommendation {
  display: flex;
  flex-direction: column;
  gap: 16px;
  flex: 1;
  min-width: 300px;
}

.personal-color-page__color-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 20px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  padding: 0 2px;
  white-space: nowrap;
}

.personal-color-page__color-list {
  display: flex;
  gap: 6.5px;
  align-items: center;
}

.personal-color-page__color-item {
  flex: 1;
  aspect-ratio: 1;
  border-radius: 8px;
  min-width: 0;
}

.personal-color-page__color-description {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-subtext2);
  margin: 0;
}

.personal-color-page__contouring-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.personal-color-page__contouring-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 2px;
}

.personal-color-page__contouring-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
}

.personal-color-page__contouring-question-button {
  background-color: var(--graysacle-box2);
  border: 1px solid var(--graysacle-line-weak);
  border-radius: 99px;
  width: 23px;
  height: 23px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.personal-color-page__contouring-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.personal-color-page__contouring-image-wrapper {
  position: relative;
  width: 100%;
  aspect-ratio: 343 / 412;
  border-radius: 8px;
  overflow: hidden;
}

.personal-color-page__contouring-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
  border-radius: 8px;
}

.personal-color-page__contouring-point {
  position: absolute;
  background-color: rgba(255, 255, 255, 0.77);
  border: 1px solid white;
  border-radius: 999px;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  transform: translate(-50%, -50%);
  z-index: 10;
}

.personal-color-page__contouring-point-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.personal-color-page__contouring-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.personal-color-page__contouring-item {
  background-color: var(--graysacle-box2);
  display: flex;
  gap: 8px;
  align-items: flex-start;
  padding: 6px;
  border-radius: 6px;
}

.personal-color-page__contouring-item-icon {
  width: 24px;
  height: 24px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.personal-color-page__contouring-item-text {
  flex: 1;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext1);
  margin: 0;
}

.personal-color-page__contouring-item-bold {
  font-weight: 800;
  line-height: 1.5;
}

.personal-color-page__products-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.personal-color-page__products-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  padding: 0 2px;
}

.personal-color-page__products-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
}

.personal-color-page__products-title p {
  margin: 0;
}

.personal-color-page__products-more {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  border-radius: 999px;
  background: transparent;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
}

.personal-color-page__products-more:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.personal-color-page__products-list {
  display: flex;
  gap: 6px;
  overflow-x: auto;
  padding-bottom: 4px;
}

.personal-color-page__products-list::-webkit-scrollbar {
  height: 6px;
}

.personal-color-page__products-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.personal-color-page__products-list::-webkit-scrollbar-thumb {
  background: var(--color-digilog-brown);
  border-radius: 10px;
}

.personal-color-page__product-card {
  flex-shrink: 0;
  width: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.personal-color-page__product-image-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 10px;
}

.personal-color-page__product-heart {
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

.personal-color-page__heart-icon {
  width: 23px;
  height: 21px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 14.9px rgba(0, 0, 0, 0.12));
}

.personal-color-page__heart-icon :deep(svg) {
  width: 23px;
  height: 21px;
  display: block;
}

.personal-color-page__product-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
  border-radius: 4px;
}

.personal-color-page__product-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  width: 100%;
}

.personal-color-page__product-brand {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 12px;
  line-height: 1.35;
  color: var(--beige3);
  width: 100%;
}

.personal-color-page__product-name {
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

.personal-color-page__product-price {
  display: flex;
  gap: 2px;
  align-items: flex-start;
  font-size: 14px;
  line-height: 1.5;
  white-space: nowrap;
  width: 100%;
}

.personal-color-page__product-price-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  color: var(--graysacle-text);
}

.personal-color-page__product-price-unit {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  color: var(--graysacle-subtext1);
}
</style>
