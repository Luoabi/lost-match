import { login } from '../../api/user'

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },

  onUsernameInput(e: any) {
    this.setData({
      username: e.detail.value
    })
  },

  onPasswordInput(e: any) {
    this.setData({
      password: e.detail.value
    })
  },
  goRegister() {
    wx.redirectTo({
      url: '/pages/register/register'
    })
  },
  async onLogin() {
    const { username, password } = this.data

    if (!username) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      })
      return
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      })
      return
    }
  
    this.setData({ loading: true })

    try {
      const res = await login({ username, password })
      
      // 保存token和用户信息
      wx.setStorageSync('token', res.data.token)
      wx.setStorageSync('userInfo', res.data.userInfo)

      wx.showToast({
        title: '登录成功',
        icon: 'success'
      })

      // 跳转到首页
      setTimeout(() => {
        wx.switchTab({
          url: '/pages/index/index'
        })
      }, 1500)
    } catch (error) {
      console.error('登录失败', error)
    } finally {
      this.setData({ loading: false })
    }
  }
})
