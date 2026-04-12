import { config } from '../config/index'

/**
 * 获取完整的图片 URL
 * @param {string} url - 图片路径（可能是相对路径或完整 URL）
 * @returns {string} 完整的图片 URL
 */
export function getImageUrl(url) {
  if (!url) {
    return '/images/default.png'
  }
  
  // 如果是本地路径（以 /images/ 开头），返回原路径
  if (url.startsWith('/images/')) {
    return url
  }
  
  // 如果已经是完整 URL（http 或 https），直接返回
  if (url.startsWith('http://') || url.startsWith('https://')) {
    return url
  }
  
  // 如果是相对路径（/api/uploads/...），拼接完整 URL
  if (url.startsWith('/api/') || url.startsWith('/uploads/')) {
    return config.imageBaseUrl + url
  }
  
  // 其他情况，拼接 imageBaseUrl
  return config.imageBaseUrl + '/' + url
}

/**
 * 批量处理图片 URL 数组
 * @param {string[]} urls - 图片路径数组
 * @returns {string[]} 完整的图片 URL 数组
 */
export function getImageUrls(urls) {
  if (!urls || !Array.isArray(urls)) {
    return []
  }
  return urls.map(url => getImageUrl(url))
}
