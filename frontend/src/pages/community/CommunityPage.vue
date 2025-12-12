<template>
  <div class="community-page">
    <div class="community-page__content">
      <!-- 공지사항 섹션 -->
      <div class="community-page__notices">
        <div
          v-for="(notice, index) in notices"
          :key="index"
          class="community-page__notice"
          @click="handleNoticeClick(notice)"
        >
          <div class="community-page__notice-content">
            <span class="community-page__notice-label">{{ notice.label }}</span>
            <span class="community-page__notice-text">{{ notice.text }}</span>
          </div>
          <div class="community-page__notice-time">{{ notice.time }}</div>
        </div>
      </div>
      
      <!-- 정렬 드롭다운 -->
      <div class="community-page__sort-section">
        <div class="community-page__sort-wrapper">
          <button
            class="community-page__sort-button"
            :class="{ 'community-page__sort-button--open': isSortOpen }"
            @click="toggleSortMenu"
          >
            <span class="community-page__sort-text">{{ selectedSortOption }}</span>
            <Icon
              name="chevron-down"
              :size="12"
              color="#6b7280"
              :class="{ 'community-page__sort-icon--rotated': isSortOpen }"
            />
          </button>
          <div
            v-if="isSortOpen"
            class="community-page__sort-dropdown"
          >
            <button
              v-for="option in sortOptions"
              :key="option.value"
              class="community-page__sort-option"
              :class="{ 'community-page__sort-option--active': selectedSortOption === option.label }"
              @click="selectSortOption(option)"
            >
              {{ option.label }}
            </button>
          </div>
        </div>
      </div>
      
      <!-- 게시글 리스트 -->
      <div class="community-page__posts">
        <div
          v-for="post in posts"
          :key="post.id"
          class="community-page__post-item"
        >
          <CommunityPostCard
            :post="post"
            @click="handlePostClick(post.id)"
            @menu-click="handlePostMenuClick(post.id)"
            @like-click="handlePostLikeClick(post.id)"
            @comment-click="handlePostCommentClick(post.id)"
            @share-click="handlePostShareClick(post.id)"
          />
          <div v-if="post.id !== posts[posts.length - 1].id" class="community-page__divider"></div>
        </div>
      </div>
    </div>
    
    <!-- 이벤트 모달 -->
    <EventModal
      :is-open="isModalOpen"
      :notice="selectedNotice"
      @close="handleModalClose"
    />
    
    <!-- 댓글 바텀시트 -->
    <CommentBottomSheet
      :is-open="isCommentSheetOpen"
      :post-id="selectedPostId"
      @close="handleCommentSheetClose"
      @comment-sent="handleCommentSent"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import CommunityPostCard from './components/CommunityPostCard.vue'
import EventModal from './components/EventModal.vue'
import CommentBottomSheet from './components/CommentBottomSheet.vue'
import Icon from '@/components/common/Icon/Icon.vue'
import profile01 from '@/assets/images/profile01.png'
import profile02 from '@/assets/images/profile02.png'
import profile03 from '@/assets/images/profile03.png'
import example10 from '@/assets/images/example10.png'
import example11 from '@/assets/images/example11.png'

const router = useRouter()

interface Notice {
  label: string
  text: string
  time: string
  id?: number
  title?: string
  content?: string
  contentLines?: string[]
}

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

interface SortOption {
  value: string
  label: string
}

const notices = ref<Notice[]>([
  {
    label: '공지',
    text: '새로운 AI 분석 기능 업데이트',
    time: '1일 전',
    title: '새로운 AI 분석 기능 업데이트',
    contentLines: [
      '안녕하세요! 디지로그를 이용해주시는 고객 여러분께 감사드립니다.',
      '',
      '더욱 정확하고 빠른 피부 분석을 위해 AI 분석 기능을 업데이트했습니다.',
      '',
      '[주요 업데이트 내용]',
      '- 분석 정확도 향상',
      '- 분석 속도 개선',
      '- 새로운 분석 항목 추가',
      '',
      '앱을 최신 버전으로 업데이트하시면 새로운 기능을 이용하실 수 있습니다.',
      '',
      '더 나은 서비스로 보답하겠습니다.',
    ],
  },
  {
    label: '이벤트',
    text: '퍼스널 컬러 분석 무료 체험 이벤트 신청하고 무료 컨설팅 받자!',
    time: '1일 전',
    title: '퍼스널컬러 분석 무료 체험 이벤트',
    contentLines: [
      '🎉 퍼스널컬러 분석 무료 체험 이벤트를 진행합니다!',
      '',
      '[이벤트 기간] 2024.01.15 ~ 2024.01.31',
      '[참여 방법] 앱 내 퍼스널컬러 분석 메뉴 이용',
      '[혜택]  퍼스널컬러 분석 1회 무료 + 맞춤 제품 추천',
      '',
      '이 기회를 놓치지 마세요!',
      '나에게 가장 어울리는 컬러를 찾아보세요:)',
      '',
      '링크 : https://www.figma.com/design/MCK2E4KRb5pR4FMGkAzqZj/',
    ],
  },
])

const isModalOpen = ref(false)
const selectedNotice = ref<Notice>({
  label: '',
  text: '',
  time: '',
})

const isCommentSheetOpen = ref(false)
const selectedPostId = ref<number | undefined>(undefined)

const isSortOpen = ref(false)
const selectedSortOption = ref('인기 순')

