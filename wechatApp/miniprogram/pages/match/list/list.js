const { getMatchList } = require('../../../api/match.js');

const statusMap = {
  0: '待确认',
  1: '已确认',
  2: '不匹配'
};

Page({
  data: {
    list: [],
    page: 1,
    pageSize: 10,
    hasMore: true,
    loading: false,
    filterStatus: '',
    showFilterModal: false,
    isMine: false, // 是否只显示我的
    statusOptions: [
      { label: '全部', value: '' },
      { label: '待确认', value: 0 },
      { label: '已确认', value: 1 },
      { label: '不匹配', value: 2 }
    ]
  },

  onLoad(options) {
    console.log('=== match/list onLoad ===');
    console.log('URL参数:', options);
    
    // 检查是否是"我的匹配"模式
    if (options.mine === '1') {
      this.setData({ isMine: true });
      wx.setNavigationBarTitle({
        title: '我的匹配'
      });
    }
    this.fetchList();
  },

  onShow() {
    console.log('=== match/list onShow ===');
    
    // 检查是否有mineMode标记（从"我的"页面跳转过来）
    const mineMode = wx.getStorageSync('mineMode');
    console.log('mineMode标记:', mineMode);
    
    if (mineMode && mineMode.mine) {
      console.log('检测到mineMode，切换到我的模式');
      this.setData({ 
        isMine: true,
        page: 1,
        list: []
      });
      
      // 更新导航栏标题
      wx.setNavigationBarTitle({
        title: '我的匹配'
      });
      
      // 清除标记
      wx.removeStorageSync('mineMode');
      
      // 重新加载数据
      this.fetchList();
    } else if (this.data.list.length > 0 && this.data.isMine) {
      // 如果已经在我的模式，刷新数据
      this.onRefresh();
    } else if (this.data.list.length > 0 && !this.data.isMine) {
      // 普通模式，刷新数据
      this.onRefresh();
    }
  },

  onPullDownRefresh() {
    this.onRefresh();
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.setData({ page: this.data.page + 1 });
      this.fetchList();
    }
  },

  async fetchList() {
    if (this.data.loading) return;

    this.setData({ loading: true });

    try {
      const params = {
        page: this.data.page,
        pageSize: this.data.pageSize
      };

      // 如果是"我的匹配"模式，添加用户ID筛选
      if (this.data.isMine) {
        const userInfo = wx.getStorageSync('userInfo');
        console.log('=== 我的匹配记录 ===');
        console.log('用户信息:', userInfo);
        if (userInfo && userInfo.id) {
          params.userId = userInfo.id;
          console.log('添加userId筛选:', userInfo.id);
        } else {
          console.error('用户信息不存在，无法筛选');
          wx.showToast({
            title: '请先登录',
            icon: 'none'
          });
          this.setData({ loading: false });
          return;
        }
      }

      if (this.data.filterStatus !== '') {
        params.status = this.data.filterStatus;
      }

      console.log('请求参数:', params);

      const res = await getMatchList(params);
      console.log('API响应:', res);
      console.log('匹配列表:', res.data.list);
      console.log('总数:', res.data.total);
      
      const newList = res.data.list.map(item => ({
        ...item,
        statusText: statusMap[item.status] || '未知',
        // 格式化匹配分数（从0-1转换为0-100）
        matchScore: item.matchScore ? (item.matchScore * 100).toFixed(0) : 0,
        imageScore: item.imageScore ? (item.imageScore * 100).toFixed(0) : 0,
        locationScore: item.locationScore ? (item.locationScore * 100).toFixed(0) : 0,
        textScore: item.textScore ? (item.textScore * 100).toFixed(0) : 0,
        // 格式化时间
        matchTime: item.createTime ? this.formatTime(item.createTime) : '未知'
      }));

      console.log('处理后的列表:', newList);

      this.setData({
        list: this.data.page === 1 ? newList : [...this.data.list, ...newList],
        hasMore: newList.length === this.data.pageSize,
        loading: false
      });

      // 显示提示信息
      if (this.data.isMine && this.data.page === 1) {
        if (newList.length === 0) {
          wx.showToast({
            title: '暂无匹配记录',
            icon: 'none'
          });
        } else {
          console.log(`成功加载 ${newList.length} 条匹配记录`);
        }
      }

      wx.stopPullDownRefresh();
    } catch (error) {
      console.error('获取匹配列表失败', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      this.setData({ loading: false });
      wx.stopPullDownRefresh();
    }
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '未知';
    const date = new Date(timeStr);
    const now = new Date();
    const diff = now - date;
    
    // 小于1分钟
    if (diff < 60000) {
      return '刚刚';
    }
    // 小于1小时
    if (diff < 3600000) {
      return Math.floor(diff / 60000) + '分钟前';
    }
    // 小于1天
    if (diff < 86400000) {
      return Math.floor(diff / 3600000) + '小时前';
    }
    // 小于7天
    if (diff < 604800000) {
      return Math.floor(diff / 86400000) + '天前';
    }
    // 格式化为日期
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  },

  onRefresh() {
    this.setData({ page: 1, list: [] });
    this.fetchList();
  },

  showFilter() {
    this.setData({ showFilterModal: true });
  },

  hideFilter() {
    this.setData({ showFilterModal: false });
  },

  stopPropagation() {},

  selectStatus(e) {
    this.setData({ filterStatus: e.currentTarget.dataset.status });
  },

  resetFilter() {
    this.setData({ filterStatus: '' });
  },

  confirmFilter() {
    this.hideFilter();
    this.onRefresh();
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/match/detail/detail?id=${id}`
    });
  },

  // 退出我的模式
  exitMineMode() {
    console.log('退出我的模式');
    this.setData({ 
      isMine: false,
      page: 1,
      list: []
    });
    
    // 恢复默认标题
    wx.setNavigationBarTitle({
      title: '匹配'
    });
    
    // 重新加载数据
    this.fetchList();
  }
});
