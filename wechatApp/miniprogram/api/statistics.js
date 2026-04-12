const { get } = require('../utils/request')

// 获取统计概览
function getStatisticsOverview() {
  return get('/statistics/overview')
}

// 获取失物类型分布
function getItemTypeDistribution() {
  return get('/statistics/item-type')
}

// 获取高频丢失区域
function getLocationDistribution() {
  return get('/statistics/location')
}

// 获取找回率趋势
function getRecoveryTrend(params) {
  return get('/statistics/recovery-trend', params)
}

module.exports = {
  getStatisticsOverview,
  getItemTypeDistribution,
  getLocationDistribution,
  getRecoveryTrend
}
