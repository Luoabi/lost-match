const { config } = require('../config/index')

/**
 * 封装的请求方法
 */
function request(options) {
  const { url, method = 'GET', data, header = {} } = options
  
  // 获取token
  const token = wx.getStorageSync('token')
  
  return new Promise((resolve, reject) => {
    wx.request({
      url: config.apiBaseUrl + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...header
      },
      success: (res) => {
        const response = res.data
        
        if (response.code === 200) {
          resolve(response)
        } else if (response.code === 401) {
          // token过期，跳转登录
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          wx.redirectTo({
            url: '/pages/login/login'
          })
          reject(response)
        } else {
          wx.showToast({
            title: response.message || '请求失败',
            icon: 'none'
          })
          reject(response)
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

/**
 * GET请求
 */
function get(url, data) {
  return request({ url, method: 'GET', data })
}

/**
 * POST请求
 */
function post(url, data) {
  return request({ url, method: 'POST', data })
}

/**
 * PUT请求
 */
function put(url, data) {
  return request({ url, method: 'PUT', data })
}

/**
 * DELETE请求
 */
function del(url, data) {
  return request({ url, method: 'DELETE', data })
}

/**
 * 上传图片
 */
function uploadImage(filePath) {
  console.log('=== uploadImage 函数开始 ===')
  console.log('文件路径:', filePath)
  
  const token = wx.getStorageSync('token')
  
  console.log('上传地址:', config.uploadUrl)
  console.log('Token:', token ? '已设置' : '未设置')
  
  return new Promise((resolve, reject) => {
    console.log('开始调用 wx.uploadFile')
    
    wx.uploadFile({
      url: config.uploadUrl,
      filePath,
      name: 'file',
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        console.log('=== wx.uploadFile success 回调 ===')
        console.log('上传响应状态码:', res.statusCode)
        console.log('上传响应数据:', res.data)
        console.log('上传响应数据类型:', typeof res.data)
        
        try {
          const data = JSON.parse(res.data)
          console.log('解析后的数据:', data)
          console.log('data.code:', data.code)
          console.log('data.data:', data.data)
          
          if (data.code === 200 || res.statusCode === 200) {
            // 兼容多种返回格式
            const url = data.data?.url || data.data || data.url
            
            console.log('提取的URL:', url)
            
            if (url) {
              console.log('准备 resolve URL:', url)
              resolve(url)
            } else {
              console.error('返回数据中没有找到URL:', data)
              wx.showToast({
                title: '上传失败：未返回URL',
                icon: 'none'
              })
              reject(new Error('未返回URL'))
            }
          } else {
            console.error('上传失败，code不是200:', data.code)
            console.error('错误信息:', data.message)
            wx.showToast({
              title: data.message || '上传失败',
              icon: 'none'
            })
            reject(data)
          }
        } catch (error) {
          console.error('=== 解析响应失败 ===')
          console.error('错误:', error)
          console.error('原始响应:', res.data)
          wx.showToast({
            title: '上传失败：响应格式错误',
            icon: 'none'
          })
          reject(error)
        }
      },
      fail: (err) => {
        console.error('=== wx.uploadFile fail 回调 ===')
        console.error('上传请求失败:', err)
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      },
      complete: () => {
        console.log('=== wx.uploadFile complete 回调 ===')
      }
    })
  })
}

module.exports = {
  request,
  get,
  post,
  put,
  del,
  uploadImage
}
