import { config } from '../config/index'

interface RequestOptions {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  header?: any
}

interface Response<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 封装的请求方法
 */
export function request<T = any>(options: RequestOptions): Promise<Response<T>> {
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
      success: (res: any) => {
        const response = res.data as Response<T>
        
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
export function get<T = any>(url: string, data?: any): Promise<Response<T>> {
  return request<T>({ url, method: 'GET', data })
}

/**
 * POST请求
 */
export function post<T = any>(url: string, data?: any): Promise<Response<T>> {
  return request<T>({ url, method: 'POST', data })
}

/**
 * PUT请求
 */
export function put<T = any>(url: string, data?: any): Promise<Response<T>> {
  return request<T>({ url, method: 'PUT', data })
}

/**
 * DELETE请求
 */
export function del<T = any>(url: string, data?: any): Promise<Response<T>> {
  return request<T>({ url, method: 'DELETE', data })
}

/**
 * 上传图片
 */
export function uploadImage(filePath: string): Promise<string> {
  const token = wx.getStorageSync('token')
  
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: config.uploadUrl,
      filePath,
      name: 'file',
      header: {
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        const data = JSON.parse(res.data)
        console.log('上传响应:', data);
        
        if (data.code === 200) {
          // 后端返回的 data 字段直接就是 URL 字符串
          resolve(data.data)
        } else {
          wx.showToast({
            title: data.message || '上传失败',
            icon: 'none'
          })
          reject(data)
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}
