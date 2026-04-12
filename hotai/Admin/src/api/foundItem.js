import request from '@/utils/request'

// 获取拾获物列表
export function getFoundItemList(params) {
  return request({
    url: '/found-item/list',
    method: 'get',
    params
  })
}

// 获取拾获物详情
export function getFoundItemDetail(id) {
  return request({
    url: `/found-item/detail/${id}`,
    method: 'get'
  })
}

// 添加拾获物
export function addFoundItem(data) {
  return request({
    url: '/found-item/add',
    method: 'post',
    data
  })
}

// 更新拾获物
export function updateFoundItem(data) {
  return request({
    url: '/found-item/update',
    method: 'put',
    data
  })
}

// 删除拾获物
export function deleteFoundItem(id) {
  return request({
    url: `/found-item/delete/${id}`,
    method: 'delete'
  })
}

// 更新拾获物状态
export function updateFoundItemStatus(id, status) {
  return request({
    url: `/found-item/status/${id}`,
    method: 'put',
    params: { status }
  })
}

// 批量删除拾获物
export function batchDeleteFoundItems(ids) {
  return request({
    url: '/found-item/batch-delete',
    method: 'delete',
    data: ids
  })
}

// 批量审核拾获物
export function batchAuditFoundItems(data) {
  return request({
    url: '/found-item/batch-audit',
    method: 'post',
    data
  })
}
