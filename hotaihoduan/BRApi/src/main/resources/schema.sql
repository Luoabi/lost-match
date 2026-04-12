-- 创建数据库
CREATE DATABASE IF NOT EXISTS lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lost_found;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(500) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT '角色：student/teacher/admin/cleaner',
    student_id VARCHAR(50) COMMENT '学号/工号',
    department VARCHAR(100) COMMENT '院系/部门',
    wechat_open_id VARCHAR(100) COMMENT '微信OpenID',
    wechat_union_id VARCHAR(100) COMMENT '微信UnionID',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用/1正常',
    lost_count INT DEFAULT 0 COMMENT '发布失物数',
    found_count INT DEFAULT 0 COMMENT '发布拾获数',
    match_success_count INT DEFAULT 0 COMMENT '匹配成功数',
    credit_score INT DEFAULT 100 COMMENT '信用分',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_wechat_open_id (wechat_open_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 失物表
CREATE TABLE IF NOT EXISTS lost_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    type VARCHAR(50) NOT NULL COMMENT '物品类型',
    description TEXT COMMENT '详细描述',
    images JSON COMMENT '图片数组',
    location VARCHAR(100) COMMENT '丢失位置',
    location_detail VARCHAR(200) COMMENT '详细位置',
    longitude DECIMAL(10,6) COMMENT '经度',
    latitude DECIMAL(10,6) COMMENT '纬度',
    lost_time DATETIME COMMENT '丢失时间',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status TINYINT DEFAULT 0 COMMENT '状态：0未找回/1已找回/2已关闭',
    reward INT DEFAULT 0 COMMENT '悬赏金额',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    keywords JSON COMMENT '关键词数组',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    audit_status TINYINT DEFAULT 0 COMMENT '审核状态：0待审核/1已通过/2已拒绝',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    audit_time DATETIME COMMENT '审核时间',
    auditor_id BIGINT COMMENT '审核人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_audit_status (audit_status),
    INDEX idx_location (longitude, latitude),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='失物表';

-- 拾获表
CREATE TABLE IF NOT EXISTS found_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    type VARCHAR(50) NOT NULL COMMENT '物品类型',
    description TEXT COMMENT '详细描述',
    images JSON COMMENT '图片数组',
    location VARCHAR(100) COMMENT '拾获位置',
    location_detail VARCHAR(200) COMMENT '详细位置',
    longitude DECIMAL(10,6) COMMENT '经度',
    latitude DECIMAL(10,6) COMMENT '纬度',
    found_time DATETIME COMMENT '拾获时间',
    contact_name VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status TINYINT DEFAULT 0 COMMENT '状态：0待认领/1已归还/2已关闭',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    keywords JSON COMMENT '关键词数组',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    audit_status TINYINT DEFAULT 0 COMMENT '审核状态：0待审核/1已通过/2已拒绝',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    audit_time DATETIME COMMENT '审核时间',
    auditor_id BIGINT COMMENT '审核人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_audit_status (audit_status),
    INDEX idx_location (longitude, latitude),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拾获表';

-- 匹配记录表
CREATE TABLE IF NOT EXISTS match_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    lost_item_id BIGINT NOT NULL COMMENT '失物ID',
    found_item_id BIGINT NOT NULL COMMENT '拾获物ID',
    match_score DECIMAL(3,2) COMMENT '总匹配得分',
    image_score DECIMAL(3,2) COMMENT '图像相似度',
    location_score DECIMAL(3,2) COMMENT '位置匹配度',
    text_score DECIMAL(3,2) COMMENT '文本匹配度',
    match_type VARCHAR(20) DEFAULT 'auto' COMMENT '匹配类型：auto/manual',
    status TINYINT DEFAULT 0 COMMENT '状态：0待确认/1已确认/2不匹配',
    confirm_user_id BIGINT COMMENT '确认人ID',
    confirm_time DATETIME COMMENT '确认时间',
    feedback VARCHAR(500) COMMENT '用户反馈',
    feedback_score INT COMMENT '反馈评分1-5',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_lost_item_id (lost_item_id),
    INDEX idx_found_item_id (found_item_id),
    INDEX idx_match_score (match_score),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='匹配记录表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT COMMENT '操作人ID',
    username VARCHAR(50) COMMENT '操作人用户名',
    action VARCHAR(20) NOT NULL COMMENT '操作类型：create/update/delete/audit/login',
    module VARCHAR(20) NOT NULL COMMENT '模块：lost/found/user/match',
    content VARCHAR(500) COMMENT '操作内容',
    ip VARCHAR(50) COMMENT 'IP地址',
    status VARCHAR(20) DEFAULT 'success' COMMENT '状态：success/failed',
    params TEXT COMMENT '请求参数JSON',
    response TEXT COMMENT '响应结果JSON',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_module (module),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    description VARCHAR(200) COMMENT '描述',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 物品类型表
CREATE TABLE IF NOT EXISTS item_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '类型名称',
    sort INT DEFAULT 0 COMMENT '排序',
    enabled TINYINT DEFAULT 1 COMMENT '是否启用：0否/1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物品类型表';

-- 地点表
CREATE TABLE IF NOT EXISTS locations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '地点名称',
    sort INT DEFAULT 0 COMMENT '排序',
    enabled TINYINT DEFAULT 1 COMMENT '是否启用：0否/1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地点表';

-- 插入默认管理员账号 (密码: 123456, MD5加密)
INSERT INTO users (username, password, real_name, role, status) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin', 1)
ON DUPLICATE KEY UPDATE username=username;

-- 插入默认物品类型
INSERT INTO item_types (name, sort) VALUES 
('手机', 1), ('钱包', 2), ('钥匙', 3), ('书籍', 4),
('雨伞', 5), ('水杯', 6), ('耳机', 7), ('充电宝', 8),
('身份证', 9), ('校园卡', 10)
ON DUPLICATE KEY UPDATE name=name;

-- 插入默认地点
INSERT INTO locations (name, sort) VALUES 
('图书馆', 1), ('食堂', 2), ('教学楼A栋', 3), ('教学楼B栋', 4),
('宿舍楼', 5), ('操场', 6), ('体育馆', 7), ('实验楼', 8)
ON DUPLICATE KEY UPDATE name=name;

-- 插入默认系统配置
INSERT INTO system_config (config_key, config_value, description) VALUES 
('site_name', '校园失物追寻平台', '网站名称'),
('image_weight', '0.4', '图像相似度权重'),
('location_weight', '0.3', '位置匹配度权重'),
('text_weight', '0.3', '文本匹配度权重'),
('match_threshold', '0.6', '最低匹配阈值'),
('min_reward', '0', '最小悬赏金额'),
('max_reward', '1000', '最大悬赏金额'),
('max_images', '9', '最多图片数'),
('max_image_size', '5', '单张图片大小MB'),
('auto_close_day', '30', '自动关闭天数')
ON DUPLICATE KEY UPDATE config_key=config_key;
