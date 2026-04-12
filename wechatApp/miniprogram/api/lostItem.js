const { get, post, put, del } = require('../utils/request')

// 获取失物列表
function getLostItemList(params) {
  return get('/lost-item/list', params)
}

// 获取失物详情
function getLostItemDetail(id) {
  return get(`/lost-item/detail/${id}`)
}

// 发布失物
function addLostItem(data) {
  return post('/lost-item/add', data)
}

// 更新失物
function updateLostItem(data) {
  return put('/lost-item/update', data)
}

// 删除失物
function deleteLostItem(id) {
  return del(`/lost-item/delete/${id}`)
}

// 更新失物状态
function updateLostItemStatus(id, status) {
  return put(`/lost-item/status/${id}`, { status })
}

module.exports = {
  getLostItemList,
  getLostItemDetail,
  addLostItem,
  updateLostItem,
  deleteLostItem,
  updateLostItemStatus
}
