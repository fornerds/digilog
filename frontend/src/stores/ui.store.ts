import { defineStore } from 'pinia'
import { ref } from 'vue'

export type Language = 'ko' | 'en' | 'ja'

const languageNames: Record<Language, string> = {
  ko: '한국어',
  en: 'English',
  ja: '日本語',
}

export const useUIStore = defineStore('ui', () => {
  // State
  const isLoading = ref(false)
  const sidebarOpen = ref(false)
  const theme = ref<'light' | 'dark'>('light')
  const language = ref<Language>((localStorage.getItem('language') as Language) || 'ko')

  // Actions
  const setLoading = (value: boolean) => {
    isLoading.value = value
  }

  const toggleSidebar = () => {
    sidebarOpen.value = !sidebarOpen.value
  }

  const setSidebarOpen = (value: boolean) => {
    sidebarOpen.value = value
  }

  const toggleTheme = () => {
    theme.value = theme.value === 'light' ? 'dark' : 'light'
  }

  const setTheme = (value: 'light' | 'dark') => {
    theme.value = value
  }

  const toggleLanguage = () => {
    const languages: Language[] = ['ko', 'en', 'ja']
    const currentIndex = languages.indexOf(language.value)
    const nextIndex = (currentIndex + 1) % languages.length
    language.value = languages[nextIndex]
    localStorage.setItem('language', language.value)
  }

  const setLanguage = (value: Language) => {
    language.value = value
    localStorage.setItem('language', language.value)
  }

  const getLanguageName = (lang?: Language): string => {
    return languageNames[lang || language.value]
  }

  return {
    // State
    isLoading,
    sidebarOpen,
    theme,
    language,
    // Actions
    setLoading,
    toggleSidebar,
    setSidebarOpen,
    toggleTheme,
    setTheme,
    toggleLanguage,
    setLanguage,
    getLanguageName,
  }
})


