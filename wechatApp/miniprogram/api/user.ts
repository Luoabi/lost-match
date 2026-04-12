import { post, get } from '../utils/request'

// 用户登录
export function login(data: { username: string; password: string }) {
  return post('/user/login', data)
}

// 用户注册
export function register(data: {
  username: string;
  password: string;
  realName: string;
  phone: string;
  email?: string;
}) {
  return post('/user/register', data)
}

// 获取用户信息
export function getUserInfo(id: number) {
  return get(`/user/info/${id}`)
}

// 更新用户信息
export function updateUserInfo(data: any) {
  return post('/user/update', data)
}
