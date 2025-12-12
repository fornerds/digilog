<template>
  <div class="mypage">
    <div class="mypage__content">
      <!-- 프로필 섹션 -->
      <div class="mypage__profile-section">
        <div class="mypage__profile-image-wrapper">
          <img
            :src="userProfile.profileImage"
            :alt="userProfile.name"
            class="mypage__profile-image"
          />
        </div>
        <div class="mypage__profile-info">
          <div class="mypage__name-group">
            <h2 class="mypage__name">{{ userProfile.name }}</h2>
            <button
              class="mypage__edit-button"
              @click="handleEditProfile"
              aria-label="프로필 수정"
            >
              <Icon name="edit-gray" :size="20" color="#4B5563" />
            </button>
          </div>
          <p class="mypage__email">{{ userProfile.email }}</p>
        </div>
      </div>
      
      <!-- 구분선 -->
      <div class="mypage__divider"></div>
      
      <!-- 보고서 관리 섹션 -->
      <div class="mypage__section">
        <div class="mypage__section-header">
          <div class="mypage__section-title-group">
            <h3 class="mypage__section-title">보고서 관리({{ reports.length }})</h3>
            <span class="mypage__new-badge">NEW</span>
          </div>
          <button
            class="mypage__more-button"
            @click="handleReportsMore"
          >
            More
          </button>
        </div>
        <div class="mypage__reports-list">
          <ReportCard
            v-for="report in reports.slice(0, 2)"
            :key="report.id"
            :report="report"
            @click="handleReportClick(report.id)"
          />
        </div>
      </div>
      
      <!-- 구분선 -->
      <div class="mypage__divider"></div>
      
      <!-- 찜한 제품 섹션 -->
      <div class="mypage__section">
        <div class="mypage__section-header">
          <h3 class="mypage__section-title">찜한 제품</h3>
          <button
            class="mypage__more-button"
            @click="handleProductsMore"
          >
            More
          </button>
        </div>
        <div class="mypage__products-list">
          <div
            v-for="product in likedProducts.slice(0, 3)"
            :key="product.id"
            class="mypage__product-card"
            @click="handleProductClick(product)"
          >
            <div class="mypage__product-image-wrapper">
              <button 
                class="mypage__product-heart"
                @click.stop="toggleProductLike(product.id)"
              >
                <span 
                  class="mypage__heart-icon"
                  v-html="product.isLiked ? heartFilledIcon : heartOutlineIcon"
                ></span>
              </button>
              <img
                :src="product.image"
                :alt="product.name"
                class="mypage__product-image"
              />
            </div>
            <div class="mypage__product-info">
              <div class="mypage__product-brand">{{ product.brand }}</div>
              <div class="mypage__product-name">{{ product.name }}</div>
              <div class="mypage__product-price">
                <span class="mypage__product-price-value">{{ formatPrice(product.price) }}</span>
                <span class="mypage__product-price-unit">원</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Icon from '@/components/common/Icon/Icon.vue'
import ReportCard from '@/pages/home/components/ReportCard.vue'
import example01 from '@/assets/images/example01.png'
import example02 from '@/assets/images/example02.png'
import example03 from '@/assets/images/example03.png'
import profile01 from '@/assets/images/profile01.png'
import heartFilledIcon from '@/assets/icons/heart-filled.svg?raw'
import heartOutlineIcon from '@/assets/icons/heart-outline.svg?raw'

const router = useRouter()

interface UserProfile {
  name: string
  email: string
  profileImage: string
}

interface Report {
  id: number
  skinAge: number
  skinType: string
  createdAt: string
  scores?: {
    pores?: number
    blackheads?: number
    pigmentation?: number
    wrinkles?: number
    porphyrin?: number
    sensitivity?: number
  }
}

interface Product {
  id: number
  brand: string
  name: string
  price: number
  image: string
  isLiked: boolean
}

const userProfile = ref<UserProfile>({
  name: '김지은',
  email: 'duswl335@naver.com',
  profileImage: profile01,
})

const reports = ref<Report[]>([
  {
    id: 1,
    skinAge: 27,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-21T00:00:00Z',
    scores: {
      pores: 65,
      blackheads: 70,
      pigmentation: 55,
      wrinkles: 60,
      porphyrin: 75,
      sensitivity: 50,
    },
  },
  {
    id: 2,
    skinAge: 27,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-21T00:00:00Z',
    scores: {
      pores: 60,
      blackheads: 65,
      pigmentation: 50,
      wrinkles: 55,
      porphyrin: 70,
      sensitivity: 45,
    },
  },
  {
    id: 3,
    skinAge: 28,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-20T00:00:00Z',
    scores: {
      pores: 70,
      blackheads: 75,
      pigmentation: 60,
      wrinkles: 65,
      porphyrin: 80,
      sensitivity: 55,
    },
  },
  {
    id: 4,
    skinAge: 27,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-19T00:00:00Z',
    scores: {
      pores: 62,
      blackheads: 68,
      pigmentation: 52,
      wrinkles: 58,
      porphyrin: 72,
      sensitivity: 48,
    },
  },
  {
    id: 5,
    skinAge: 26,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-18T00:00:00Z',
    scores: {
      pores: 58,
      blackheads: 63,
      pigmentation: 48,
      wrinkles: 53,
      porphyrin: 68,
      sensitivity: 43,
    },
  },
  {
    id: 6,
    skinAge: 27,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-17T00:00:00Z',
    scores: {
      pores: 64,
      blackheads: 69,
      pigmentation: 54,
      wrinkles: 59,
      porphyrin: 74,
      sensitivity: 49,
    },
  },
  {
    id: 7,
    skinAge: 28,
    skinType: 'DS (민감 건성)',
    createdAt: '2024-05-16T00:00:00Z',
    scores: {
      pores: 66,
      blackheads: 71,
      pigmentation: 56,
      wrinkles: 61,
      porphyrin: 76,
      sensitivity: 51,
    },
  },
])

