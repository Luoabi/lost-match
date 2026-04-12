# 校园失物追寻平台 - 后台管理系统

基于 Vue3 + Element Plus + Pinia + Vue Router + Mock.js 构建的后台管理系统

## 技术栈

- **框架**: Vue 3 (Composition API)
- **UI组件库**: Element Plus
- **状态管理**: Pinia (支持持久化)
- **路由管理**: Vue Router 4
- **HTTP请求**: Axios
- **Mock数据**: Mock.js
- **构建工具**: Vite
- **进度条**: NProgress

## 项目结构

```
src/
├── api/                  # API接口
│   ├── user.js          # 用户相关接口
│   ├── lostItem.js      # 失物相关接口
│   ├── foundItem.js     # 拾获相关接口
│   ├── match.js         # 匹配相关接口
│   └── statistics.js    # 统计相关接口
├── assets/              # 静态资源
├── components/          # 公共组件
├── layout/              # 布局组件
│   ├── components/      # 布局子组件
│   │   ├── Navbar.vue   # 顶部导航
│   │   └── Sidebar.vue  # 侧边栏
│   └── index.vue        # 布局主文件
├── mock/                # Mock数据
│   ├── modules/         # 模块化Mock
│   └── index.js         # Mock入口
├── router/              # 路由配置
│   └── index.js
├── store/               # 状态管理
│   ├── modules/         # 模块化Store
│   │   ├── user.js      # 用户状态
│   │   └── app.js       # 应用状态
│   └── index.js         # Store入口
├── styles/              # 全局样式
│   └── index.css
├── utils/               # 工具函数
│   └── request.js       # Axios封装
├── views/               # 页面组件
│   ├── dashboard/       # 首页
│   ├── login/           # 登录页
│   ├── lost-items/      # 失物管理
│   ├── found-items/     # 拾获管理
│   ├── match-records/   # 匹配记录
│   ├── users/           # 用户管理
│   ├── statistics/      # 数据统计
│   ├── settings/        # 系统设置
│   └── error/           # 错误页面
├── App.vue              # 根组件
└── main.js              # 入口文件
```

## 功能模块

### 1. 用户认证
- 登录/登出
- Token管理
- 权限控制

### 2. 失物管理
- 失物列表查询
- 失物详情查看
- 失物状态管理
- 失物删除

### 3. 拾获管理
- 拾获物列表查询
- 拾获物详情查看
- 拾获物状态管理
- 拾获物删除

### 4. 匹配记录
- 匹配记录列表
- 多维度匹配得分展示
- 匹配详情查看

### 5. 用户管理
- 用户列表查询
- 用户信息编辑
- 用户删除

### 6. 数据统计
- 数据概览
- 失物类型分布
- 高频丢失区域
- 找回率趋势

## 开发指南

### 安装依赖
```bash
npm install
```

### 启动开发服务器
```bash
npm run dev
```

### 构建生产版本
```bash
npm run build
```

### 预览生产构建
```bash
npm run preview
```

## 默认账号

- 用户名: `admin`
- 密码: `123456`

## Mock数据说明

开发环境下自动启用Mock数据，无需后端接口即可运行。

当后端接口开发完成后，只需：
1. 修改 `.env.production` 中的 `VITE_API_BASE_URL`
2. 在 `src/main.js` 中移除Mock导入即可切换到真实接口

## 接口规范

所有接口返回格式：
```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

## 注意事项

1. 所有API请求都会自动携带Token（通过请求拦截器）
2. Token存储在localStorage中，刷新页面不会丢失
3. 路由守卫会自动检查登录状态
4. 响应拦截器会统一处理错误信息

## 后续扩展

- [ ] 图片上传组件
- [ ] 富文本编辑器
- [ ] 数据导出功能
- [ ] 更多图表展示
- [ ] 消息通知功能
- [ ] 操作日志记录
