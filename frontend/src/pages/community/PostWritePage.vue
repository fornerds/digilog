<template>
  <div class="post-write-page">
    <div class="post-write-page__content">
      <!-- 텍스트 입력 영역 -->
      <div class="post-write-page__section">
        <div class="post-write-page__text-input-wrapper">
          <textarea
            v-model="content"
            class="post-write-page__text-input"
            placeholder="무엇을 공유하고 싶나요?"
            rows="10"
          ></textarea>
        </div>
      </div>
      
      <!-- 해시태그 입력 영역 -->
      <div class="post-write-page__section">
        <div class="post-write-page__hashtag-input-wrapper">
          <input
            v-model="hashtags"
            type="text"
            class="post-write-page__hashtag-input"
            placeholder="해시태그 (예: #여드름#보습#톤업)"
            @keyup.space="handleHashtagSpace"
          />
        </div>
        <div class="post-write-page__hashtag-guide">
          <p class="post-write-page__hashtag-guide-text">띄어쓰기 시 자동으로 해시태그가 추가됩니다.</p>
        </div>
      </div>
      
      <!-- 이미지 업로드 영역 -->
      <div class="post-write-page__images-section">
        <div class="post-write-page__images-list">
          <!-- 이미지 업로드 버튼 -->
          <button
            class="post-write-page__upload-button"
            @click="handleImageUpload"
            aria-label="이미지 업로드"
          >
            <div class="post-write-page__upload-icon-wrapper">
              <Icon name="camera" :size="29" color="#BAC2D0" />
            </div>
          </button>
          
          <!-- 이미지 미리보기 -->
          <ImagePreview
            v-for="(image, index) in uploadedImages"
            :key="index"
            :image-url="image"
            :index="index"
            @delete="handleImageDelete"
          />
        </div>
      </div>
      
      <!-- 숨겨진 파일 입력 -->
      <input
        ref="fileInputRef"
        type="file"
        accept="image/*"
        multiple
        class="post-write-page__file-input"
        @change="handleFileChange"
      />
    </div>
    
    <!-- 비속어 감지 모달 -->
    <ProfanityWarningModal
      :is-open="isProfanityModalOpen"
      @close="handleProfanityModalClose"
      @submit="handleProfanityModalSubmit"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Icon from '@/components/common/Icon/Icon.vue'
import ImagePreview from './components/ImagePreview.vue'
import ProfanityWarningModal from './components/ProfanityWarningModal.vue'

const router = useRouter()
const route = useRoute()

const postId = computed(() => {
  return route.params.postId ? Number(route.params.postId) : null
})

const isEditMode = computed(() => {
  return postId.value !== null
})

const content = ref('')
const hashtags = ref('')
const uploadedImages = ref<string[]>([])
const fileInputRef = ref<HTMLInputElement | null>(null)
const isProfanityModalOpen = ref(false)

// 비속어 감지 함수 (실제로는 API를 통해 검사해야 함)
const checkProfanity = (text: string): boolean => {
  // TODO: 실제 비속어 감지 API 호출
  // 현재는 테스트를 위해 항상 true 반환 (모달 표시)
  // 실제 구현 시에는 API 응답에 따라 결정
  return true
}

const handleUpload = () => {
  // 내용 검증
  if (!content.value.trim()) {
    alert('내용을 입력해주세요.')
    return
  }
  
  // 비속어 감지
  if (checkProfanity(content.value)) {
    isProfanityModalOpen.value = true
    return
  }
  
  // 비속어가 없으면 바로 업로드
  submitPost()
}

const submitPost = () => {
  if (isEditMode.value) {
    // TODO: 실제 게시글 수정 로직 구현
    console.log('Update post:', {
      postId: postId.value,
      content: content.value,
      hashtags: hashtags.value,
      images: uploadedImages.value,
    })
  } else {
    // TODO: 실제 게시글 업로드 로직 구현
    console.log('Upload post:', {
      content: content.value,
      hashtags: hashtags.value,
      images: uploadedImages.value,
    })
  }
  
  // 업로드/수정 후 커뮤니티 페이지로 이동
  router.push('/community')
}

