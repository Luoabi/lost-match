import Mock from 'mockjs'

const { Random } = Mock

// 生成匹配记录
const matchRecords = []
for (let i = 0; i < 50; i++) {
  matchRecords.push({
    id: i + 1,
    lostItemId: Random.integer(1, 100),
    lostItemTitle: `丢失${Random.pick(['手机', '钱包', '钥匙', '书籍'])}`,
    foundItemId: Random.integer(1, 100),
    foundItemTitle: `拾获${Random.pick(['手机', '钱包', '钥匙', '书籍'])}`,
    matchScore: Random.float(0.5, 1, 2, 2),
    imageScore: Random.float(0.3, 1, 2, 2),
    locationScore: Random.float(0.3, 1, 2, 2),
    textScore: Random.float(0.3, 1, 2, 2),
    status: Random.pick([0, 1, 2]),
    statusText: Random.pick(['待确认', '已确认', '不匹配']),
    createTime: Random.datetime()
  })
}

// 获取匹配记录列表
Mock.mock(RegExp('/api/match/list.*'), 'get', (options) => {
  const url = new URL('http://localhost' + options.url)
  const page = parseInt(url.searchParams.get('page')) || 1
  const pageSize = parseInt(url.searchParams.get('pageSize')) || 10
  
  const start = (page - 1) * pageSize
  const end = start + pageSize
  
  return {
    code: 200,
    message: '成功',
    data: {
      list: matchRecords.slice(start, end),
      total: matchRecords.length
    }
  }
})

// 获取匹配详情
Mock.mock(RegExp('/api/match/detail/.*'), 'get', (options) => {
  const id = parseInt(options.url.split('/').pop())
  const record = matchRecords.find(r => r.id === id)
  
  return {
    code: 200,
    message: '成功',
    data: record || {}
  }
})

// 手动匹配
Mock.mock('/api/match/manual', 'post', {
  code: 200,
  message: '匹配成功',
  data: {
    matchCount: Random.integer(1, 10)
  }
})