const sortOptions: SortOption[] = [
  { value: 'popular', label: '인기 순' },
  { value: 'latest', label: '최신 순' },
  { value: 'recommended', label: '추천 순' },
]

const posts = ref<Post[]>([
  {
    id: 1,
    authorName: '김지은',
    skinType: 'DS타입',
    profileImage: profile01,
    content: '드디어 피부 분석 받아봤어요! DS 타입이라고 나왔는데 생각보다 점수가 좋아서 기뻐요 ✨',
    hashtags: ['DS타입', '피부분석'],
    image: example10,
    likeCount: 24,
    commentCount: 8,
    createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(), // 2시간 전
  },
  {
    id: 2,
    authorName: '박민수',
    skinType: 'DR타입',
    profileImage: profile02,
    content: '남자도 스킨케어 열심히 하고 있어요! 퍼스널컬러 분석도 받아보니 웜톤이더라구요. 추천 제품 써보니 확실히 달라요 👍',
    hashtags: ['DS타입', '피부분석'],
    likeCount: 15,
    commentCount: 5,
    createdAt: new Date(Date.now() - 4 * 60 * 60 * 1000).toISOString(), // 4시간 전
  },
  {
    id: 3,
    authorName: '이서연',
    skinType: 'DS타입',
    profileImage: profile03,
    content: '여드름 때문에 고민이었는데 AI 분석 받고 맞춤 케어 시작했어요. 한 달 후기 올릴게요!',
    hashtags: ['DS타입', '피부분석'],
    image: example11,
    likeCount: 32,
    commentCount: 12,
    createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(), // 2시간 전
  },
])

const toggleSortMenu = () => {
  isSortOpen.value = !isSortOpen.value
}

const selectSortOption = (option: SortOption) => {
  selectedSortOption.value = option.label
  isSortOpen.value = false
  // TODO: 실제 정렬 로직 구현
}

const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  if (!target.closest('.community-page__sort-wrapper')) {
    isSortOpen.value = false
  }
}

const handleNoticeClick = (notice: Notice) => {
  selectedNotice.value = notice
  isModalOpen.value = true
}

const handleModalClose = () => {
  isModalOpen.value = false
}

const handlePostClick = (postId: number) => {
  router.push(`/community/${postId}`)
}

const handlePostMenuClick = (postId: number) => {
  // TODO: 게시글 메뉴 표시 (수정/삭제)
  console.log('Post menu clicked:', postId)
}

const handlePostLikeClick = (postId: number) => {
  // TODO: 좋아요 토글 로직
  const post = posts.value.find(p => p.id === postId)
  if (post) {
    post.likeCount = (post.likeCount || 0) + 1
  }
}

const handlePostCommentClick = (postId: number) => {
  selectedPostId.value = postId
  isCommentSheetOpen.value = true
}

const handleCommentSheetClose = () => {
  isCommentSheetOpen.value = false
  selectedPostId.value = undefined
}

const handleCommentSent = (text: string) => {
  // TODO: 실제 댓글 전송 로직 구현
  console.log('Comment sent:', text, 'for post:', selectedPostId.value)
  // 댓글 전송 후 바텀시트는 유지 (사용자가 계속 댓글을 볼 수 있도록)
}

const handlePostShareClick = (postId: number) => {
  // TODO: 공유 기능 구현
  console.log('Post share clicked:', postId)
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.community-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 0 100px;
  max-width: 848px;
  margin: 0 auto;
}

.community-page__content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 0 16px;
}

.community-page__notices {
  display: flex;
  flex-direction: column;
  gap: 0;
  padding: 0 2px;
}

.community-page__notice {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 28px;
  cursor: pointer;
}

.community-page__notice-content {
  display: flex;
  gap: 8px;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.community-page__notice-label {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-text);
  white-space: nowrap;
  flex-shrink: 0;
  width: 56px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.community-page__notice-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.6;
  color: var(--graysacle-subtext1);
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.community-page__notice-time {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
  padding: 0 4px;
  white-space: nowrap;
  flex-shrink: 0;
}

.community-page__sort-section {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0 4px;
}

.community-page__sort-wrapper {
  position: relative;
}

.community-page__sort-button {
  background-color: var(--graysacle-box3);
  border: 1px solid var(--graysacle-subtext3);
  border-radius: 99px;
  display: flex;
  gap: 6px;
  align-items: center;
  padding: 6px 10px 6px 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.community-page__sort-button--open {
  border-color: var(--graysacle-subtext3);
}

.community-page__sort-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
}

.community-page__sort-icon--rotated {
  transform: rotate(180deg);
}

.community-page__sort-dropdown {
  position: absolute;
  top: calc(100% + 4px);
  right: 0;
  background-color: var(--graysacle-box3);
  border: 1px solid var(--graysacle-line-weak);
  border-radius: 6px;
  padding: 4px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 120px;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.community-page__sort-option {
  background: transparent;
  border: none;
  border-radius: 6px;
  padding: 6px 12px;
  text-align: left;
  cursor: pointer;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext2);
  white-space: nowrap;
  transition: background-color 0.2s;
}

.community-page__sort-option:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.community-page__sort-option--active {
  color: var(--graysacle-text);
  background-color: rgba(0, 0, 0, 0.05);
}

.community-page__posts {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.community-page__post-item {
  display: flex;
  flex-direction: column;
}

.community-page__divider {
  height: 0.5px;
  background-color: var(--graysacle-line-highlight);
  width: 100%;
  margin: 0;
}
</style>
