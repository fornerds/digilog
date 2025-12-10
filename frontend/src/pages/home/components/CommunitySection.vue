<template>
  <section class="community-section">
    <div class="community-section__header">
      <div class="community-section__title-group">
        <h2 class="community-section__title">커뮤니티</h2>
        <div class="community-section__badge">
          <span class="community-section__badge-text">NEW</span>
        </div>
      </div>
      <RouterLink to="/community" class="community-section__more">
        <span class="community-section__more-text">More</span>
      </RouterLink>
    </div>
    <div class="community-section__list">
      <CommunityPostCard
        v-for="post in posts"
        :key="post.id"
        :post="post"
        @click="handlePostClick(post.id)"
      />
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import CommunityPostCard from './CommunityPostCard.vue'

const router = useRouter()

const posts = ref([
  {
    id: 1,
    content: '드디어 피부 분석 받아봤어요! DS 타입이라고 나왔는데 생각보다 점수가 좋아서 기뻐요 ✨',
    createdAt: new Date().toISOString(),
  },
  {
    id: 2,
    content: '남자도 스킨케어 열심히 하고 있어요! 퍼스널컬러 분석도 받아보니 웜톤이더라구요. 추천 제품 써보니 확실히 달라요 👍',
    createdAt: new Date().toISOString(),
  },
  {
    id: 3,
    content: '여드름 때문에 고민이었는데 AI 분석 받고 맞춤 케어 시작했어요. 한 달 후기 올릴게요!',
    createdAt: new Date().toISOString(),
  },
])

const handlePostClick = (postId: number) => {
  router.push(`/community/${postId}`)
}
</script>

<style scoped>
.community-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.community-section__header {
  display: flex;
  height: 40px;
  align-items: center;
  justify-content: space-between;
  padding: 0 2px;
}

.community-section__title-group {
  display: flex;
  align-items: center;
  gap: 4px;
}

.community-section__title {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 18px;
  line-height: 1.5;
  color: var(--graysacle-text);
  margin: 0;
  white-space: nowrap;
}

.community-section__badge {
  background-color: var(--graysacle-box2);
  height: 14px;
  padding: 4px 10px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 31px;
}

.community-section__badge-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 800;
  font-size: 10px;
  line-height: 1.35;
  color: var(--beige3);
  text-align: center;
  white-space: nowrap;
}

.community-section__more {
  display: flex;
  height: 40px;
  align-items: center;
  justify-content: flex-end;
  width: 60px;
  text-decoration: none;
}

.community-section__more-text {
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

.community-section__list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 0 4px;
  align-items: center;
  justify-content: center;
}
</style>
