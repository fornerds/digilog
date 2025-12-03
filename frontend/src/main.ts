import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth.store'
import './styles/main.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 앱 초기화: 토큰이 있으면 사용자 정보 가져오기
const authStore = useAuthStore()
authStore.initialize()

app.mount('#app')

