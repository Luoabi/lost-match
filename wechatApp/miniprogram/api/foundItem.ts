import { get, post, put, del } from '../utils/request'

// 获取拾获列表
export function getFoundItemList(params: any) {
  return get('/found-item/list', params)
}

// 获取拾获详情
export function getFoundItemDetail(id: number) {
  return get(`/found-item/detail/${id}`)
}

// 发布拾获
export function addFoundItem(data: any) {
  return post('/found-item/add', data)
}

// 更新拾获
export function updateFoundItem(data: any) {
  return put('/found-item/update', data)
}

// 删除拾获
export function deleteFoundItem(id: number) {
  return del(`/found-item/delete/${id}`)
}

// 更新拾获状态
export function updateFoundItemStatus(id: number, status: number) {
  return put(`/found-item/status/${id}`, { status })
}
