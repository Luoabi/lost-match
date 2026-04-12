const { get, post, put, del } = require('../utils/request')

// 获取拾获列表
function getFoundItemList(params) {
  return get('/found-item/list', params)
}

// 获取拾获详情
function getFoundItemDetail(id) {
  return get(`/found-item/detail/${id}`)
}

// 发布拾获
function addFoundItem(data) {
  return post('/found-item/add', data)
}

// 更新拾获
function updateFoundItem(data) {
  return put('/found-item/update', data)
}

// 删除拾获
function deleteFoundItem(id) {
  return del(`/found-item/delete/${id}`)
}

// 更新拾获状态
function updateFoundItemStatus(id, status) {
  return put(`/found-item/status/${id}`, { status })
}

module.exports = {
  getFoundItemList,
  getFoundItemDetail,
  addFoundItem,
  updateFoundItem,
  deleteFoundItem,
  updateFoundItemStatus
}
