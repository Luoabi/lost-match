# 校园失物追寻微信小程序

基于多模态信息感知的校园失物追寻与决策支持平台 - 微信小程序端

## 项目状态

✅ **开发完成度：100%**

所有功能页面已开发完成，可以正常运行。

## 功能特性

### 核心功能
- 🔐 用户登录认证（JWT Token）
- 📱 失物信息发布与管理
- 🎁 拾获物品发布与管理
- 🔗 智能匹配推荐（图像+位置+文本）
- 📊 个人数据统计
- 👤 个人信息管理

### 技术特点
- 📸 多图上传（最多9张）
- 🔄 下拉刷新、上拉加载
- 🔍 搜索与筛选功能
- 📍 位置选择
- 💯 匹配度评分展示
- 🎨 统一的UI设计

## 项目结构

```
wechatApp/
├── miniprogram/                # 小程序源码目录
│   ├── pages/                  # 页面目录
│   │   ├── index/             # 首页
│   │   ├── login/             # 登录页
│   │   ├── lost/              # 失物模块
│   │   │   ├── list/          # 失物列表
│   │   │   ├── detail/        # 失物详情
│   │   │   └── publish/       # 发布失物
│   │   ├── found/             # 拾获模块
│   │   │   ├── list/          # 拾获列表
│   │   │   ├── detail/        # 拾获详情
│   │   │   └── publish/       # 发布拾获
│   │   ├── match/             # 匹配模块
│   │   │   ├── list/          # 匹配列表
│   │   │   └── detail/        # 匹配详情
│   │   └── mine/              # 个人中心
│   ├── api/                   # API接口封装
│   │   ├── user.js            # 用户接口
│   │   ├── lostItem.js        # 失物接口
│   │   ├── foundItem.js       # 拾获接口
│   │   └── match.js           # 匹配接口
│   ├── utils/                 # 工具类
│   │   └── request.js         # HTTP请求封装
│   ├── config/                # 配置文件
│   │   └── index.js           # 全局配置
│   ├── app.js                 # 小程序入口
│   ├── app.json               # 小程序配置
│   └── app.wxss               # 全局样式
├── QUICK_START.md             # 快速开始指南
├── IMPLEMENTATION_SUMMARY.md  # 实现总结
├── COMPLETION_SUMMARY.md      # 完成总结
└── README.md                  # 项目说明
```

## 快速开始

### 1. 环境准备

- 安装[微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)
- 确保后端服务已启动（SpringBoot）

### 2. 导入项目

1. 打开微信开发者工具
2. 选择"导入项目"
3. 选择 `wechatApp` 目录
4. 填写AppID（可使用测试号）

### 3. 配置开发环境

在微信开发者工具中：
1. 点击右上角"详情"
2. 勾选"不校验合法域名、web-view（业务域名）、TLS 版本以及 HTTPS 证书"
3. 勾选"启用ES6转ES5"
4. 勾选"增强编译"

### 4. 修改API地址

编辑 `miniprogram/config/index.js`：

```javascript
export const config = {
  // 修改为你的后端地址
  apiBaseUrl: 'http://localhost:8080/api',
  uploadUrl: 'http://localhost:8080/api/file/upload',
}
```

### 5. 编译运行

点击"编译"按钮，小程序即可运行。

### 6. 测试账号

- 用户名：`admin`
- 密码：`123456`

## 页面说明

### 首页（pages/index）
- 统计数据展示（失物、拾获、匹配、找回率）
- 快捷操作入口
- 最新失物列表
- 最新拾获列表

### 登录页（pages/login）
- 用户名密码登录
- 表单验证
- Token保存

### 失物模块（pages/lost）

#### 失物列表（list）
- 分页加载
- 下拉刷新、上拉加载更多
- 类型和状态筛选
- 搜索功能
- 悬赏显示

#### 失物详情（detail）
- 图片轮播
- 完整信息展示
- 匹配推荐
- 联系方式
- 编辑/删除（所有者）
- 标记为已找回

#### 发布失物（publish）
- 多图上传（最多9张）
- 类型选择
- 位置选择
- 时间选择
- 悬赏设置
- 表单验证

### 拾获模块（pages/found）
功能同失物模块，无悬赏功能。

### 匹配模块（pages/match）

#### 匹配列表（list）
- 匹配记录展示
- 匹配得分显示
- 状态筛选
- 失物与拾获对比

#### 匹配详情（detail）
- 综合匹配度展示
- 图像/位置/文本得分
- 失物信息展示
- 拾获信息展示
- 确认/拒绝匹配
- 提交反馈

### 个人中心（pages/mine）
- 用户信息展示
- 统计数据
- 编辑个人信息
- 我的失物
- 我的拾获
- 匹配记录
- 退出登录

