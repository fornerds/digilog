<template>
  <div class="liked-products-page">
    <div class="liked-products-page__content">
      <div class="liked-products-page__section">
        <div class="liked-products-page__count">총 {{ products.length }}개 제품</div>
        <div class="liked-products-page__products-grid">
          <div
            v-for="(product, index) in products"
            :key="product.id"
            class="liked-products-page__product-card"
            @click="handleProductClick(product)"
          >
            <div class="liked-products-page__product-image-wrapper">
              <button 
                class="liked-products-page__product-heart"
                @click.stop="toggleProductLike(index)"
              >
                <span 
                  class="liked-products-page__heart-icon"
                  v-html="product.isLiked ? heartFilledIcon : heartOutlineIcon"
                ></span>
              </button>
              <img
                :src="product.image"
                :alt="product.name"
                class="liked-products-page__product-image"
              />
            </div>
            <div class="liked-products-page__product-info">
              <div class="liked-products-page__product-brand">{{ product.brand }}</div>
              <div class="liked-products-page__product-name">{{ product.name }}</div>
              <div class="liked-products-page__product-price">
                <span class="liked-products-page__product-price-value">{{ formatPrice(product.price) }}</span>
                <span class="liked-products-page__product-price-unit">원</span>
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
import example01 from '@/assets/images/example01.png'
import example02 from '@/assets/images/example02.png'
import example03 from '@/assets/images/example03.png'
import heartFilledIcon from '@/assets/icons/heart-filled.svg?raw'
import heartOutlineIcon from '@/assets/icons/heart-outline.svg?raw'

const router = useRouter()

interface Product {
  id: number
  brand: string
  name: string
  price: number
  image: string
  isLiked: boolean
}

const products = ref<Product[]>([
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
    name: '나이아신아마이드 세럼',
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

const handleProductClick = (product: Product) => {
  console.log('Product clicked:', product)
  // TODO: 제품 상세 페이지로 이동 로직 추가
}

const toggleProductLike = (index: number) => {
  products.value[index].isLiked = !products.value[index].isLiked
  // 좋아요 해제 시 목록에서 제거할 수도 있음
  if (!products.value[index].isLiked) {
    products.value.splice(index, 1)
  }
}

const formatPrice = (price: number) => {
  return price.toLocaleString('ko-KR')
}
</script>

<style scoped>
.liked-products-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 54px 0 100px;
}

.liked-products-page__content {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 0 16px;
}

.liked-products-page__section {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
}

.liked-products-page__count {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext2);
  padding: 0 4px;
}

.liked-products-page__products-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16px 7px;
  align-items: flex-start;
}

.liked-products-page__product-card {
  width: 168px;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.liked-products-page__product-image-wrapper {
  position: relative;
  width: 168px;
  aspect-ratio: 1;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 10px;
}

.liked-products-page__product-heart {
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

.liked-products-page__heart-icon {
  width: 23px;
  height: 21px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  filter: drop-shadow(0 0 14.9px rgba(0, 0, 0, 0.12));
}

.liked-products-page__heart-icon :deep(svg) {
  width: 23px;
  height: 21px;
  display: block;
}

.liked-products-page__product-image {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
  border-radius: 4px;
}

.liked-products-page__product-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: flex-start;
  width: 100%;
  padding: 0;
}

.liked-products-page__product-brand {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 12px;
  line-height: 1.35;
  color: var(--beige3);
  width: 100%;
}

.liked-products-page__product-name {
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

.liked-products-page__product-price {
  display: flex;
  gap: 2px;
  align-items: flex-start;
  font-size: 14px;
  line-height: 1.5;
  white-space: nowrap;
  width: 100%;
}

.liked-products-page__product-price-value {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  color: var(--graysacle-text);
}

.liked-products-page__product-price-unit {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  color: var(--graysacle-subtext1);
}
</style>

