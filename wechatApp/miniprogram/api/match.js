const { get, post, put } = require('../utils/request')

// 获取匹配列表
function getMatchList(params) {
  return get('/match/list', params)
}

// 获取匹配详情
function getMatchDetail(id) {
  return get(`/match/detail/${id}`)
}

// 确认匹配
function confirmMatch(id, userId) {
  return put(`/match/confirm/${id}`, { userId })
}

// 拒绝匹配
function rejectMatch(id) {
  return put(`/match/reject/${id}`)
}

// 提交反馈
function submitFeedback(data) {
  return post('/match/feedback', data)
}

module.exports = {
  getMatchList,
  getMatchDetail,
  confirmMatch,
  rejectMatch,
  submitFeedback
}
