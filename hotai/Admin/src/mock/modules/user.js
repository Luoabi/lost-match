import Mock from 'mockjs'

const { Random } = Mock

// 模拟用户数据
const users = []
const roleMap = {
  student: '学生',
  teacher: '教师',
  admin: '管理员',
  cleaner: '保洁人员'
}

for (let i = 0; i < 50; i++) {
  const role = Random.pick(['student', 'teacher', 'admin', 'cleaner'])
  users.push({
    id: i + 1,
    username: Random.name(),
    realName: Random.cname(),
    phone: /^1[3-9]\d{9}/,
    email: Random.email(),
    avatar: Random.image('100x100', Random.color(), '#FFF', 'U'),
    role: role,
    roleText: roleMap[role],
    studentId: role === 'student' ? `2024${Random.integer(100000, 999999)}` : null,
    department: Random.pick(['计算机学院', '经济学院', '文学院', '理学院', '工学院']),
    status: Random.pick([0, 1]),
    lostCount: Random.integer(0, 10),
    foundCount: Random.integer(0, 10),
    matchSuccessCount: Random.integer(0, 8),
    creditScore: Random.integer(80, 100),
    lastLoginTime: Random.datetime(),
    createTime: Random.datetime()
  })
}

// 登录
Mock.mock('/api/user/login', 'post', (options) => {
  const { username, password } = JSON.parse(options.body)
  
  if (username === 'admin' && password === '123456') {
    return {
      code: 200,
      message: '登录成功',
      data: {
        token: Random.guid()
      }
    }
  }
  
  return {
    code: 400,
    message: '用户名或密码错误'
  }
})

// 获取用户信息
Mock.mock('/api/user/info', 'get', {
  code: 200,
  message: '成功',
  data: {
    userInfo: {
      id: 1,
      username: 'admin',
      realName: '管理员',
      phone: '13800138000',
      email: 'admin@example.com',
      avatar: Random.image('100x100', '#409EFF', '#FFF', 'Admin'),
      department: '信息中心',
      lostCount: 5,
      foundCount: 8,
      matchSuccessCount: 6,
      creditScore: 100
    },
    roles: ['admin']
  }
})

// 获取用户列表
Mock.mock(RegExp('/api/user/list.*'), 'get', (options) => {
  const url = new URL('http://localhost' + options.url)
  const page = parseInt(url.searchParams.get('page')) || 1
  const pageSize = parseInt(url.searchParams.get('pageSize')) || 10
  const keyword = url.searchParams.get('keyword') || ''
  
  let filteredUsers = users
  if (keyword) {
    filteredUsers = users.filter(u => 
      u.username.includes(keyword) || u.phone.includes(keyword)
    )
  }
  
  const start = (page - 1) * pageSize
  const end = start + pageSize
  
  return {
    code: 200,
    message: '成功',
    data: {
      list: filteredUsers.slice(start, end),
      total: filteredUsers.length
    }
  }
})

// 添加用户
Mock.mock('/api/user/add', 'post', {
  code: 200,
  message: '添加成功'
})

// 更新用户
Mock.mock('/api/user/update', 'put', {
  code: 200,
  message: '更新成功'
})

// 删除用户
Mock.mock(RegExp('/api/user/delete/.*'), 'delete', {
  code: 200,
  message: '删除成功'
})
