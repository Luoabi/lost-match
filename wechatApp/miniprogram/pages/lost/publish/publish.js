const { addLostItem, updateLostItem, getLostItemDetail } = require('../../../api/lostItem.js');
const { uploadImage } = require('../../../utils/request.js');
const { getImageUrl } = require('../../../utils/image.js');

Page({
  data: {
    isEdit: false,
    itemId: null,
    form: {
      title: '',
      type: '',
      description: '',
      images: [],
      location: '',
      locationDetail: '',
      longitude: null,
      latitude: null,
      lostTime: '',
      contactName: '',
      contactPhone: '',
      reward: '',
      keywords: ''
    },
    typeOptions: ['证件', '电子产品', '书籍', '钥匙', '钱包', '衣物', '其他'],
    locationOptions: ['图书馆', '食堂', '教学楼A栋', '教学楼B栋', '宿舍区', '操场', '校门口', '其他']
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ isEdit: true, itemId: options.id });
      this.loadDetail(options.id);
    }
    
    // 设置默认联系信息
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo && !options.id) {
      this.setData({
        'form.contactName': userInfo.name || '',
        'form.contactPhone': userInfo.phone || ''
      });
    }
  },

  async loadDetail(id) {
    try {
      const res = await getLostItemDetail(id);
      const item = res.data;
      
      // 处理关键词：数组转字符串
      let keywords = '';
      if (item.keywords && Array.isArray(item.keywords)) {
        keywords = item.keywords.join(' ');
      }
      
      // 处理时间：ISO 格式转日期字符串
      let lostTime = item.lostTime;
      if (lostTime && lostTime.includes('T')) {
        lostTime = lostTime.split('T')[0];
      }
      
      // 处理图片：将相对路径转换为完整 URL
      let images = item.images || [];
      if (images.length > 0) {
        images = images.map(url => getImageUrl(url));
      }
      
      this.setData({ 
        form: {
          ...item,
          keywords: keywords,
          lostTime: lostTime,
          images: images
        }
      });
    } catch (error) {
      console.error('加载失物详情失败', error);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({
      [`form.${field}`]: e.detail.value
    });
  },

  onTypeChange(e) {
    this.setData({
      'form.type': this.data.typeOptions[e.detail.value]
    });
  },

  onLocationChange(e) {
    this.setData({
      'form.location': this.data.locationOptions[e.detail.value]
    });
  },

  onDateChange(e) {
    this.setData({
      'form.lostTime': e.detail.value
    });
  },

  // 选择地图位置
  chooseLocation() {
    wx.chooseLocation({
      success: (res) => {
        console.log('选择的位置:', res);
        this.setData({
          'form.locationDetail': res.address + res.name,
          'form.longitude': res.longitude,
          'form.latitude': res.latitude
        });
        wx.showToast({
          title: '位置已选择',
          icon: 'success'
        });
      },
      fail: (err) => {
        console.error('选择位置失败:', err);
        if (err.errMsg.includes('auth deny')) {
          wx.showModal({
            title: '提示',
            content: '需要授权位置信息才能选择地点',
            success: (res) => {
              if (res.confirm) {
                wx.openSetting();
              }
            }
          });
        }
      }
    });
  },

  async chooseImage() {
    const maxCount = 9 - this.data.form.images.length;
    if (maxCount <= 0) {
      wx.showToast({
        title: '最多上传9张图片',
        icon: 'none'
      });
      return;
    }

    wx.chooseMedia({
      count: maxCount,
      mediaType: ['image'],
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: async (res) => {
        // 从 tempFiles 中提取图片路径
        const tempFilePaths = res.tempFiles.map(file => file.tempFilePath);
        console.log('选择的图片路径:', tempFilePaths);
        wx.showLoading({ title: '上传中...' });

        try {
          const uploadPromises = tempFilePaths.map((path, index) => {
            console.log(`开始上传第 ${index + 1} 张图片:`, path);
            return uploadImage(path)
              .then(url => {
                console.log(`第 ${index + 1} 张图片上传成功（相对路径）:`, url);
                // 将相对路径转换为完整 URL
                const fullUrl = getImageUrl(url);
                console.log(`第 ${index + 1} 张图片完整 URL:`, fullUrl);
                return fullUrl;
              })
              .catch(err => {
                console.error(`第 ${index + 1} 张图片上传失败:`, err);
                return null;
              });
          });
          
          const urls = await Promise.all(uploadPromises);

          console.log('所有上传结果:', urls);
          
          // 过滤掉失败的（null）
          const successUrls = urls.filter(url => url != null);
          console.log('成功上传的URL:', successUrls);

          this.setData({
            'form.images': [...this.data.form.images, ...successUrls]
          });

          console.log('当前图片数组:', this.data.form.images);

          wx.hideLoading();
          
          if (successUrls.length > 0) {
            wx.showToast({
              title: `成功上传${successUrls.length}张`,
              icon: 'success'
            });
          } else {
            wx.showToast({
              title: '上传失败',
              icon: 'none'
            });
          }
        } catch (error) {
          wx.hideLoading();
          wx.showToast({
            title: '上传失败',
            icon: 'none'
          });
          console.error('图片上传失败', error);
        }
      }
    });
  },

  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.form.images;
    images.splice(index, 1);
    this.setData({
      'form.images': images
    });
  },

  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      current: url,
      urls: this.data.form.images
    });
  },

  validateForm() {
    const { title, type, description, location, lostTime, contactName, contactPhone } = this.data.form;

    if (!title || !title.trim()) {
      wx.showToast({ title: '请输入标题', icon: 'none' });
      return false;
    }

    if (!type) {
      wx.showToast({ title: '请选择物品类型', icon: 'none' });
      return false;
    }

    if (!description || !description.trim()) {
      wx.showToast({ title: '请输入描述', icon: 'none' });
      return false;
    }

    if (!location) {
      wx.showToast({ title: '请选择丢失地点', icon: 'none' });
      return false;
    }

    if (!lostTime) {
      wx.showToast({ title: '请选择丢失时间', icon: 'none' });
      return false;
    }

    if (!contactName || !contactName.trim()) {
      wx.showToast({ title: '请输入联系人', icon: 'none' });
      return false;
    }

    if (!contactPhone || !contactPhone.trim()) {
      wx.showToast({ title: '请输入联系电话', icon: 'none' });
      return false;
    }

    const phoneReg = /^1[3-9]\d{9}$/;
    if (!phoneReg.test(contactPhone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' });
      return false;
    }

    return true;
  },

  async onSubmit() {
    if (!this.validateForm()) {
      return;
    }

    wx.showLoading({ title: this.data.isEdit ? '更新中...' : '发布中...' });

    try {
      // 获取用户信息
      const userInfo = wx.getStorageSync('userInfo');
      if (!userInfo || !userInfo.id) {
        wx.hideLoading();
        wx.showToast({
          title: '请先登录',
          icon: 'none'
        });
        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }, 1500);
        return;
      }

      // 处理关键词：字符串转数组
      let keywords = [];
      if (this.data.form.keywords && this.data.form.keywords.trim()) {
        keywords = this.data.form.keywords.trim().split(/\s+/);
      }

      // 处理时间：日期字符串转 ISO 格式
      let lostTime = this.data.form.lostTime;
      if (lostTime && !lostTime.includes('T')) {
        // 如果是日期格式 YYYY-MM-DD，转换为 ISO 格式
        lostTime = lostTime + 'T00:00:00';
      }

      // 确保 images 是数组且过滤掉 null/undefined
      let images = this.data.form.images || [];
      images = images.filter(img => img != null && img !== '');

      const formData = {
        ...this.data.form,
        userId: userInfo.id,
        reward: parseInt(this.data.form.reward) || 0,
        keywords: keywords,
        lostTime: lostTime,
        images: images,  // 使用过滤后的图片数组
        locationDetail: this.data.form.locationDetail || '',
        longitude: this.data.form.longitude,
        latitude: this.data.form.latitude
      };

      // 调试日志
      console.log('提交的表单数据:', {
        ...formData,
        images: formData.images,
        imagesLength: formData.images.length
      });

      if (this.data.isEdit) {
        formData.id = this.data.itemId;
        await updateLostItem(formData);
        wx.showToast({
          title: '更新成功',
          icon: 'success'
        });
      } else {
        await addLostItem(formData);
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        });
      }

      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || (this.data.isEdit ? '更新失败' : '发布失败'),
        icon: 'none'
      });
      console.error('提交失败', error);
    }
  }
});