const likedProducts = ref<Product[]>([
  {
    id: 1,
    brand: '라운드랩',
    name: '무기자차 선크림',
    price: 22000,
    image: example01,
    isLiked: true,
  },
  {
    id: 2,
    brand: '폴리초이스',
    name: '나이아신마이드 세럼',
    price: 45000,
    image: example02,
    isLiked: true,
  },
  {
    id: 3,
    brand: '라로슈포제',
    name: '세라마이드 수분 크림',
    price: 42000,
    image: example03,
    isLiked: true,
  },
])

const handleEditProfile = () => {
  router.push('/mypage/profile')
}

const handleReportsMore = () => {
  router.push('/reports')
}

const handleProductsMore = () => {
  router.push('/mypage/liked-products')
}

const handleReportClick = (reportId: number) => {
  router.push(`/reports/${reportId}`)
}

const handleProductClick = (product: Product) => {
  console.log('Product clicked:', product)
  // TODO: 제품 상세 페이지로 이동
}

const toggleProductLike = (productId: number) => {
  const product = likedProducts.value.find(p => p.id === productId)
  if (product) {
    product.isLiked = !product.isLiked
  }
}

const formatPrice = (price: number) => {
  return price.toLocaleString('ko-KR')
}
</script>

<style scoped>
.mypage {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 0 100px;
}

.mypage__content {
  display: flex;
  flex-direction: column;
  gap: 26px;
  padding: 0 16px;
}

.mypage__profile-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  justify-content: center;
  padding: 20px 16px;
  height: 251px;
}

.mypage__profile-image-wrapper {
  width: 126px;
  height: 126px;
  border-radius: 366.98px;
  overflow: hidden;
  flex-shrink: 0;
}

.mypage__profile-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
}

.mypage__profile-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.mypage__name-group {
  display: flex;
  gap: 10px;
  align-items: center;
  justify-content: center;
}

.mypage__name {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 24px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mypage__edit-button {
  background-color: var(--graysacle-box1);
  border: none;
  border-radius: 99px;
  width: 34px;
  height: 34px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
  flex-shrink: 0;
  transition: background-color 0.2s;
}

.mypage__edit-button:hover {
  background-color: var(--graysacle-box2);
}

.mypage__email {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  margin: 0;
  padding: 2px 0;
  white-space: pre-wrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mypage__divider {
  height: 0.5px;
  background-color: var(--graysacle-line-weak);
  width: 100%;
}

.mypage__section {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.mypage__section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 40px;
  padding: 0 2px;
}

.mypage__section-title-group {
  display: flex;
  gap: 4px;
  align-items: center;
}

.mypage__section-title {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
}

.mypage__new-badge {
  background-color: var(--graysacle-box2);
  border-radius: 999px;
  padding: 4px 10px;
  height: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 10px;
  line-height: 1.35;
  color: var(--beige3);
  white-space: nowrap;
}

.mypage__more-button {
  border: 1px solid var(--navy3);
  border-radius: 99px;
  height: 22px;
  width: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  cursor: pointer;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 12px;
  line-height: 1.35;
  color: var(--navy3);
  white-space: nowrap;
  transition: background-color 0.2s;
}

.mypage__more-button:hover {
  background-color: rgba(48, 79, 130, 0.05);
}

.mypage__reports-list {
  display: flex;
  gap: 10px;
  align-items: center;
  overflow-x: auto;
  padding: 0 2px;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.mypage__reports-list::-webkit-scrollbar {
  display: none;
}

.mypage__products-list {
  display: flex;
  gap: 6px;
  align-items: center;
  overflow-x: auto;
  padding: 0 2px;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.mypage__products-list::-webkit-scrollbar {
  display: none;
}

.mypage__product-card {
  flex: 0 0 120px;
  max-width: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.mypage__product-image-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
  aspect-ratio: 1;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 10px;
}

.mypage__product-heart {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 2;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2.333px;
  background: transparent;
  border: none;
  cursor: pointer;
}

.mypage__heart-icon {
  width: 23px;
  height: 21px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 14.9px rgba(0, 0, 0, 0.12));
}

.mypage__heart-icon :deep(svg) {
  width: 23px;
  height: 21px;
  display: block;
}

.mypage__product-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
  border-radius: 4px;
}

.mypage__product-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  width: 100%;
  padding: 0;
}

.mypage__product-brand {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 12px;
  line-height: 1.35;
  color: var(--beige3);
  width: 100%;
}

.mypage__product-name {
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

.mypage__product-price {
  display: flex;
  gap: 2px;
  align-items: flex-start;
  font-size: 14px;
  line-height: 1.5;
  white-space: nowrap;
  width: 100%;
}

.mypage__product-price-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  color: var(--graysacle-text);
}

.mypage__product-price-unit {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  color: var(--graysacle-subtext1);
}
</style>
