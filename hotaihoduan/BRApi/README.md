# 校园失物追寻平台 - 后端API

基于SpringBoot 4.0.5 + JPA + MySQL的失物招领系统后端服务

## 技术栈

- **框架**: Spring Boot 4.0.5
- **ORM**: Spring Data JPA
- **数据库**: MySQL 8.0+
- **认证**: JWT
- **工具**: Lombok, Hutool
- **Java版本**: 17

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 创建数据库

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source src/main/resources/schema.sql
```

或者手动创建：
```sql
CREATE DATABASE lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置

编辑 `src/main/resources/application.properties`：

```properties
# 修改数据库连接信息
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
```

### 4. 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/BRApi-0.0.1-SNAPSHOT.jar
```

### 5. 测试接口

访问测试接口：
```
http://localhost:8080/api/test/hello
http://localhost:8080/api/test/info
```

## 项目结构

```
src/main/java/org/xingchang/brapi/
├── common/              # 通用类
│   ├── Result.java      # 统一响应
│   └── PageResult.java  # 分页响应
├── config/              # 配置类
│   └── CorsConfig.java  # 跨域配置
├── controller/          # 控制器
│   └── TestController.java
├── entity/              # 实体类
│   ├── User.java
│   ├── LostItem.java
│   ├── FoundItem.java
│   ├── MatchRecord.java
│   ├── OperationLog.java
│   ├── SystemConfig.java
│   ├── ItemType.java
│   └── Location.java
├── repository/          # 数据访问层
│   ├── UserRepository.java
│   ├── LostItemRepository.java
│   ├── FoundItemRepository.java
│   ├── MatchRecordRepository.java
│   ├── OperationLogRepository.java
│   ├── SystemConfigRepository.java
│   ├── ItemTypeRepository.java
│   └── LocationRepository.java
├── service/             # 业务逻辑层（待实现）
├── util/                # 工具类
│   └── JwtUtil.java
└── BrApiApplication.java
```

## 数据库表

### 核心表
- `users` - 用户表
- `lost_items` - 失物表
- `found_items` - 拾获表
- `match_records` - 匹配记录表

### 辅助表
- `operation_logs` - 操作日志表
- `system_config` - 系统配置表
- `item_types` - 物品类型表
- `locations` - 地点表

详细表结构请查看 `src/main/resources/schema.sql`

## API接口

### 测试接口
- GET `/api/test/hello` - 测试连接
- GET `/api/test/info` - 项目信息

### 用户接口（待实现）
- POST `/api/user/login` - 用户登录
- GET `/api/user/info` - 获取用户信息
- GET `/api/user/list` - 用户列表

### 失物接口（待实现）
- GET `/api/lost-item/list` - 失物列表
- GET `/api/lost-item/detail/{id}` - 失物详情
- POST `/api/lost-item/add` - 添加失物
- PUT `/api/lost-item/update` - 更新失物
- DELETE `/api/lost-item/delete/{id}` - 删除失物

### 拾获接口（待实现）
- GET `/api/found-item/list` - 拾获列表
- GET `/api/found-item/detail/{id}` - 拾获详情
- POST `/api/found-item/add` - 添加拾获
- PUT `/api/found-item/update` - 更新拾获
- DELETE `/api/found-item/delete/{id}` - 删除拾获

更多接口请查看 `BACKEND_IMPLEMENTATION.md`

## 默认账号

```
用户名：admin
密码：123456
```

## 响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "成功",
  "data": {},
  "timestamp": 1705305600000
}
```

### 失败响应
```json
{
  "code": 500,
  "message": "错误信息",
  "data": null,
  "timestamp": 1705305600000
}
```

### 分页响应
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

## 开发进度

- [x] 项目初始化
- [x] 数据库设计
- [x] 实体类创建
- [x] Repository层
- [x] 通用工具类
- [x] 跨域配置
- [ ] Service层
- [ ] Controller层
- [ ] JWT认证
- [ ] 日志拦截器
- [ ] 异常处理

## 注意事项

1. 首次启动会自动创建数据库表（JPA的ddl-auto=update）
2. 建议先执行schema.sql初始化数据
3. JWT密钥请在生产环境修改
4. 数据库密码请妥善保管

## 联系方式

如有问题，请查看 `BACKEND_IMPLEMENTATION.md` 获取更多信息。

---

**版本**: 1.0.0  
**更新时间**: 2025-01-15
