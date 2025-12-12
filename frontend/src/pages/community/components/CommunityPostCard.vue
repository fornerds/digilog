<template>
  <div class="community-post-card">
    <div class="community-post-card__profile">
      <div class="community-post-card__profile-info">
        <img
          :src="post.profileImage"
          :alt="post.authorName"
          class="community-post-card__profile-image"
        />
        <div class="community-post-card__profile-details">
          <div class="community-post-card__author-name">{{ post.authorName }}</div>
          <div class="community-post-card__meta">
            <span class="community-post-card__skin-type">{{ post.skinType }}</span>
            <span class="community-post-card__separator">•</span>
            <span class="community-post-card__time">{{ formatTime(post.createdAt) }}</span>
          </div>
        </div>
      </div>
      <button
        class="community-post-card__menu-button"
        @click.stop="handleMenuClick"
        aria-label="메뉴"
      >
        <Icon name="dots" :size="20" color="#4b5563" />
      </button>
    </div>
    
    <div class="community-post-card__content">
      <p class="community-post-card__text">{{ post.content }}</p>
    </div>
    
    <div v-if="post.hashtags && post.hashtags.length > 0" class="community-post-card__hashtags">
      <p class="community-post-card__hashtag-text">{{ formatHashtags(post.hashtags) }}</p>
    </div>
    
    <div v-if="post.image" class="community-post-card__image-wrapper">
      <img
        :src="post.image"
        :alt="post.content"
        class="community-post-card__image"
      />
    </div>
    
    <div class="community-post-card__actions">
      <div class="community-post-card__action-group">
        <button
          class="community-post-card__action-button"
          @click.stop="handleLikeClick"
          aria-label="좋아요"
        >
          <Icon name="heart-like" :size="17" color="#6B7280" />
          <span class="community-post-card__action-count">{{ post.likeCount || 0 }}</span>
        </button>
        <button
          class="community-post-card__action-button"
          @click.stop="handleCommentClick"
          aria-label="댓글"
        >
          <Icon name="heart-comment" :size="17" color="#6B7280" />
          <span class="community-post-card__action-count">{{ post.commentCount || 0 }}</span>
        </button>
      </div>
      <button
        class="community-post-card__share-button"
        @click.stop="handleShareClick"
        aria-label="공유"
      >
        <Icon name="share" :size="17" color="#6B7280" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'

interface Post {
  id: number
  authorName: string
  skinType: string
  profileImage: string
  content: string
  hashtags?: string[]
  image?: string
  likeCount?: number
  commentCount?: number
  createdAt: string
}

interface Props {
  post: Post
}

const props = defineProps<Props>()

const emit = defineEmits<{
  click: []
  'menu-click': []
  'like-click': []
  'comment-click': []
  'share-click': []
}>()

const formatTime = (dateString: string) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  
  if (days > 0) {
    return `${days}일 전`
  } else if (hours > 0) {
    return `${hours}시간 전`
  } else if (minutes > 0) {
    return `${minutes}분 전`
  } else {
    return '방금 전'
  }
}

const formatHashtags = (hashtags: string[]) => {
  return hashtags.map(tag => `#${tag}`).join(' ')
}

const handleMenuClick = () => {
  emit('menu-click')
}

const handleLikeClick = () => {
  emit('like-click')
}

const handleCommentClick = () => {
  emit('comment-click')
}

const handleShareClick = () => {
  emit('share-click')
}
</script>

<style scoped>
.community-post-card {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 24px 2px;
  cursor: pointer;
}

.community-post-card__profile {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.community-post-card__profile-info {
  display: flex;
  gap: 10px;
  align-items: center;
  flex: 1;
}

.community-post-card__profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.community-post-card__profile-details {
  display: flex;
  flex-direction: column;
  gap: 0;
  flex: 1;
}

.community-post-card__author-name {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-text);
  margin-bottom: 2px;
}

.community-post-card__meta {
  display: flex;
  align-items: center;
  gap: 4px;
}

.community-post-card__skin-type {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.community-post-card__separator {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  width: 10px;
  text-align: center;
}

.community-post-card__time {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.community-post-card__menu-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  flex-shrink: 0;
}

.community-post-card__content {
  display: flex;
  align-items: center;
  padding: 0 2px;
}

.community-post-card__text {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.6;
  color: var(--graysacle-text);
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

.community-post-card__hashtags {
  display: flex;
  align-items: center;
  padding: 0 2px;
}

.community-post-card__hashtag-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--beige3);
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

.community-post-card__image-wrapper {
  width: 100%;
  aspect-ratio: 343 / 210;
  border-radius: 4px;
  overflow: hidden;
}

.community-post-card__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: 50% 50%;
}

.community-post-card__actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 40px;
  padding: 0 2px;
}

.community-post-card__action-group {
  display: flex;
  gap: 16px;
  align-items: center;
}

.community-post-card__action-button {
  display: flex;
  gap: 4px;
  align-items: center;
  height: 40px;
  padding: 0 2px;
  background: transparent;
  border: none;
  cursor: pointer;
}

.community-post-card__action-count {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
}

.community-post-card__share-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  padding: 1.667px;
  background: transparent;
  border: none;
  cursor: pointer;
  border-radius: 6.667px;
}
</style>

