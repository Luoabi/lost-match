import request from '@/utils/request'

// ========== 基础设置 ==========

export function getBasicSettings() {
  return request({
    url: '/settings/basic',
    method: 'get'
  })
}

export function updateBasicSettings(data) {
  return request({
    url: '/settings/basic',
    method: 'put',
    data
  })
}

// ========== 物品类型 ==========

export function getItemTypes() {
  return request({
    url: '/settings/item-types',
    method: 'get'
  })
}

export function addItemType(data) {
  return request({
    url: '/settings/item-types',
    method: 'post',
    data
  })
}

export function updateItemType(id, data) {
  return request({
    url: `/settings/item-types/${id}`,
    method: 'put',
    data
  })
}

export function deleteItemType(id) {
  return request({
    url: `/settings/item-types/${id}`,
    method: 'delete'
  })
}

// ========== 地点 ==========

export function getLocations() {
  return request({
    url: '/settings/locations',
    method: 'get'
  })
}

export function addLocation(data) {
  return request({
    url: '/settings/locations',
    method: 'post',
    data
  })
}

export function updateLocation(id, data) {
  return request({
    url: `/settings/locations/${id}`,
    method: 'put',
    data
  })
}

export function deleteLocation(id) {
  return request({
    url: `/settings/locations/${id}`,
    method: 'delete'
  })
}

// ========== 匹配算法 ==========

export function getMatchAlgorithm() {
  return request({
    url: '/settings/match-algorithm',
    method: 'get'
  })
}

export function updateMatchAlgorithm(data) {
  return request({
    url: '/settings/match-algorithm',
    method: 'put',
    data
  })
}

// ========== 系统配置（统一接口 - 组合多个接口） ==========

export async function getSystemConfig() {
  try {
    const [basicRes, matchRes] = await Promise.all([
      getBasicSettings(),
      getMatchAlgorithm()
    ])
    
    return {
      data: {
        // 基础设置
        siteName: basicRes.data.siteName || '校园失物追寻平台',
        logo: basicRes.data.logo || '',
        phone: basicRes.data.phone || '',
        email: basicRes.data.email || '',
        // 匹配算法
        imageWeight: parseFloat(matchRes.data.imageWeight) || 0.4,
        locationWeight: parseFloat(matchRes.data.locationWeight) || 0.3,
        textWeight: parseFloat(matchRes.data.textWeight) || 0.3,
        matchThreshold: parseFloat(matchRes.data.matchThreshold) || 0.6,
        // 其他设置（使用默认值）
        minReward: parseInt(matchRes.data.minReward) || 0,
        maxReward: parseInt(matchRes.data.maxReward) || 1000,
        maxImages: parseInt(matchRes.data.maxImages) || 9,
        maxImageSize: parseInt(matchRes.data.maxImageSize) || 5,
        autoCloseDay: parseInt(matchRes.data.autoCloseDay) || 30
      }
    }
  } catch (error) {
    console.error('获取系统配置失败：', error)
    // 返回默认配置
    return {
      data: {
        siteName: '校园失物追寻平台',
        logo: '',
        phone: '',
        email: '',
        imageWeight: 0.4,
        locationWeight: 0.3,
        textWeight: 0.3,
        matchThreshold: 0.6,
        minReward: 0,
        maxReward: 1000,
        maxImages: 9,
        maxImageSize: 5,
        autoCloseDay: 30
      }
    }
  }
}

export async function updateSystemConfig(data) {
  const promises = []
  
  // 更新基础设置
  if (data.siteName || data.phone || data.email) {
    const basicData = {}
    if (data.siteName) basicData.siteName = data.siteName
    if (data.phone) basicData.phone = data.phone
    if (data.email) basicData.email = data.email
    promises.push(updateBasicSettings(basicData))
  }
  
  // 更新匹配算法
  if (data.imageWeight !== undefined || data.locationWeight !== undefined || 
      data.textWeight !== undefined || data.matchThreshold !== undefined) {
    const matchData = {}
    if (data.imageWeight !== undefined) matchData.imageWeight = data.imageWeight
    if (data.locationWeight !== undefined) matchData.locationWeight = data.locationWeight
    if (data.textWeight !== undefined) matchData.textWeight = data.textWeight
    if (data.matchThreshold !== undefined) matchData.matchThreshold = data.matchThreshold
    promises.push(updateMatchAlgorithm(matchData))
  }
  
  // 其他设置（暂时也放在匹配算法接口中）
  if (data.minReward !== undefined || data.maxReward !== undefined || 
      data.maxImages !== undefined || data.maxImageSize !== undefined || 
      data.autoCloseDay !== undefined) {
    const otherData = {}
    if (data.minReward !== undefined) otherData.minReward = data.minReward
    if (data.maxReward !== undefined) otherData.maxReward = data.maxReward
    if (data.maxImages !== undefined) otherData.maxImages = data.maxImages
    if (data.maxImageSize !== undefined) otherData.maxImageSize = data.maxImageSize
    if (data.autoCloseDay !== undefined) otherData.autoCloseDay = data.autoCloseDay
    promises.push(updateMatchAlgorithm(otherData))
  }
  
  return Promise.all(promises)
}
