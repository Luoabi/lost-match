const { post, get } = require('../utils/request')

// 用户登录
function login(data) {
  return post('/user/login', data)
}

// 用户注册
function register(data) {
  return post('/user/register', data)
}

// 获取用户信息
function getUserInfo(id) {
  return get(`/user/info/${id}`)
}

// 更新用户信息
function updateUserInfo(data) {
  return post('/user/update', data)
}

module.exports = {
  login,
  register,
  getUserInfo,
  updateUserInfo
}
