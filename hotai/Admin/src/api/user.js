import request from '@/utils/request'

// 登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo(id) {
  return request({
    url: `/user/info/${id}`,
    method: 'get'
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/user/delete/${id}`,
    method: 'delete'
  })
}

// 批量删除用户
export function batchDeleteUsers(ids) {
  return request({
    url: '/user/batch-delete',
    method: 'delete',
    data: ids
  })
}

// 更新个人信息
export function updateUserProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/user/change-password',
    method: 'put',
    data
  })
}
