import { get, post, put, del } from '../utils/request'

// 获取失物列表
export function getLostItemList(params: any) {
  return get('/lost-item/list', params)
}

// 获取失物详情
export function getLostItemDetail(id: number) {
  return get(`/lost-item/detail/${id}`)
}

// 发布失物
export function addLostItem(data: any) {
  return post('/lost-item/add', data)
}

// 更新失物
export function updateLostItem(data: any) {
  return put('/lost-item/update', data)
}

// 删除失物
export function deleteLostItem(id: number) {
  return del(`/lost-item/delete/${id}`)
}

// 更新失物状态
export function updateLostItemStatus(id: number, status: number) {
  return put(`/lost-item/status/${id}`, { status })
}
