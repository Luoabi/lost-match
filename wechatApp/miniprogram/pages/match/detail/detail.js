const { getMatchDetail, confirmMatch, rejectMatch, submitMatchFeedback } = require('../../../api/match.js');

const statusMap = {
  0: '待确认',
  1: '已确认',
  2: '不匹配'
};

Page({
  data: {
    detail: {},
    feedback: ''
  },

  onLoad(options) {
    if (options.id) {
      this.fetchDetail(options.id);
    }
  },

  async fetchDetail(id) {
    try {
      const res = await getMatchDetail(id);
      
      // 处理分数：从0-1转换为0-100
      const detail = {
        ...res.data,
        statusText: statusMap[res.data.status] || '未知',
        matchScore: res.data.matchScore ? (res.data.matchScore * 100).toFixed(0) : 0,
        imageScore: res.data.imageScore ? (res.data.imageScore * 100).toFixed(0) : 0,
        locationScore: res.data.locationScore ? (res.data.locationScore * 100).toFixed(0) : 0,
        textScore: res.data.textScore ? (res.data.textScore * 100).toFixed(0) : 0,
        // 格式化时间
        lostItemTime: this.formatTime(res.data.lostItemTime),
        foundItemTime: this.formatTime(res.data.foundItemTime)
      };
      
      this.setData({ detail });
    } catch (error) {
      console.error('获取匹配详情失败', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '未知';
    const date = new Date(timeStr);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hour = String(date.getHours()).padStart(2, '0');
    const minute = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  previewLostImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      current: url,
      urls: this.data.detail.lostItemImages
    });
  },

  previewFoundImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      current: url,
      urls: this.data.detail.foundItemImages
    });
  },

  async confirmMatch() {
    wx.showModal({
      title: '确认匹配',
      content: '确定这是您的失物吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            // 获取当前用户ID
            const userInfo = wx.getStorageSync('userInfo');
            if (!userInfo || !userInfo.id) {
              wx.showToast({
                title: '请先登录',
                icon: 'none'
              });
              return;
            }
            
            await confirmMatch(this.data.detail.id, userInfo.id);
            wx.showToast({
              title: '确认成功',
              icon: 'success'
            });
            this.fetchDetail(this.data.detail.id);
          } catch (error) {
            console.error('确认失败', error);
            wx.showToast({
              title: error.message || '确认失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  async rejectMatch() {
    wx.showModal({
      title: '拒绝匹配',
      content: '确定这不是您的失物吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await rejectMatch(this.data.detail.id);
            wx.showToast({
              title: '已标记为不匹配',
              icon: 'success'
            });
            this.fetchDetail(this.data.detail.id);
          } catch (error) {
            console.error('拒绝失败', error);
            wx.showToast({
              title: error.message || '操作失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  onFeedbackInput(e) {
    this.setData({ feedback: e.detail.value });
  },

  async submitFeedback() {
    if (!this.data.feedback.trim()) {
      wx.showToast({
        title: '请输入反馈内容',
        icon: 'none'
      });
      return;
    }

    try {
      await submitMatchFeedback(this.data.detail.id, this.data.feedback);
      wx.showToast({
        title: '反馈提交成功',
        icon: 'success'
      });
      this.setData({ feedback: '' });
    } catch (error) {
      console.error('提交反馈失败', error);
      wx.showToast({
        title: '提交失败',
        icon: 'none'
      });
    }
  },

  goLostDetail() {
    wx.navigateTo({
      url: `/pages/lost/detail/detail?id=${this.data.detail.lostItemId}`
    });
  },

  goFoundDetail() {
    wx.navigateTo({
      url: `/pages/found/detail/detail?id=${this.data.detail.foundItemId}`
    });
  }
});
