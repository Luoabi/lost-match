import { defineStore } from 'pinia'
import { login, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null,
    roles: []
  }),
  
  getters: {
    isAdmin: (state) => state.userInfo?.role === 'admin'
  },
  
  actions: {
    // 登录
    async login(loginForm) {
      try {
        const res = await login(loginForm)
        // 后端返回格式: { code: 200, data: { token, userInfo } }
        this.token = res.data.token
        this.userInfo = res.data.userInfo
        this.roles = [res.data.userInfo.role]
        return res
      } catch (error) {
        throw error
      }
    },
    
    // 获取用户信息
    async getUserInfo() {
      try {
        if (!this.userInfo?.id) {
          return
        }
        const res = await getUserInfo(this.userInfo.id)
        // 后端返回格式: { code: 200, data: User对象 }
        this.userInfo = res.data
        this.roles = [res.data.role]
        return res
      } catch (error) {
        throw error
      }
    },
    
    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = null
      this.roles = []
    }
  },
  
  persist: {
    key: 'user-store',
    storage: localStorage,
    paths: ['token', 'userInfo']
  }
})
