const { getLostItemList } = require('../../../api/lostItem.js');
const { getFoundItemList } = require('../../../api/foundItem.js');

const lostStatusMap = {
  0: '待审核',
  1: '已发布',
  2: '已找回',
  3: '已关闭'
};

const foundStatusMap = {
  0: '待审核',
  1: '已发布',
  2: '已归还',
  3: '已关闭'
};

Page({
  data: {
    activeTab: 'lost', // 'lost' 或 'found'
    list: [],
    page: 1,
    pageSize: 10,
    hasMore: true,
    loading: false,
    searchKeyword: '',
    filterType: '',
    filterStatus: '',
    isMine: false,
    typeOptions: ['证件', '电子产品', '书籍', '钥匙', '钱包', '衣物', '其他'],
    statusOptions: [
      { label: '全部', value: '' },
      { label: '待审核', value: 0 },
      { label: '已发布', value: 1 },
      { label: '已找回/归还', value: 2 },
      { label: '已关闭', value: 3 }
    ]
  },

  onLoad(options) {
    console.log('=== items/list onLoad ===');
    console.log('URL参数:', options);
    
    // 检查是否是"我的"模式
    if (options.mine === '1') {
      this.setData({ isMine: true });
      wx.setNavigationBarTitle({
        title: options.tab === 'found' ? '我的拾获' : '我的失物'
      });
    }
    
    // 设置初始tab
    if (options.tab) {
      this.setData({ activeTab: options.tab });
    }
    
    this.fetchList();
  },

  onShow() {
    console.log('=== items/list onShow ===');
    
    // 检查是否有mineMode标记（从"我的"页面跳转过来）
    const mineMode = wx.getStorageSync('mineMode');
    console.log('mineMode标记:', mineMode);
    
    if (mineMode && mineMode.mine) {
      console.log('检测到mineMode，切换到我的模式');
      this.setData({ 
        isMine: true,
        activeTab: mineMode.tab || 'lost',
        page: 1,
        list: []
      });
      
      // 更新导航栏标题
      wx.setNavigationBarTitle({
        title: mineMode.tab === 'found' ? '我的拾获' : '我的失物'
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

  // 切换Tab
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    if (tab === this.data.activeTab) return;
    
    console.log('切换Tab到:', tab);
    
    // 更新导航栏标题
    if (this.data.isMine) {
      wx.setNavigationBarTitle({
        title: tab === 'found' ? '我的拾获' : '我的失物'
      });
    }
    
    this.setData({
      activeTab: tab,
      page: 1,
      list: [],
      searchKeyword: '',
      filterType: '',
      filterStatus: ''
    });
    this.fetchList();
  },

  async fetchList() {
    if (this.data.loading) return;

    this.setData({ loading: true });

    try {
      const params = {
        page: this.data.page,
        pageSize: this.data.pageSize
      };

      // 如果是"我的"模式，添加用户ID筛选
      if (this.data.isMine) {
        const userInfo = wx.getStorageSync('userInfo');
        console.log('=== 我的失物/拾获列表 ===');
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

      if (this.data.searchKeyword) {
        params.keyword = this.data.searchKeyword;
      }
      if (this.data.filterType) {
        params.type = this.data.filterType;
      }
      if (this.data.filterStatus !== '') {
        params.status = this.data.filterStatus;
      }

      console.log('请求参数:', params);
      console.log('当前Tab:', this.data.activeTab);

      // 根据activeTab调用不同的API
      const apiFunc = this.data.activeTab === 'lost' ? getLostItemList : getFoundItemList;
      const statusMap = this.data.activeTab === 'lost' ? lostStatusMap : foundStatusMap;
      
      const res = await apiFunc(params);
      console.log('API响应:', res);
      console.log('列表数据:', res.data.list);
      console.log('总数:', res.data.total);
      
      const newList = res.data.list.map(item => ({
        ...item,
        statusText: statusMap[item.status] || '未知'
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
            title: this.data.activeTab === 'lost' ? '暂无失物记录' : '暂无拾获记录',
            icon: 'none'
          });
        } else {
          console.log(`成功加载 ${newList.length} 条记录`);
        }
      }

      wx.stopPullDownRefresh();
    } catch (error) {
      console.error('获取列表失败', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
      this.setData({ loading: false });
      wx.stopPullDownRefresh();
    }
  },

  onRefresh() {
    this.setData({ page: 1, list: [] });
    this.fetchList();
  },

  onSearchInput(e) {
    this.setData({ searchKeyword: e.detail.value });
    clearTimeout(this.searchTimer);
    this.searchTimer = setTimeout(() => {
      this.onRefresh();
    }, 500);
  },

  selectType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({ filterType: type });
    this.onRefresh();
  },

  selectStatus(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({ filterStatus: status });
    this.onRefresh();
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    const path = this.data.activeTab === 'lost' 
      ? `/pages/lost/detail/detail?id=${id}`
      : `/pages/found/detail/detail?id=${id}`;
    wx.navigateTo({ url: path });
  },

  goPublish() {
    const path = this.data.activeTab === 'lost'
      ? '/pages/lost/publish/publish'
      : '/pages/found/publish/publish';
    wx.navigateTo({ url: path });
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
      title: '失物拾获'
    });
    
    // 重新加载数据
    this.fetchList();
  }
});
