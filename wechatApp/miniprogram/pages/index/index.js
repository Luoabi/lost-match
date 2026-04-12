const { getLostItemList } = require('../../api/lostItem')
const { getFoundItemList } = require('../../api/foundItem')
const { getStatisticsOverview } = require('../../api/statistics')

Page({
  data: {
    stats: {
      totalLost: 0,
      totalFound: 0,
      totalMatched: 0,
      recoveryRate: 0
    },
    lostList: [],
    foundList: [],
    refreshing: false
  },

  onLoad() {
    this.checkLogin()
    this.loadData()
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.loadData()
  },

  // 下拉刷新
  onRefresh() {
    this.setData({ refreshing: true })
    this.loadData().finally(() => {
      this.setData({ refreshing: false })
    })
  },

  // 加载所有数据
  async loadData() {
    await Promise.all([
      this.fetchStats(),
      this.fetchLostList(),
      this.fetchFoundList()
    ])
  },

  // 检查登录状态
  checkLogin() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }
  },

  // 获取统计数据
  async fetchStats() {
    try {
      const res = await getStatisticsOverview()
      this.setData({
        stats: {
          totalLost: res.data.totalLost || 0,
          totalFound: res.data.totalFound || 0,
          totalMatched: res.data.totalMatched || 0,
          recoveryRate: res.data.recoveryRate || 0
        }
      })
    } catch (error) {
      console.error('获取统计数据失败', error)
      // 失败时使用默认值
      this.setData({
        stats: {
          totalLost: 0,
          totalFound: 0,
          totalMatched: 0,
          recoveryRate: 0
        }
      })
    }
  },

  // 获取最新失物列表
  async fetchLostList() {
    try {
      const res = await getLostItemList({
        page: 1,
        pageSize: 10, // 增加显示数量
        auditStatus: 1, // 只显示已审核通过的
        sortBy: 'createTime', // 按创建时间排序
        sortOrder: 'desc' // 降序，最新的在前面
      })
      console.log('失物列表:', res.data)
      this.setData({
        lostList: res.data.list || []
      })
    } catch (error) {
      console.error('获取失物列表失败', error)
    }
  },

  // 获取最新拾获列表
  async fetchFoundList() {
    try {
      const res = await getFoundItemList({
        page: 1,
        pageSize: 10, // 增加显示数量
        auditStatus: 1, // 只显示已审核通过的
        sortBy: 'createTime', // 按创建时间排序
        sortOrder: 'desc' // 降序，最新的在前面
      })
      console.log('拾获列表:', res.data)
      this.setData({
        foundList: res.data.list || []
      })
    } catch (error) {
      console.error('获取拾获列表失败', error)
    }
  },

  // 发布失物
  onPublishLost() {
    wx.navigateTo({
      url: '/pages/lost/publish/publish'
    })
  },

  // 发布拾获
  onPublishFound() {
    wx.navigateTo({
      url: '/pages/found/publish/publish'
    })
  },

  // 查看匹配
  onViewMatch() {
    wx.switchTab({
      url: '/pages/match/list/list'
    })
  },

  // 我的信息
  onViewMine() {
    wx.switchTab({
      url: '/pages/mine/mine'
    })
  },

  // 查看更多失物
  onViewMoreLost() {
    wx.switchTab({
      url: '/pages/items/list/list'
    })
  },

  // 查看更多拾获
  onViewMoreFound() {
    wx.switchTab({
      url: '/pages/items/list/list'
    })
  },

  // 查看失物详情
  onViewLostDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/lost/detail/detail?id=${id}`
    })
  },

  // 查看拾获详情
  onViewFoundDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/found/detail/detail?id=${id}`
    })
  }
})
