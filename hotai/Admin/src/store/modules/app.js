import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: true,
      withoutAnimation: false
    },
    device: 'desktop'
  }),
  
  actions: {
    toggleSidebar() {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = false
    },
    
    closeSidebar(withoutAnimation) {
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
    },
    
    setDevice(device) {
      this.device = device
    }
  },
  
  persist: {
    key: 'app-store',
    storage: localStorage,
    paths: ['sidebar']
  }
})
