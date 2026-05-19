# 校园失物追寻平台

一个集微信小程序、管理后台和Spring Boot后端于一体的智能校园失物招领系统，支持多维度智能匹配算法，帮助快速找回失物。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术架构](#技术架构)
- [功能特性](#功能特性)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [核心业务流程](#核心业务流程)
- [API文档](#api文档)
- [数据库设计](#数据库设计)
- [开发指南](#开发指南)
- [截图展示](#截图展示)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

---

## 项目简介

校园失物追寻平台是一套完整的失物招领解决方案，采用**微信小程序**作为用户端，**Vue 3 + Element Plus**作为管理后台，**Spring Boot 3.2 + MySQL**作为后端服务。系统核心亮点是**智能多维度匹配算法**，支持基于类型、位置、时间、关键词的综合匹配，有效提升失物找回效率。

### 项目亮点

- ✨ **智能匹配算法**：综合类型、位置、时间、关键词四维匹配
- 📱 **微信小程序**：便捷的移动端用户体验
- 🖥️ **管理后台**：完善的审核、统计、用户管理功能
- 🔐 **JWT认证**：安全的用户权限管理
- 📍 **地图定位**：支持经纬度精确定位和距离计算
- 📊 **数据统计**：丰富的数据分析和可视化展示

---

## 技术架构

### 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 3.2.0 |
| **ORM** | Spring Data JPA | - |
| **数据库** | MySQL | 8.0+ |
| **认证** | JWT (jjwt) | 0.11.5 |
| **工具库** | Hutool | 5.8.23 |
| **API文档** | SpringDoc OpenAPI | 2.2.0 |
| **Java版本** | JDK | 17 |
| **前端框架** | Vue | 3.5.32 |
| **UI组件库** | Element Plus | 2.13.6 |
| **状态管理** | Pinia | 3.0.4 |
| **构建工具** | Vite | 8.0.4 |
| **小程序** | 微信小程序原生 | - |

### 系统架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                            用户层                                │
│  ┌─────────────────┐              ┌─────────────────┐          │
│  │  微信小程序     │              │   管理后台      │          │
│  │  (用户端)       │              │   (Vue 3)      │          │
│  └────────┬────────┘              └────────┬────────┘          │
└───────────┼────────────────────────────────┼───────────────────┘
            │                                │
            │ HTTP/REST API                  │
            │                                │
┌───────────▼────────────────────────────────▼───────────────────┐
│                          后端服务层                              │
│                 Spring Boot + JPA + MySQL                       │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  Controller层: 接口入口                                  │  │
│  │  - UserController                                       │  │
│  │  - LostItemController                                   │  │
│  │  - FoundItemController                                  │  │
│  │  - MatchController                                      │  │
│  │  - AuditController                                      │  │
│  └───────────────────────┬─────────────────────────────────┘  │
│  ┌───────────────────────▼─────────────────────────────────┐  │
│  │  Service层: 业务逻辑                                    │  │
│  │  - UserService                                          │  │
│  │  - LostItemService (发布+自动匹配触发)                  │  │
│  │  - FoundItemService (发布+自动匹配触发)                 │  │
│  │  - MatchService (智能匹配核心算法)                      │  │
│  │  - AuditService                                         │  │
│  └───────────────────────┬─────────────────────────────────┘  │
│  ┌───────────────────────▼─────────────────────────────────┐  │
│  │  Repository层: 数据访问 (JPA)                           │  │
│  └─────────────────────────────────────────────────────────┘  │
└──────────────────────────────┬──────────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────────┐
│                         MySQL 数据库                             │
│  ┌────────────┐  ┌────────────┐  ┌───────────────┐             │
│  │lost_items  │  │found_items │  │match_records  │             │
│  └────────────┘  └────────────┘  └───────────────┘             │
└─────────────────────────────────────────────────────────────────┘
```

---

## 功能特性

### 微信小程序端
- 用户注册/登录
- 发布失物信息（标题、类型、描述、图片、位置、时间）
- 发布拾获信息
- 浏览失物/拾获列表
- 查看匹配记录
- 匹配确认/拒绝
- 个人中心（我的发布、我的匹配）
- 地图定位选择地点

### 管理后台
- 用户管理
- 失物审核管理
- 拾获审核管理
- 匹配记录管理
- 数据统计（发布数、匹配成功率等）
- 系统配置
- 操作日志

### 后端服务
- 智能匹配算法（类型、位置、时间、关键词）
- JWT认证授权
- 文件上传
- 分页查询
- 批量审核
- 数据统计

---

## 项目结构

```
zzkBS/
├── hotai/                              # 管理后台
│   └── Admin/                          # Vue 3 管理后台
│       ├── src/
│       │   ├── api/                    # API接口
│       │   ├── views/                  # 页面组件
│       │   ├── router/                 # 路由配置
│       │   ├── store/                  # Pinia状态管理
│       │   └── layout/                 # 布局组件
│       ├── package.json
│       └── vite.config.js
│
├── hotaihoduan/                        # 后端服务
│   └── BRApi/                          # Spring Boot后端
│       ├── src/
│       │   └── main/
│       │       ├── java/
│       │       │   └── org/xingchang/brapi/
│       │       │       ├── controller/ # 控制器层
│       │       │       ├── service/    # 业务逻辑层
│       │       │       ├── repository/ # 数据访问层
│       │       │       ├── entity/     # 实体类
│       │       │       ├── config/     # 配置类
│       │       │       └── util/       # 工具类
│       │       └── resources/
│       │           ├── application.yml # 配置文件
│       │           └── schema.sql      # 数据库初始化脚本
│       └── pom.xml
│
└── wechatApp/                          # 微信小程序
    └── miniprogram/
        ├── pages/                      # 页面
        ├── api/                        # API封装
        ├── utils/                      # 工具函数
        └── app.json
```

---

## 快速开始

### 环境要求

- **JDK**: 17+
- **Maven**: 3.6+
- **MySQL**: 8.0+
- **Node.js**: 16+
- **微信开发者工具**: 最新版本

### 1. 克隆项目

```bash
git clone https://github.com/yourusername/campus-lost-found.git
cd campus-lost-found
```

### 2. 数据库配置

创建数据库并执行初始化脚本：

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source hotaihoduan/BRApi/src/main/resources/schema.sql
```

或者手动创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS campus_lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 后端启动

修改配置文件 `hotaihoduan/BRApi/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_lost_found?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root      # 修改为你的数据库用户名
    password: root      # 修改为你的数据库密码
```

使用Maven启动：

```bash
cd hotaihoduan/BRApi
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080/api` 启动，API文档地址：`http://localhost:8080/api/swagger-ui.html`

### 4. 管理后台启动

```bash
cd hotai/Admin
npm install
npm run dev
```

管理后台将在 `http://localhost:5173` 启动

默认管理员账号：
- 用户名: `admin`
- 密码: `123456`

### 5. 微信小程序配置

1. 使用微信开发者工具打开 `wechatApp` 目录
2. 修改 `project.config.json` 中的 `appid` 为你的小程序AppID
3. 修改API基础地址（位于 `wechatApp/miniprogram/config/index.js` 或相应配置文件中）
4. 点击编译运行

---

## 核心业务流程

### 物品发布与智能匹配

```
用户发布失物/拾获信息
        ↓
   保存到数据库
        ↓
自动触发智能匹配算法
        ↓
遍历所有状态为"未找回"/"未归还"的记录
        ↓
计算四维匹配得分：
  - 类型匹配 (30%)
  - 位置匹配 (30%)
  - 时间匹配 (20%)
  - 关键词匹配 (20%)
        ↓
  综合得分 ≥ 60%
        ↓
创建匹配记录，状态为"待确认"
        ↓
   用户查看匹配记录
        ↓
   确认匹配 / 拒绝匹配
        ↓
确认匹配：
  - 失物状态 = "已找回"
  - 拾获状态 = "已归还"
  - 匹配状态 = "已确认"
```

### 智能匹配算法详解

**匹配维度权重**：

| 维度 | 权重 | 算法说明 |
|------|------|----------|
| **类型匹配** | 30% | 物品类型完全一致得1.0分，否则0分 |
| **位置匹配** | 30% | 1. 大致位置相同得1.0分<br>2. 使用Haversine公式计算经纬度距离<br>3. 500米内满分，2000米外0分 |
| **时间匹配** | 20% | 拾获时间在丢失时间之后<br>24小时内满分，7天外0分 |
| **关键词匹配** | 20% | Jaccard相似度（交集/并集） |

**Haversine距离计算**（Java实现）：

```java
private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371000; // 地球半径（米）
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return R * c;
}
```

**关键词Jaccard相似度**：

```java
private double calculateKeywordScore(List<String> keywords1, List<String> keywords2) {
    if (keywords1 == null || keywords2 == null || keywords1.isEmpty() || keywords2.isEmpty()) {
        return 0.5;
    }
    Set<String> set1 = new HashSet<>(keywords1);
    Set<String> set2 = new HashSet<>(keywords2);
    
    Set<String> intersection = new HashSet<>(set1);
    intersection.retainAll(set2);
    
    Set<String> union = new HashSet<>(set1);
    union.addAll(set2);
    
    return union.size() > 0 ? (double) intersection.size() / union.size() : 0.0;
}
```

---

## API文档

启动后端服务后，访问 Swagger UI 查看完整API文档：

```
http://localhost:8080/api/swagger-ui.html
```

### 主要接口

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| **用户** | POST | `/user/login` | 用户登录 |
| | GET | `/user/info` | 获取用户信息 |
| **失物** | GET | `/lost-item/list` | 失物列表 |
| | POST | `/lost-item/add` | 发布失物 |
| | PUT | `/lost-item/update` | 更新失物 |
| | DELETE | `/lost-item/delete/{id}` | 删除失物 |
| **拾获** | GET | `/found-item/list` | 拾获列表 |
| | POST | `/found-item/add` | 发布拾获 |
| **匹配** | GET | `/match/list` | 匹配记录列表 |
| | POST | `/match/confirm/{id}` | 确认匹配 |
| | POST | `/match/reject/{id}` | 拒绝匹配 |
| **审核** | POST | `/lost-item/batch-audit` | 批量审核失物 |
| | POST | `/found-item/batch-audit` | 批量审核拾获 |

### 响应格式

**成功响应**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {},
  "timestamp": 1705305600000
}
```

**分页响应**：
```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "list": [],
    "total": 100,
    "page": 1,
    "pageSize": 10,
    "totalPages": 10
  },
  "timestamp": 1705305600000
}
```

---

## 数据库设计

### 核心数据表

#### users（用户表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(255) | 密码 |
| real_name | VARCHAR(50) | 真实姓名 |
| phone | VARCHAR(20) | 手机号 |
| role | VARCHAR(20) | 角色 |
| credit_score | INT | 信用分 |
| create_time | DATETIME | 创建时间 |

#### lost_items（失物表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| title | VARCHAR(100) | 标题 |
| type | VARCHAR(50) | 物品类型 |
| description | TEXT | 描述 |
| images | JSON | 图片数组 |
| location | VARCHAR(100) | 丢失位置 |
| longitude | DECIMAL(10,6) | 经度 |
| latitude | DECIMAL(10,6) | 纬度 |
| lost_time | DATETIME | 丢失时间 |
| status | TINYINT | 状态(0未找回/1已找回) |
| audit_status | TINYINT | 审核状态 |

#### found_items（拾获表）
结构同失物表，`lost_time` 改为 `found_time`

#### match_records（匹配记录表）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| lost_item_id | BIGINT | 失物ID |
| found_item_id | BIGINT | 拾获物ID |
| match_score | DECIMAL(3,2) | 总匹配得分 |
| location_score | DECIMAL(3,2) | 位置匹配度 |
| text_score | DECIMAL(3,2) | 文本匹配度 |
| image_score | DECIMAL(3,2) | 图像相似度 |
| match_type | VARCHAR(20) | 匹配类型(auto/manual) |
| status | TINYINT | 状态(0待确认/1已确认/2不匹配) |

---

## 开发指南

### 后端开发

**核心代码位置**：

- 实体类：`src/main/java/org/xingchang/brapi/entity/`
- 服务层：`src/main/java/org/xingchang/brapi/service/`
- 控制器：`src/main/java/org/xingchang/brapi/controller/`
- 数据访问：`src/main/java/org/xingchang/brapi/repository/`
- 匹配算法核心：`MatchService.java`

**新增API接口**：

1. 在 `entity` 包创建实体类（如需）
2. 在 `repository` 包创建 Repository 接口
3. 在 `service` 包实现业务逻辑
4. 在 `controller` 包创建 Controller

### 前端开发

**页面路由**：`hotai/Admin/src/router/index.js`

**API封装**：`hotai/Admin/src/api/`

**新增页面**：

1. 在 `views` 目录创建页面组件
2. 在 `router/index.js` 配置路由
3. 在 `api` 目录创建对应的API调用文件

---

## 截图展示

### 微信小程序
- 首页
- 发布页面
- 匹配记录页面
- 个人中心

### 管理后台
- 登录页面
- 仪表板
- 失物管理
- 匹配记录
- 数据统计

---

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

---

## 联系方式

- 作者：[你的名字]
- 邮箱：[你的邮箱]
- 项目地址：[GitHub仓库地址]

---

**版本**: 1.0.0  
**更新时间**: 2025

---

### 致谢

感谢所有为本项目做出贡献的开发者！
