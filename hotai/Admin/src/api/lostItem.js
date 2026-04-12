import request from '@/utils/request'

// 获取失物列表
export function getLostItemList(params) {
  return request({
    url: '/lost-item/list',
    method: 'get',
    params
  })
}

// 获取失物详情
export function getLostItemDetail(id) {
  return request({
    url: `/lost-item/detail/${id}`,
    method: 'get'
  })
}

// 添加失物
export function addLostItem(data) {
  return request({
    url: '/lost-item/add',
    method: 'post',
    data
  })
}

// 更新失物
export function updateLostItem(data) {
  return request({
    url: '/lost-item/update',
    method: 'put',
    data
  })
}

// 删除失物
export function deleteLostItem(id) {
  return request({
    url: `/lost-item/delete/${id}`,
    method: 'delete'
  })
}

// 更新失物状态
export function updateLostItemStatus(id, status) {
  return request({
    url: `/lost-item/status/${id}`,
    method: 'put',
    params: { status }
  })
}

// 批量删除失物
export function batchDeleteLostItems(ids) {
  return request({
    url: '/lost-item/batch-delete',
    method: 'delete',
    data: ids
  })
}

// 批量审核失物
export function batchAuditLostItems(data) {
  return request({
    url: '/lost-item/batch-audit',
    method: 'post',
    data
  })
}
