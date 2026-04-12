// 配置文件
const config = {
  // API基础地址
  apiBaseUrl: 'http://localhost:8080/api',
  
  // 图片上传地址
  uploadUrl: 'http://localhost:8080/api/file/upload',
  
  // 默认头像
  defaultAvatar: '/images/default-avatar.png',
  
  // 分页配置
  pageSize: 10,
  
  // 图片上传限制
  maxImageCount: 9,
  maxImageSize: 5 * 1024 * 1024, // 5MB
}

// 物品类型
const itemTypes = [
  '手机', '钱包', '钥匙', '书籍', '雨伞', 
  '水杯', '耳机', '充电宝', '身份证', '校园卡', '其他'
]

// 地点列表
const locations = [
  '图书馆', '食堂', '教学楼A栋', '教学楼B栋',
  '宿舍楼', '操场', '体育馆', '实验楼', '其他'
]

// 状态映射
const statusMap = {
  lost: {
    0: '未找回',
    1: '已找回',
    2: '已关闭'
  },
  found: {
    0: '待认领',
    1: '已归还',
    2: '已关闭'
  }
}

module.exports = {
  config,
  itemTypes,
  locations,
  statusMap
}
