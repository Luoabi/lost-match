import { get, post, put } from '../utils/request'

// 获取匹配列表
export function getMatchList(params: any) {
  return get('/match/list', params)
}

// 获取匹配详情
export function getMatchDetail(id: number) {
  return get(`/match/detail/${id}`)
}

// 确认匹配
export function confirmMatch(id: number, userId: number) {
  return put(`/match/confirm/${id}`, { userId })
}

// 拒绝匹配
export function rejectMatch(id: number) {
  return put(`/match/reject/${id}`)
}

// 提交反馈
export function submitFeedback(data: any) {
  return post('/match/feedback', data)
}
