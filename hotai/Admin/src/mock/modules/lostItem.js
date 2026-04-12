import Mock from 'mockjs'

const { Random } = Mock

// 物品类型
const itemTypes = ['手机', '钱包', '钥匙', '书籍', '雨伞', '水杯', '耳机', '充电宝', '身份证', '校园卡']
const locations = ['图书馆', '食堂', '教学楼A栋', '教学楼B栋', '宿舍楼', '操场', '体育馆', '实验楼']
const statuses = [
  { value: 0, label: '未找回' },
  { value: 1, label: '已找回' },
  { value: 2, label: '已关闭' }
]

// 生成失物数据
const lostItems = []
for (let i = 0; i < 100; i++) {
  const itemType = Random.pick(itemTypes)
  const status = Random.pick(statuses)
  const location = Random.pick(locations)
  
  lostItems.push({
    id: i + 1,
    title: `丢失${itemType}`,
    type: itemType,
    description: Random.cparagraph(1, 2),
    images: [
      Random.image('400x300', Random.color(), '#FFF', itemType),
      Random.image('400x300', Random.color(), '#FFF', itemType)
    ],
    location: location,
    locationDetail: `${location}${Random.integer(1, 5)}楼${Random.integer(101, 505)}室`,
    longitude: Random.float(116.3, 116.5, 6, 6),
    latitude: Random.float(39.9, 40.0, 6, 6),
    lostTime: Random.datetime(),
    contactName: Random.cname(),
    contactPhone: /^1[3-9]\d{9}/,
    status: status.value,
    statusText: status.label,
    keywords: [itemType, Random.pick(['黑色', '白色', '蓝色', '红色', '灰色'])],
    reward: Random.pick([0, 50, 100, 200]),
    viewCount: Random.integer(0, 500),
    userId: Random.integer(1, 50),
    userName: Random.cname(),
    userAvatar: Random.image('100x100', '#409EFF', '#FFF', 'U'),
    createTime: Random.datetime(),
    updateTime: Random.datetime()
  })
}

// 获取失物列表
Mock.mock(RegExp('/api/lost-item/list.*'), 'get', (options) => {
  const url = new URL('http://localhost' + options.url)
  const page = parseInt(url.searchParams.get('page')) || 1
  const pageSize = parseInt(url.searchParams.get('pageSize')) || 10
  const keyword = url.searchParams.get('keyword') || ''
  const status = url.searchParams.get('status')
  
  let filteredItems = lostItems
  
  if (keyword) {
    filteredItems = filteredItems.filter(item => 
      item.title.includes(keyword) || 
      item.description.includes(keyword) ||
      item.type.includes(keyword)
    )
  }
  
  if (status !== null && status !== undefined && status !== '') {
    filteredItems = filteredItems.filter(item => item.status === parseInt(status))
  }
  
  const start = (page - 1) * pageSize
  const end = start + pageSize
  
  return {
    code: 200,
    message: '成功',
    data: {
      list: filteredItems.slice(start, end),
      total: filteredItems.length
    }
  }
})

// 获取失物详情
Mock.mock(RegExp('/api/lost-item/detail/.*'), 'get', (options) => {
  const id = parseInt(options.url.split('/').pop())
  const item = lostItems.find(item => item.id === id)
  
  return {
    code: 200,
    message: '成功',
    data: item || {}
  }
})

// 添加失物
Mock.mock('/api/lost-item/add', 'post', {
  code: 200,
  message: '添加成功'
})

// 更新失物
Mock.mock('/api/lost-item/update', 'put', {
  code: 200,
  message: '更新成功'
})

// 删除失物
Mock.mock(RegExp('/api/lost-item/delete/.*'), 'delete', {
  code: 200,
  message: '删除成功'
})

// 更新失物状态
Mock.mock(RegExp('/api/lost-item/status/.*'), 'put', {
  code: 200,
  message: '状态更新成功'
})
