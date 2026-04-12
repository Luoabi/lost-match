import Mock from 'mockjs'
import './modules/user'
import './modules/lostItem'
import './modules/foundItem'
import './modules/match'
import './modules/statistics'

// 配置Mock
Mock.setup({
  timeout: '200-600'
})

console.log('Mock数据已启用')
