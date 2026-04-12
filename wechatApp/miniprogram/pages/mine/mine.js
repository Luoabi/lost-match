const { getUserInfo, updateUserInfo } = require('../../api/user.js');
const { getLostItemList } = require('../../api/lostItem.js');
const { getFoundItemList } = require('../../api/foundItem.js');
const { getMatchList } = require('../../api/match.js');

Page({
  data: {
    userInfo: {},
    stats: {
      lostCount: 0,
      foundCount: 0,
      matchCount: 0
    },
    showEditModal: false,
    editForm: {
      realName: '',
      phone: '',
      email: '',
      studentId: '',
      department: ''
    }
  },

  onLoad() {
    this.loadUserInfo();
    this.loadStats();
  },

  onShow() {
    // 从其他页面返回时刷新数据
    this.loadStats();
  },

  async loadUserInfo() {
    try {
      const userInfo = wx.getStorageSync('userInfo');
      if (userInfo) {
        this.setData({ userInfo });
      }

      // 确保有用户 ID 才调用接口
      if (userInfo && userInfo.id) {
        const res = await getUserInfo(userInfo.id);
        this.setData({ userInfo: res.data });
        wx.setStorageSync('userInfo', res.data);
      }
    } catch (error) {
      console.error('获取用户信息失败', error);
    }
  },

  async loadStats() {
    try {
      // 获取用户ID
      const userInfo = wx.getStorageSync('userInfo');
      if (!userInfo || !userInfo.id) {
        console.log('用户信息不存在，无法加载统计数据');
        return;
      }

      console.log('开始加载用户统计数据，用户ID:', userInfo.id);

      // 并行请求三个接口
      const [lostRes, foundRes, matchRes] = await Promise.all([
        getLostItemList({ 
          page: 1, 
          pageSize: 1, 
          userId: userInfo.id 
        }).catch(err => {
          console.error('获取失物统计失败:', err);
          return { data: { total: 0 } };
        }),
        getFoundItemList({ 
          page: 1, 
          pageSize: 1, 
          userId: userInfo.id 
        }).catch(err => {
          console.error('获取拾获统计失败:', err);
          return { data: { total: 0 } };
        }),
        getMatchList({ 
          page: 1, 
          pageSize: 1, 
          userId: userInfo.id 
        }).catch(err => {
          console.error('获取匹配统计失败:', err);
          return { data: { total: 0 } };
        })
      ]);

      console.log('失物数量:', lostRes.data.total);
      console.log('拾获数量:', foundRes.data.total);
      console.log('匹配数量:', matchRes.data.total);

      this.setData({
        'stats.lostCount': lostRes.data.total || 0,
        'stats.foundCount': foundRes.data.total || 0,
        'stats.matchCount': matchRes.data.total || 0
      });
    } catch (error) {
      console.error('获取统计数据失败', error);
      // 失败时设置为0
      this.setData({
        'stats.lostCount': 0,
        'stats.foundCount': 0,
        'stats.matchCount': 0
      });
    }
  },

  showEdit() {
    this.setData({
      showEditModal: true,
      editForm: {
        realName: this.data.userInfo.realName || '',
        phone: this.data.userInfo.phone || '',
        email: this.data.userInfo.email || '',
        studentId: this.data.userInfo.studentId || '',
        department: this.data.userInfo.department || ''
      }
    });
  },

  hideEdit() {
    this.setData({ showEditModal: false });
  },

  stopPropagation() {},

  onEditInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`editForm.${field}`]: e.detail.value
    });
  },

  async saveEdit() {
    const { realName, phone, email, studentId, department } = this.data.editForm;

    // 验证必填项
    if (!realName || !realName.trim()) {
      wx.showToast({
        title: '请输入真实姓名',
        icon: 'none'
      });
      return;
    }

    if (!phone || !phone.trim()) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return;
    }

    // 验证手机号格式
    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }

    // 验证邮箱格式（如果填写了）
    if (email && email.trim()) {
      const emailReg = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailReg.test(email)) {
        wx.showToast({
          title: '请输入正确的邮箱',
          icon: 'none'
        });
        return;
      }
    }

    try {
      wx.showLoading({ title: '保存中...' });
      
      await updateUserInfo({
        id: this.data.userInfo.id,
        realName: realName.trim(),
        phone: phone.trim(),
        email: email.trim() || null,
        studentId: studentId.trim() || null,
        department: department.trim() || null
      });

      wx.hideLoading();
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      });
      
      this.hideEdit();
      this.loadUserInfo();
    } catch (error) {
      wx.hideLoading();
      console.error('保存失败', error);
      wx.showToast({
        title: error.message || '保存失败',
        icon: 'none'
      });
    }
  },

  goMyLost() {
    console.log('跳转到我的失物');
    // 因为items/list在tabBar中，需要先switchTab再传参
    // 但switchTab不支持传参，所以使用全局变量或storage
    wx.setStorageSync('mineMode', { mine: true, tab: 'lost' });
    wx.switchTab({
      url: '/pages/items/list/list',
      success: () => {
        console.log('跳转成功');
      },
      fail: (err) => {
        console.error('跳转失败:', err);
      }
    });
  },

  goMyFound() {
    console.log('跳转到我的拾获');
    wx.setStorageSync('mineMode', { mine: true, tab: 'found' });
    wx.switchTab({
      url: '/pages/items/list/list',
      success: () => {
        console.log('跳转成功');
      },
      fail: (err) => {
        console.error('跳转失败:', err);
      }
    });
  },

  goMyMatch() {
    console.log('跳转到匹配记录');
    wx.setStorageSync('mineMode', { mine: true });
    wx.switchTab({
      url: '/pages/match/list/list',
      success: () => {
        console.log('跳转成功');
      },
      fail: (err) => {
        console.error('跳转失败:', err);
      }
    });
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      confirmColor: '#F56C6C',
      success: (res) => {
        if (res.confirm) {
          wx.clearStorageSync();
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});
