import request from '@/utils/request'

// 获取匹配记录列表
export function getMatchList(params) {
  return request({
    url: '/match/list',
    method: 'get',
    params
  })
}

// 获取匹配详情
export function getMatchDetail(id) {
  return request({
    url: `/match/detail/${id}`,
    method: 'get'
  })
}

// 手动匹配
export function manualMatch(data) {
  return request({
    url: '/match/manual',
    method: 'post',
    data
  })
}

// 确认匹配
export function confirmMatch(id, userId) {
  return request({
    url: `/match/confirm/${id}`,
    method: 'put',
    params: { userId }
  })
}

// 拒绝匹配
export function rejectMatch(id) {
  return request({
    url: `/match/reject/${id}`,
    method: 'put'
  })
}

// 提交反馈
export function submitFeedback(data) {
  return request({
    url: '/match/feedback',
    method: 'post',
    data
  })
}
