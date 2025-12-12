<template>
  <Teleport to="body">
    <div
      v-if="isOpen"
      class="comment-bottom-sheet"
      @click.self="handleClose"
    >
      <div class="comment-bottom-sheet__content">
        <div class="comment-bottom-sheet__handle"></div>
        
        <div class="comment-bottom-sheet__comments">
          <div
            v-for="(comment, index) in comments"
            :key="index"
            class="comment-bottom-sheet__comment-item"
          >
            <div class="comment-bottom-sheet__comment">
              <img
                :src="comment.profileImage"
                :alt="comment.authorName"
                class="comment-bottom-sheet__comment-profile-image"
              />
              <div class="comment-bottom-sheet__comment-content">
                <div class="comment-bottom-sheet__comment-header">
                  <div class="comment-bottom-sheet__comment-author">
                    <span class="comment-bottom-sheet__comment-name">{{ comment.authorName }}</span>
                    <span class="comment-bottom-sheet__comment-separator">•</span>
                    <span class="comment-bottom-sheet__comment-skin-type">{{ comment.skinType }}</span>
                  </div>
                  <div class="comment-bottom-sheet__comment-time">{{ comment.time }}</div>
                </div>
                <div class="comment-bottom-sheet__comment-text">
                  <p v-for="(line, lineIndex) in comment.contentLines" :key="lineIndex" class="comment-bottom-sheet__comment-line">
                    {{ line }}
                  </p>
                </div>
              </div>
            </div>
            <div v-if="index !== comments.length - 1" class="comment-bottom-sheet__divider"></div>
          </div>
        </div>
        
        <div class="comment-bottom-sheet__input-wrapper">
          <div class="comment-bottom-sheet__input-box">
            <input
              v-model="commentText"
              type="text"
              class="comment-bottom-sheet__input"
              placeholder="댓글을 입력해 주세요."
              @keyup.enter="handleSend"
            />
            <button
              class="comment-bottom-sheet__send-button"
              @click="handleSend"
              :disabled="!commentText.trim()"
              aria-label="전송"
            >
              <Icon name="send" :size="16" color="white" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import Icon from '@/components/common/Icon/Icon.vue'
import profile01 from '@/assets/images/profile01.png'
import profile02 from '@/assets/images/profile02.png'
import profile03 from '@/assets/images/profile03.png'

interface Comment {
  authorName: string
  skinType: string
  profileImage: string
  time: string
  contentLines: string[]
}

interface Props {
  isOpen: boolean
  postId?: number
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
  'comment-sent': [text: string]
}>()

const commentText = ref('')

const comments = ref<Comment[]>([
  {
    authorName: '이서연',
    skinType: 'DS타입',
    profileImage: profile03,
    time: '2시간',
    contentLines: ['오 저도 DS예요!'],
  },
  {
    authorName: '김지은',
    skinType: 'DS타입',
    profileImage: profile01,
    time: '2시간',
    contentLines: [
      '오 반갑습니다 ㅎㅎ 건조해서 걱정 많으셨겠어요.',
      '지금은 좀 어떠신지',
    ],
  },
  {
    authorName: '이서연',
    skinType: 'DS타입',
    profileImage: profile03,
    time: '2시간',
    contentLines: ['많이 좋아졌어요!'],
  },
])

watch(() => props.isOpen, (newValue) => {
  if (newValue) {
    commentText.value = ''
  }
})

const handleClose = () => {
  emit('close')
}

const handleSend = () => {
  if (commentText.value.trim()) {
    emit('comment-sent', commentText.value.trim())
    commentText.value = ''
    // TODO: 실제 댓글 전송 로직 구현
  }
}
</script>

<style scoped>
.comment-bottom-sheet {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: flex-end;
  justify-content: center;
  z-index: 1000;
  padding: 0;
}

.comment-bottom-sheet__content {
  background-color: var(--graysacle-background, white);
  border: 1px solid #efeff0;
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  width: 100%;
  height: 80vh;
  max-height: 710px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.comment-bottom-sheet__handle {
  background-color: var(--graysacle-box1);
  height: 4px;
  width: 105px;
  border-radius: 999px;
  margin: 10px auto 0;
  flex-shrink: 0;
}

.comment-bottom-sheet__comments {
  flex: 1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 0;
  min-height: 0;
  padding: 10px 20px 40px 20px;
}

.comment-bottom-sheet__comments::-webkit-scrollbar {
  width: 4px;
}

.comment-bottom-sheet__comments::-webkit-scrollbar-track {
  background: transparent;
}

.comment-bottom-sheet__comments::-webkit-scrollbar-thumb {
  background-color: var(--graysacle-line-weak);
  border-radius: 999px;
}

.comment-bottom-sheet__comment-item {
  display: flex;
  flex-direction: column;
  padding: 8px 0;
}

.comment-bottom-sheet__comment {
  display: flex;
  gap: 10px;
  padding: 8px 2px;
  align-items: flex-start;
}

.comment-bottom-sheet__comment-profile-image {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.comment-bottom-sheet__comment-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.comment-bottom-sheet__comment-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.comment-bottom-sheet__comment-author {
  display: flex;
  gap: 2px;
  align-items: center;
}

.comment-bottom-sheet__comment-name {
  font-family: 'SUIT', sans-serif;
  font-weight: 700;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext1);
  white-space: nowrap;
}

.comment-bottom-sheet__comment-separator {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
  width: 10px;
  text-align: center;
}

.comment-bottom-sheet__comment-skin-type {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-subtext3);
  white-space: nowrap;
}

.comment-bottom-sheet__comment-time {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 12px;
  line-height: 1.35;
  color: var(--graysacle-guidetext, #bac2d0);
  white-space: nowrap;
}

.comment-bottom-sheet__comment-text {
  padding: 2px;
}

.comment-bottom-sheet__comment-line {
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 14px;
  line-height: 1.35;
  color: var(--graysacle-text);
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-bottom-sheet__divider {
  height: 0.5px;
  background-color: var(--graysacle-line-highlight);
  width: 100%;
  margin: 0;
}

.comment-bottom-sheet__input-wrapper {
  background-color: var(--graysacle-background, white);
  padding: 10px;
  flex-shrink: 0;
  border-top: 1px solid var(--graysacle-line-weak);
}

.comment-bottom-sheet__input-box {
  background-color: var(--graysacle-background, white);
  border: 1px solid var(--graysacle-subtext1);
  border-radius: 99px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 6px;
}

.comment-bottom-sheet__input {
  flex: 1;
  height: 100%;
  padding: 0 16px;
  border: none;
  background: transparent;
  font-family: 'SUIT', sans-serif;
  font-weight: 500;
  font-size: 16px;
  line-height: 1.35;
  color: var(--graysacle-text);
  outline: none;
}

.comment-bottom-sheet__input::placeholder {
  color: var(--graysacle-subtext3);
}

.comment-bottom-sheet__send-button {
  background-color: var(--graysacle-subtext1);
  width: 32px;
  height: 32px;
  border-radius: 99px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  padding: 10px;
  flex-shrink: 0;
  transition: opacity 0.2s;
}

.comment-bottom-sheet__send-button:hover:not(:disabled) {
  opacity: 0.9;
}

.comment-bottom-sheet__send-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>

