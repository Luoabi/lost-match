const { getFoundItemDetail, updateFoundItemStatus, deleteFoundItem } = require('../../../api/foundItem.js');
const { getMatchesByFoundItem } = require('../../../api/match.js');

const statusMap = {
  0: '待审核',
  1: '已发布',
  2: '已归还',
  3: '已关闭'
};

Page({
  data: {
    detail: {},
    matches: [],
    isOwner: false
  },

  onLoad(options) {
    if (options.id) {
      this.fetchDetail(options.id);
      this.fetchMatches(options.id);
    }
  },

  async fetchDetail(id) {
    try {
      const res = await getFoundItemDetail(id);
      const userInfo = wx.getStorageSync('userInfo');
      
      this.setData({
        detail: {
          ...res.data,
          statusText: statusMap[res.data.status] || '未知'
        },
        isOwner: userInfo && userInfo.id === res.data.userId
      });
    } catch (error) {
      console.error('获取拾获详情失败', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  async fetchMatches(foundItemId) {
    try {
      const res = await getMatchesByFoundItem(foundItemId);
      this.setData({
        matches: res.data.list.slice(0, 3)
      });
    } catch (error) {
      console.error('获取匹配推荐失败', error);
    }
  },

  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      current: url,
      urls: this.data.detail.images
    });
  },

  makeCall() {
    wx.makePhoneCall({
      phoneNumber: this.data.detail.contactPhone
    });
  },

  async markAsReturned() {
    wx.showModal({
      title: '确认',
      content: '确定标记为已归还吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await updateFoundItemStatus(this.data.detail.id, 2);
            wx.showToast({
              title: '已标记为归还',
              icon: 'success'
            });
            this.fetchDetail(this.data.detail.id);
          } catch (error) {
            console.error('更新状态失败', error);
          }
        }
      }
    });
  },

  editItem() {
    wx.navigateTo({
      url: `/pages/found/publish/publish?id=${this.data.detail.id}`
    });
  },

  deleteItem() {
    wx.showModal({
      title: '确认删除',
      content: '删除后无法恢复，确定要删除吗？',
      confirmColor: '#F56C6C',
      success: async (res) => {
        if (res.confirm) {
          try {
            await deleteFoundItem(this.data.detail.id);
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            setTimeout(() => {
              wx.navigateBack();
            }, 1500);
          } catch (error) {
            console.error('删除失败', error);
          }
        }
      }
    });
  },

  goMatchDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/match/detail/detail?id=${id}`
    });
  }
});
