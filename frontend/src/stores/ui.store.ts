import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUIStore = defineStore('ui', () => {
  // State
  const isLoading = ref(false)
  const sidebarOpen = ref(false)
  const theme = ref<'light' | 'dark'>('light')

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

  return {
    // State
    isLoading,
    sidebarOpen,
    theme,
    // Actions
    setLoading,
    toggleSidebar,
    setSidebarOpen,
    toggleTheme,
    setTheme,
  }
})


