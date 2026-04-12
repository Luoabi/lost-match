import Mock from 'mockjs'

const { Random } = Mock

const itemTypes = ['手机', '钱包', '钥匙', '书籍', '雨伞', '水杯', '耳机', '充电宝', '身份证', '校园卡']
const locations = ['图书馆', '食堂', '教学楼A栋', '教学楼B栋', '宿舍楼', '操场', '体育馆', '实验楼']
const statuses = [
  { value: 0, label: '待认领' },
  { value: 1, label: '已归还' },
  { value: 2, label: '已关闭' }
]

// 生成拾获物数据
const foundItems = []
for (let i = 0; i < 100; i++) {
  const itemType = Random.pick(itemTypes)
  const status = Random.pick(statuses)
  const location = Random.pick(locations)
  
  foundItems.push({
    id: i + 1,
    title: `拾获${itemType}`,
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
    foundTime: Random.datetime(),
    contactName: Random.cname(),
    contactPhone: /^1[3-9]\d{9}/,
    status: status.value,
    statusText: status.label,
    keywords: [itemType, Random.pick(['黑色', '白色', '蓝色', '红色', '灰色'])],
    viewCount: Random.integer(0, 500),
    userId: Random.integer(1, 50),
    userName: Random.cname(),
    userAvatar: Random.image('100x100', '#67C23A', '#FFF', 'U'),
    createTime: Random.datetime(),
    updateTime: Random.datetime()
  })
}

// 获取拾获物列表
Mock.mock(RegExp('/api/found-item/list.*'), 'get', (options) => {
  const url = new URL('http://localhost' + options.url)
  const page = parseInt(url.searchParams.get('page')) || 1
  const pageSize = parseInt(url.searchParams.get('pageSize')) || 10
  const keyword = url.searchParams.get('keyword') || ''
  const status = url.searchParams.get('status')
  
  let filteredItems = foundItems
  
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

// 获取拾获物详情
Mock.mock(RegExp('/api/found-item/detail/.*'), 'get', (options) => {
  const id = parseInt(options.url.split('/').pop())
  const item = foundItems.find(item => item.id === id)
  
  return {
    code: 200,
    message: '成功',
    data: item || {}
  }
})

// 添加拾获物
Mock.mock('/api/found-item/add', 'post', {
  code: 200,
  message: '添加成功'
})

// 更新拾获物
Mock.mock('/api/found-item/update', 'put', {
  code: 200,
  message: '更新成功'
})

// 删除拾获物
Mock.mock(RegExp('/api/found-item/delete/.*'), 'delete', {
  code: 200,
  message: '删除成功'
})

// 更新拾获物状态
Mock.mock(RegExp('/api/found-item/status/.*'), 'put', {
  code: 200,
  message: '状态更新成功'
})
