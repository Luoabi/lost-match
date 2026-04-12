const { getLostItemList } = require('../../../api/lostItem.js');
const { getImageUrl } = require('../../../utils/image.js');

const statusMap = {
  0: '待审核',
  1: '已发布',
  2: '已找回',
  3: '已关闭'
};

Page({
  data: {
    list: [],
    page: 1,
    pageSize: 10,
    hasMore: true,
    loading: false,
    searchKeyword: '',
    filterType: '',
    filterStatus: '',
    filterStatusText: '',
    showFilterModal: false,
    isMine: false, // 是否只显示我的
    typeOptions: ['全部', '证件', '电子产品', '书籍', '钥匙', '钱包', '衣物', '其他'],
    statusOptions: [
      { label: '全部', value: '' },
      { label: '待审核', value: 0 },
      { label: '已发布', value: 1 },
      { label: '已找回', value: 2 },
      { label: '已关闭', value: 3 }
    ]
  },

  onLoad(options) {
    // 检查是否是"我的失物"模式
    if (options.mine === '1') {
      this.setData({ isMine: true });
      wx.setNavigationBarTitle({
        title: '我的失物'
      });
    }
    this.fetchList();
  },

  onShow() {
    // 从发布页返回时刷新列表
    if (this.data.list.length > 0) {
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

      // 如果是"我的失物"模式，添加用户ID筛选
      if (this.data.isMine) {
        const userInfo = wx.getStorageSync('userInfo');
        if (userInfo && userInfo.id) {
          params.userId = userInfo.id;
        }
      }

      if (this.data.searchKeyword) {
        params.keyword = this.data.searchKeyword;
      }
      if (this.data.filterType && this.data.filterType !== '全部') {
        params.type = this.data.filterType;
      }
      if (this.data.filterStatus !== '') {
        params.status = this.data.filterStatus;
      }

      const res = await getLostItemList(params);
      
      const newList = res.data.list.map(item => ({
        ...item,
        statusText: statusMap[item.status] || '未知',
        // 处理图片 URL，将相对路径转换为完整 URL
        images: item.images && item.images.length > 0 
          ? item.images.map(url => getImageUrl(url))
          : []
      }));

      this.setData({
        list: this.data.page === 1 ? newList : [...this.data.list, ...newList],
        hasMore: newList.length === this.data.pageSize,
        loading: false
      });

      wx.stopPullDownRefresh();
    } catch (error) {
      console.error('获取失物列表失败', error);
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

  showFilter() {
    this.setData({ showFilterModal: true });
  },

  hideFilter() {
    this.setData({ showFilterModal: false });
  },

  stopPropagation() {},

  selectType(e) {
    this.setData({ filterType: e.currentTarget.dataset.type });
  },

  selectStatus(e) {
    const status = e.currentTarget.dataset.status;
    const statusText = this.data.statusOptions.find(item => item.value === status)?.label || '';
    this.setData({ 
      filterStatus: status,
      filterStatusText: statusText
    });
  },

  clearTypeFilter() {
    this.setData({ filterType: '' });
    this.onRefresh();
  },

  clearStatusFilter() {
    this.setData({ filterStatus: '', filterStatusText: '' });
    this.onRefresh();
  },

  resetFilter() {
    this.setData({ 
      filterType: '',
      filterStatus: '',
      filterStatusText: ''
    });
  },

  confirmFilter() {
    this.hideFilter();
    this.onRefresh();
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/lost/detail/detail?id=${id}`
    });
  },

  goPublish() {
    wx.navigateTo({
      url: '/pages/lost/publish/publish'
    });
  }
});