## API接口

### 用户接口（api/user.js）
- `login(username, password)` - 用户登录
- `getUserInfo()` - 获取用户信息
- `updateUserInfo(data)` - 更新用户信息

### 失物接口（api/lostItem.js）
- `getLostItemList(params)` - 获取失物列表
- `getLostItemDetail(id)` - 获取失物详情
- `addLostItem(data)` - 添加失物
- `updateLostItem(id, data)` - 更新失物
- `deleteLostItem(id)` - 删除失物
- `updateLostItemStatus(id, status)` - 更新失物状态

### 拾获接口（api/foundItem.js）
功能同失物接口。

### 匹配接口（api/match.js）
- `getMatchList(params)` - 获取匹配列表
- `getMatchDetail(id)` - 获取匹配详情
- `getMatchesByLostItem(lostItemId)` - 根据失物获取匹配
- `getMatchesByFoundItem(foundItemId)` - 根据拾获获取匹配
- `confirmMatch(id)` - 确认匹配
- `rejectMatch(id)` - 拒绝匹配
- `submitMatchFeedback(id, feedback)` - 提交反馈

## 核心功能实现

### 1. 多模态信息采集

#### 图像采集
```javascript
// 选择图片
wx.chooseImage({
  count: 9,
  success: async (res) => {
    // 上传图片
    const urls = await Promise.all(
      res.tempFilePaths.map(path => uploadImage(path))
    )
  }
})
```

#### 位置采集
```javascript
// 使用picker选择预设地点
<picker mode="selector" range="{{locationOptions}}">
  <view>{{location || '请选择地点'}}</view>
</picker>
```

#### 文本采集
```javascript
// 标题、描述、关键词
form: {
  title: '',
  description: '',
  keywords: ''
}
```

### 2. 智能匹配展示

```javascript
// 匹配数据结构
{
  matchScore: 85,      // 综合匹配度
  imageScore: 90,      // 图像得分（权重0.4）
  locationScore: 80,   // 位置得分（权重0.3）
  textScore: 85        // 文本得分（权重0.3）
}
```

### 3. 用户认证

```javascript
// 登录后保存token
wx.setStorageSync('token', token)

// 请求时自动携带token
header: {
  'Authorization': `Bearer ${token}`
}

// token过期自动跳转登录
if (response.code === 401) {
  wx.redirectTo({ url: '/pages/login/login' })
}
```

## 样式规范

### 颜色
- 主色调：`#4A90E2`
- 成功色：`#67C23A`
- 警告色：`#E6A23C`
- 危险色：`#F56C6C`
- 信息色：`#909399`

### 间距
- xs: `10rpx`
- sm: `20rpx`
- md: `30rpx`
- lg: `40rpx`
- xl: `60rpx`

### 圆角
- sm: `8rpx`
- md: `12rpx`
- lg: `20rpx`

## 注意事项

### 1. 开发环境
- 需要微信开发者工具
- 需要勾选"不校验合法域名"
- 需要后端服务运行

### 2. 图片上传
- 最多上传9张图片
- 自动压缩图片
- 需要后端支持文件上传接口

### 3. 数据格式
- 日期格式：`YYYY-MM-DD`
- 手机号格式：11位数字
- 图片URL：完整的HTTP地址

### 4. 权限控制
- 只有所有者可以编辑/删除
- 只有所有者可以更新状态
- 所有用户可以查看详情

## 常见问题

### 1. 请求失败
- 检查后端服务是否启动
- 检查API地址配置是否正确
- 检查是否勾选"不校验合法域名"

### 2. 图片上传失败
- 检查uploadUrl配置
- 检查后端文件上传接口
- 检查图片大小是否超限

### 3. 页面跳转失败
- 检查页面路径是否正确
- 检查app.json中是否注册页面
- tabBar页面使用switchTab，普通页面使用navigateTo

## 后续优化建议

### 1. 图片资源
- 添加TabBar图标
- 添加功能图标
- 添加Logo
- 添加默认图片

### 2. 功能增强
- 添加地图选点
- 添加消息通知
- 添加收藏功能
- 添加分享功能

### 3. 性能优化
- 图片懒加载
- 虚拟列表
- 缓存策略
- 预加载

### 4. 用户体验
- 骨架屏
- 动画效果
- 手势操作
- 语音输入

## 文档

- [快速开始指南](./QUICK_START.md)
- [实现总结](./IMPLEMENTATION_SUMMARY.md)
- [完成总结](./COMPLETION_SUMMARY.md)

## 技术支持

如有问题，请查看文档或联系开发团队。

## 许可证

本项目仅用于学习和研究目的。
