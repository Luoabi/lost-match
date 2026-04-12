const { register } = require('../../api/user.js');

Page({
  data: {
    form: {
      username: '',
      password: '',
      confirmPassword: '',
      name: '',
      phone: '',
      email: ''
    }
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`form.${field}`]: e.detail.value
    });
  },

  validateForm() {
    const { username, password, confirmPassword, name, phone } = this.data.form;

    if (!username || username.trim().length < 4) {
      wx.showToast({
        title: '用户名至少4位',
        icon: 'none'
      });
      return false;
    }

    if (!password || password.length < 6) {
      wx.showToast({
        title: '密码至少6位',
        icon: 'none'
      });
      return false;
    }

    if (password !== confirmPassword) {
      wx.showToast({
        title: '两次密码不一致',
        icon: 'none'
      });
      return false;
    }

    if (!name || !name.trim()) {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return false;
    }

    if (!phone || !phone.trim()) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return false;
    }

    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return false;
    }

    return true;
  },

  async onRegister() {
      
    if (!this.validateForm()) {
      return;
    }

    wx.showLoading({
      title: '注册中...'
    });

    try {
      const { username, password, name, phone, email } = this.data.form;
      
      // 构建注册数据
      const registerData = {
        username: username.trim(),
        password: password,  // 密码不要trim，保持原样
        realName: name.trim(),
        phone: phone.trim(),
        email: email ? email.trim() : ''
      };
      
      // 调试日志：查看发送的数据（不显示密码）
      console.log('注册数据:', {
        ...registerData,
        password: '***'
      });
      
      const res = await register(registerData);

      wx.hideLoading();
      
      wx.showModal({
        title: '注册成功',
        content: '请使用新账号登录',
        showCancel: false,
        success: () => {
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }
      });
    } catch (error) {
      wx.hideLoading();
      console.error('注册失败', error);
      wx.showToast({
        title: error.message || '注册失败',
        icon: 'none'
      });
    }
  },

  goLogin() {
    wx.redirectTo({
      url: '/pages/login/login'
    });
  }
});
