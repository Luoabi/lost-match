const { login } = require('../../api/user')

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },

  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    })
  },

  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
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
      if (res.data && res.data.token) {
        wx.setStorageSync('token', res.data.token)
      }
      
      if (res.data && res.data.userInfo) {
        wx.setStorageSync('userInfo', res.data.userInfo)
      }

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
      wx.showToast({
        title: error.message || '登录失败',
        icon: 'none'
      })
    } finally {
      this.setData({ loading: false })
    }
  },

  goRegister() {
    wx.redirectTo({
      url: '/pages/register/register'
    })
  }
})
