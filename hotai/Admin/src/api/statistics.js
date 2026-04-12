import request from '@/utils/request'

// 获取统计概览
export function getStatisticsOverview() {
  return request({
    url: '/statistics/overview',
    method: 'get'
  })
}

// 获取失物类型分布
export function getItemTypeDistribution() {
  return request({
    url: '/statistics/item-type',
    method: 'get'
  })
}

// 获取高频丢失区域
export function getLocationDistribution() {
  return request({
    url: '/statistics/location',
    method: 'get'
  })
}

// 获取找回率趋势
export function getRecoveryTrend(params) {
  return request({
    url: '/statistics/recovery-trend',
    method: 'get',
    params
  })
}