const handleProfanityModalClose = () => {
  isProfanityModalOpen.value = false
}

const handleProfanityModalSubmit = () => {
  isProfanityModalOpen.value = false
  submitPost()
}

const handleImageUpload = () => {
  fileInputRef.value?.click()
}

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement
  const files = target.files
  
  if (files && files.length > 0) {
    Array.from(files).forEach((file) => {
      if (file.type.startsWith('image/')) {
        const reader = new FileReader()
        reader.onload = (e) => {
          const result = e.target?.result as string
          if (result) {
            uploadedImages.value.push(result)
          }
        }
        reader.readAsDataURL(file)
      }
    })
  }
  
  // 파일 입력 초기화 (같은 파일을 다시 선택할 수 있도록)
  if (target) {
    target.value = ''
  }
}

const handleImageDelete = (index: number) => {
  uploadedImages.value.splice(index, 1)
}

const handleHashtagSpace = () => {
  // 띄어쓰기 시 자동으로 해시태그 추가 로직
  // 현재는 단순히 띄어쓰기만 허용
}

// 헤더의 업로드 버튼 클릭 이벤트 리스너
const handleHeaderUpload = () => {
  handleUpload()
}

onMounted(() => {
  // 커스텀 이벤트 리스너 등록
  window.addEventListener('header-upload-click', handleHeaderUpload)
  
  // 수정 모드인 경우 기존 게시글 데이터 불러오기
  if (isEditMode.value && postId.value) {
    // TODO: 실제 API를 통해 게시글 데이터 불러오기
    // 예시 데이터 (실제로는 API에서 가져와야 함)
    // const post = await fetchPost(postId.value)
    // content.value = post.content
    // hashtags.value = post.hashtags?.join(' ') || ''
    // uploadedImages.value = post.images || []
  }
})

onUnmounted(() => {
  window.removeEventListener('header-upload-click', handleHeaderUpload)
})
</script>

<style scoped>
.post-write-page {
  background-color: var(--graysacle-box3);
  min-height: 100vh;
  padding: 44px 0 100px;
}

.post-write-page__content {
  display: flex;
  flex-direction: column;
  gap: 30px;
  padding: 0 16px;
}

.post-write-page__section {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 100%;
}

.post-write-page__text-input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: flex-end;
  width: 100%;
}

.post-write-page__text-input {
  background-color: var(--graysacle-box2);
  border: none;
  border-radius: 6px;
  height: 270px;
  padding: 16px;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-text);
  resize: none;
  outline: none;
  width: 100%;
}

.post-write-page__text-input::placeholder {
  color: var(--graysacle-subtext3);
}

.post-write-page__hashtag-input-wrapper {
  width: 100%;
}

.post-write-page__hashtag-input {
  background-color: var(--graysacle-box2);
  border: none;
  border-radius: 6px;
  height: 48px;
  padding: 0 16px;
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-text);
  outline: none;
  width: 100%;
}

.post-write-page__hashtag-input::placeholder {
  color: var(--graysacle-subtext3);
}

.post-write-page__hashtag-guide {
  padding: 0 6px;
}

.post-write-page__hashtag-guide-text {
  font-family: 'SUIT', sans-serif;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
  margin: 0;
}

.post-write-page__images-section {
  width: 100%;
}

.post-write-page__images-list {
  display: flex;
  flex-wrap: wrap;
  gap: 9px;
  align-items: center;
  min-height: 60px;
  padding: 0 3px;
}

.post-write-page__upload-button {
  border: 1px solid var(--graysacle-line);
  border-radius: 4px;
  width: 60px;
  height: 60px;
  background-color: var(--graysacle-box2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 0;
  flex-shrink: 0;
  transition: background-color 0.2s;
}

.post-write-page__upload-button:hover {
  background-color: var(--graysacle-box1);
}

.post-write-page__upload-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2.667px;
  border-radius: 10.667px;
}

.post-write-page__file-input {
  display: none;
}
</style>
