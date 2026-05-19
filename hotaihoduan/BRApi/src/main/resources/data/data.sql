-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lost_found
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `found_items`
--

DROP TABLE IF EXISTS `found_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `found_items` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `title` varchar(100) NOT NULL COMMENT '标题',
                               `type` varchar(50) NOT NULL COMMENT '物品类型',
                               `description` text COMMENT '详细描述',
                               `images` json DEFAULT NULL COMMENT '图片数组',
                               `location` varchar(100) DEFAULT NULL COMMENT '拾获位置',
                               `location_detail` varchar(200) DEFAULT NULL COMMENT '详细位置',
                               `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
                               `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
                               `found_time` datetime DEFAULT NULL COMMENT '拾获时间',
                               `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
                               `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                               `status` tinyint DEFAULT '0',
                               `view_count` int DEFAULT '0' COMMENT '浏览次数',
                               `keywords` json DEFAULT NULL COMMENT '关键词数组',
                               `user_id` bigint NOT NULL COMMENT '用户ID',
                               `audit_status` tinyint DEFAULT '0',
                               `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
                               `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
                               `auditor_id` bigint DEFAULT NULL COMMENT '审核人ID',
                               `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_user_id` (`user_id`),
                               KEY `idx_status` (`status`),
                               KEY `idx_type` (`type`),
                               KEY `idx_audit_status` (`audit_status`),
                               KEY `idx_location` (`longitude`,`latitude`),
                               KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='拾获表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `found_items`
--

LOCK TABLES `found_items` WRITE;
/*!40000 ALTER TABLE `found_items` DISABLE KEYS */;
INSERT INTO `found_items` VALUES (1,'拾到iPhone15手机','手机','黑色，壳蓝色','[\"/upload/2026/phone1.jpg\"]','图书馆','三楼自习区',116.403874,39.914885,'2026-04-01 15:00:00','李四','13800138002',1,1,NULL,3,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 02:21:10'),(2,'拾到黑色钱包','钱包','有身份证银行卡','[\"/upload/2026/wallet1.jpg\"]','食堂','一楼窗口',116.405215,39.915120,'2026-04-02 12:30:00','王五','13800138003',0,0,NULL,4,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(3,'拾到一串钥匙','钥匙','带小熊挂件','[]','教学楼A栋','402教室',116.402156,39.913652,'2026-04-03 10:20:00','赵六','13800138004',1,0,NULL,5,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(4,'拾到校园卡','校园卡','卡号2024004','[]','宿舍楼','三号楼门口',116.406582,39.916254,'2026-04-04 22:10:00','张三','13800138001',0,0,NULL,2,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(5,'拾到蓝色雨伞','雨伞','小米全自动','[]','操场','跑道旁',116.401458,39.917563,'2026-04-05 17:40:00','陈老师','13900139001',0,0,NULL,6,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(6,'拾到白色耳机','耳机','AirPods Pro','[\"/upload/2026/earphone1.jpg\"]','体育馆','羽毛球馆',116.404785,39.912356,'2026-04-05 19:20:00','刘老师','13900139002',0,0,NULL,7,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(7,'拾到身份证','身份证','姓名王五','[]','实验楼','二楼大厅',116.400125,39.914251,'2026-04-06 09:50:00','张阿姨','13700137001',0,0,NULL,8,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(8,'拾到黑色充电宝','充电宝','罗马仕2万毫安','[]','教学楼B栋','305教室',116.403652,39.915896,'2026-04-06 16:00:00','李叔','13700137002',0,0,NULL,9,0,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(9,'拾到Java书籍','书籍','Java编程思想','[]','图书馆','二楼书库',116.403874,39.914885,'2026-04-07 16:30:00','测试用户','13600136001',0,0,NULL,10,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(10,'拾到透明水杯','水杯','500ml玻璃杯','[]','食堂','二楼餐桌',116.405215,39.915120,'2026-04-07 12:50:00','张三','13800138001',2,0,NULL,2,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(11,'拾到华为手机','手机','Mate60黑色','[\"/upload/2026/phone2.jpg\"]','图书馆','一楼大厅',116.403874,39.914885,'2026-04-08 13:20:00','李四','13800138002',0,0,NULL,3,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(12,'拾到棕色钱包','钱包','皮质，有现金','[]','教学楼A栋','楼梯口',116.402156,39.913652,'2026-04-08 08:40:00','王五','13800138003',0,0,NULL,4,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(13,'拾到门卡钥匙','钥匙','宿舍卡+钥匙','[]','宿舍楼','一楼大厅',116.406582,39.916254,'2026-04-09 22:00:00','赵六','13800138004',0,1,NULL,5,0,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 19:29:34'),(19,'黑色手机iPhone','电子产品','黑色的手机','[\"http://localhost:8080/api/uploads/2026/04/12/58e36991-dcf4-4800-8c9c-0c3147457711.png\"]','图书馆','上海市上海市',121.489410,31.405270,'2026-04-12 00:00:00','text3','14878888888',0,0,'[\"黑色\", \"手机\"]',16,1,'审核通过','2026-04-12 13:27:49',1,'2026-04-12 13:20:52','2026-04-12 13:27:49'),(20,'白色 手机','电子产品','白色手机苹果','[\"http://localhost:8080/api/uploads/2026/04/18/58e698b2-1b13-4d3c-b9d0-577845b37816.png\"]','图书馆','图书馆',NULL,NULL,'2026-04-18 00:00:00','测试号2','18587000000',1,0,'[\"白色\", \"手机\"]',18,0,NULL,NULL,NULL,'2026-04-18 11:20:39','2026-04-18 11:21:49');
/*!40000 ALTER TABLE `found_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_types`
--

DROP TABLE IF EXISTS `item_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item_types` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `name` varchar(50) NOT NULL COMMENT '类型名称',
                              `sort` int DEFAULT '0' COMMENT '排序',
                              `enabled` tinyint DEFAULT '1',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物品类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_types`
--

LOCK TABLES `item_types` WRITE;
/*!40000 ALTER TABLE `item_types` DISABLE KEYS */;
INSERT INTO `item_types` VALUES (1,'手机',1,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(2,'钱包',2,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(3,'钥匙',3,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(4,'书籍',4,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(5,'雨伞',5,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(6,'水杯',6,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(7,'耳机',7,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(8,'充电宝',8,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(9,'身份证',9,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(10,'校园卡',10,1,'2026-04-08 13:03:23','2026-04-08 13:03:23');
/*!40000 ALTER TABLE `item_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `name` varchar(100) NOT NULL COMMENT '地点名称',
                             `sort` int DEFAULT '0' COMMENT '排序',
                             `enabled` tinyint DEFAULT '1',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'图书馆',1,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(2,'食堂',2,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(3,'教学楼A栋',3,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(4,'教学楼B栋',4,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(5,'宿舍楼',5,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(6,'操场',6,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(7,'体育馆',7,1,'2026-04-08 13:03:23','2026-04-08 13:03:23'),(8,'实验楼',8,1,'2026-04-08 13:03:23','2026-04-08 13:03:23');
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lost_items`
--

DROP TABLE IF EXISTS `lost_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lost_items` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `title` varchar(100) NOT NULL COMMENT '标题',
                              `type` varchar(50) NOT NULL COMMENT '物品类型',
                              `description` text COMMENT '详细描述',
                              `images` json DEFAULT NULL COMMENT '图片数组',
                              `location` varchar(100) DEFAULT NULL COMMENT '丢失位置',
                              `location_detail` varchar(200) DEFAULT NULL COMMENT '详细位置',
                              `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
                              `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
                              `lost_time` datetime DEFAULT NULL COMMENT '丢失时间',
                              `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
                              `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                              `status` tinyint DEFAULT '0',
                              `reward` int DEFAULT '0' COMMENT '悬赏金额',
                              `view_count` int DEFAULT '0' COMMENT '浏览次数',
                              `keywords` json DEFAULT NULL COMMENT '关键词数组',
                              `user_id` bigint NOT NULL COMMENT '用户ID',
                              `audit_status` tinyint DEFAULT '0',
                              `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
                              `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
                              `auditor_id` bigint DEFAULT NULL COMMENT '审核人ID',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              KEY `idx_user_id` (`user_id`),
                              KEY `idx_status` (`status`),
                              KEY `idx_type` (`type`),
                              KEY `idx_audit_status` (`audit_status`),
                              KEY `idx_location` (`longitude`,`latitude`),
                              KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='失物表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lost_items`
--

LOCK TABLES `lost_items` WRITE;
/*!40000 ALTER TABLE `lost_items` DISABLE KEYS */;
INSERT INTO `lost_items` VALUES (1,'丢失iPhone15手机','手机','黑色iPhone15，手机壳是蓝色','[\"/upload/2026/phone1.jpg\"]','图书馆','三楼自习区',116.403874,39.914885,'2026-04-01 14:30:00','张三','13800138001',0,50,5,NULL,2,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 13:02:46'),(2,'丢失黑色钱包','钱包','钱包内有身份证、银行卡','[\"/upload/2026/wallet1.jpg\"]','食堂','一楼窗口',116.405215,39.915120,'2026-04-02 12:10:00','李四','13800138002',0,30,1,NULL,3,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 02:21:20'),(3,'丢失一串钥匙','钥匙','银色钥匙，带小熊挂件','[]','教学楼A栋','402教室',116.402156,39.913652,'2026-04-03 10:00:00','王五','13800138003',1,0,0,NULL,4,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(4,'丢失校园卡','校园卡','卡号2024004，照片是本人','[]','宿舍楼','三号楼门口',116.406582,39.916254,'2026-04-04 22:00:00','赵六','13800138004',0,0,1,NULL,5,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-11 21:41:35'),(5,'丢失蓝色雨伞','雨伞','全自动折叠伞，品牌是小米','[]','操场','跑道旁',116.401458,39.917563,'2026-04-05 17:20:00','张三','13800138001',0,0,0,NULL,2,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(6,'丢失白色耳机','耳机','AirPods Pro，充电盒有划痕','[\"/upload/2026/earphone1.jpg\"]','体育馆','羽毛球馆',116.404785,39.912356,'2026-04-05 19:00:00','陈老师','13900139001',0,20,0,NULL,6,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(7,'丢失身份证','身份证','姓名王五，地址XX市','[]','实验楼','二楼大厅',116.400125,39.914251,'2026-04-06 09:30:00','王五','13800138003',0,0,0,NULL,4,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(8,'丢失黑色充电宝','充电宝','20000毫安，罗马仕','[]','教学楼B栋','305教室',116.403652,39.915896,'2026-04-06 15:40:00','刘老师','13900139002',0,0,0,NULL,7,0,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(9,'丢失专业书籍','书籍','《Java编程思想》第四版','[]','图书馆','二楼书库',116.403874,39.914885,'2026-04-07 16:10:00','赵六','13800138004',0,0,0,NULL,5,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(10,'丢失透明水杯','水杯','500ml玻璃杯，有茶隔','[]','食堂','二楼餐桌',116.405215,39.915120,'2026-04-07 12:30:00','李四','13800138002',2,0,0,NULL,3,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(11,'丢失华为手机','手机','华为Mate60，黑色','[\"/upload/2026/phone2.jpg\"]','图书馆','一楼大厅',116.403874,39.914885,'2026-04-08 13:00:00','测试用户','13600136001',1,100,2,NULL,10,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 00:18:35'),(12,'丢失棕色钱包','钱包','皮质钱包，内有现金100元','[]','教学楼A栋','楼梯口',116.402156,39.913652,'2026-04-08 08:20:00','陈老师','13900139001',0,0,9,NULL,6,1,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 02:49:14'),(13,'丢失门卡钥匙','钥匙','宿舍门卡+两把钥匙','[]','宿舍楼','一楼大厅',116.406582,39.916254,'2026-04-09 21:50:00','刘老师','13900139002',0,0,5,NULL,7,0,NULL,NULL,NULL,'2026-04-08 15:12:25','2026-04-12 01:02:07'),(29,'黑色iPhone手机','电子产品','黑色的13','[\"http://localhost:8080/api/uploads/2026/04/12/34b9f39f-d961-4535-b5b6-cb5fb953652f.png\"]','图书馆','上海市宝山区友谊支路上海市宝山区人民政府(友谊支路东)',121.489410,31.405270,'2026-04-12 00:00:00','text2','14556666666',0,120,4,'[\"黑色\", \"手机\"]',15,1,'审核通过','2026-04-12 13:27:51',1,'2026-04-12 13:17:51','2026-04-12 14:01:29'),(30,'测试最后','电子产品','手机','[\"http://localhost:8080/api/uploads/2026/04/18/19789bca-ee2e-4cc8-91c6-c780ecb4f9e4.png\"]','图书馆','二楼',NULL,NULL,'2026-04-18 00:00:00','测试最后','18580000000',1,20,1,'[\"手机\", \"白色\"]',17,0,NULL,NULL,NULL,'2026-04-18 11:17:52','2026-04-18 11:21:49');
/*!40000 ALTER TABLE `lost_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match_records`
--

DROP TABLE IF EXISTS `match_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `match_records` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `lost_item_id` bigint NOT NULL COMMENT '失物ID',
                                 `found_item_id` bigint NOT NULL COMMENT '拾获物ID',
                                 `match_score` decimal(3,2) DEFAULT NULL COMMENT '总匹配得分',
                                 `image_score` decimal(3,2) DEFAULT NULL COMMENT '图像相似度',
                                 `location_score` decimal(3,2) DEFAULT NULL COMMENT '位置匹配度',
                                 `text_score` decimal(3,2) DEFAULT NULL COMMENT '文本匹配度',
                                 `match_type` varchar(20) DEFAULT 'auto' COMMENT '匹配类型：auto/manual',
                                 `status` tinyint DEFAULT '0',
                                 `confirm_user_id` bigint DEFAULT NULL COMMENT '确认人ID',
                                 `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
                                 `feedback` varchar(500) DEFAULT NULL COMMENT '用户反馈',
                                 `feedback_score` int DEFAULT NULL COMMENT '反馈评分1-5',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_lost_item_id` (`lost_item_id`),
                                 KEY `idx_found_item_id` (`found_item_id`),
                                 KEY `idx_match_score` (`match_score`),
                                 KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='匹配记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match_records`
--

LOCK TABLES `match_records` WRITE;
/*!40000 ALTER TABLE `match_records` DISABLE KEYS */;
INSERT INTO `match_records` VALUES (1,1,1,0.95,0.98,0.95,0.92,'auto',1,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(2,2,2,0.90,0.92,0.90,0.88,'auto',2,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(3,3,3,0.98,0.95,1.00,0.99,'auto',1,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(4,4,4,0.92,0.85,0.98,0.93,'auto',2,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(5,5,5,0.88,0.82,0.95,0.87,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(6,6,6,0.94,0.96,0.90,0.95,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(7,7,7,0.85,0.78,0.92,0.85,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(8,11,11,0.96,0.99,0.95,0.94,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(9,12,12,0.86,0.80,0.93,0.85,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(10,15,15,0.89,0.84,0.96,0.88,'auto',0,NULL,NULL,NULL,NULL,'2026-04-08 15:12:25'),(11,29,19,1.00,0.50,1.00,1.00,'auto',2,NULL,NULL,NULL,NULL,'2026-04-12 13:20:52'),(12,30,19,0.67,0.50,1.00,0.67,'auto',0,NULL,NULL,NULL,NULL,'2026-04-18 11:17:52'),(13,29,20,0.70,0.50,1.00,0.67,'auto',0,NULL,NULL,NULL,NULL,'2026-04-18 11:20:39'),(14,30,20,1.00,0.50,1.00,1.00,'auto',1,18,'2026-04-18 11:21:49',NULL,NULL,'2026-04-18 11:20:39');
/*!40000 ALTER TABLE `match_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_logs`
--

DROP TABLE IF EXISTS `operation_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_logs` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `user_id` bigint DEFAULT NULL COMMENT '操作人ID',
                                  `username` varchar(50) DEFAULT NULL COMMENT '操作人用户名',
                                  `action` varchar(20) NOT NULL COMMENT '操作类型：create/update/delete/audit/login',
                                  `module` varchar(20) NOT NULL COMMENT '模块：lost/found/user/match',
                                  `content` varchar(500) DEFAULT NULL COMMENT '操作内容',
                                  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
                                  `status` varchar(20) DEFAULT 'success' COMMENT '状态：success/failed',
                                  `params` text COMMENT '请求参数JSON',
                                  `response` text COMMENT '响应结果JSON',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_user_id` (`user_id`),
                                  KEY `idx_action` (`action`),
                                  KEY `idx_module` (`module`),
                                  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_logs`
--

LOCK TABLES `operation_logs` WRITE;
/*!40000 ALTER TABLE `operation_logs` DISABLE KEYS */;
INSERT INTO `operation_logs` VALUES (1,1,'admin','login','user','管理员登录系统','127.0.0.1','success',NULL,NULL,'2026-04-08 15:12:25'),(2,2,'student01','create','lost','发布失物：iPhone15','192.168.1.101','success',NULL,NULL,'2026-04-08 15:12:25'),(3,3,'student02','create','found','拾获物品：钱包','192.168.1.102','success',NULL,NULL,'2026-04-08 15:12:25'),(4,1,'admin','audit','lost','审核通过失物记录','127.0.0.1','success',NULL,NULL,'2026-04-08 15:12:25'),(5,4,'student03','update','lost','更新钥匙信息','192.168.1.103','success',NULL,NULL,'2026-04-08 15:12:25'),(6,5,'student04','create','lost','发布丢失校园卡','192.168.1.104','success',NULL,NULL,'2026-04-08 15:12:25'),(7,6,'teacher01','create','found','拾获耳机','192.168.1.105','success',NULL,NULL,'2026-04-08 15:12:25'),(8,1,'admin','login','user','管理员后台登录','127.0.0.1','success',NULL,NULL,'2026-04-08 15:12:25'),(9,8,'cleaner01','create','found','拾获身份证','192.168.1.107','success',NULL,NULL,'2026-04-08 15:12:25'),(10,10,'testuser01','create','lost','发布丢失手机','192.168.1.109','success',NULL,NULL,'2026-04-08 15:12:25');
/*!40000 ALTER TABLE `operation_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `config_key` varchar(100) NOT NULL COMMENT '配置键',
                                 `config_value` text COMMENT '配置值',
                                 `description` varchar(200) DEFAULT NULL COMMENT '描述',
                                 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES (1,'site_name','校园失物追寻平台','网站名称','2026-04-29 21:43:12'),(2,'image_weight','0.5','图像相似度权重','2026-04-11 23:51:08'),(3,'location_weight','0.2','位置匹配度权重','2026-04-11 23:51:08'),(4,'text_weight','0.3','文本匹配度权重','2026-04-11 23:51:08'),(5,'match_threshold','0.6','最低匹配阈值','2026-04-11 23:51:08'),(6,'min_reward','3','最小悬赏金额','2026-04-11 23:50:57'),(7,'max_reward','1000','最大悬赏金额','2026-04-11 23:50:57'),(8,'max_images','9','最多图片数','2026-04-11 23:50:57'),(9,'max_image_size','5','单张图片大小MB','2026-04-11 23:50:57'),(10,'auto_close_day','30','自动关闭天数','2026-04-11 23:50:57'),(11,'system.site.name','校园失物追寻平台',NULL,'2026-04-11 23:46:41'),(12,'system.phone','啊手动阀',NULL,'2026-04-11 23:46:41'),(13,'match.image.weight','0.4',NULL,'2026-04-11 23:49:14'),(14,'match.location.weight','0.3',NULL,'2026-04-11 23:49:14'),(15,'match.text.weight','0.3',NULL,'2026-04-11 23:49:14'),(16,'match.threshold','0.6',NULL,'2026-04-11 23:49:14'),(17,'system.min.reward','2',NULL,'2026-04-11 23:49:42'),(18,'system.max.reward','1000',NULL,'2026-04-11 23:49:42'),(19,'system.max.images','9',NULL,'2026-04-11 23:49:42'),(20,'system.max.image.size','5',NULL,'2026-04-11 23:49:42'),(21,'system.auto.close.day','30',NULL,'2026-04-11 23:49:42'),(22,'phone','啊手动阀',NULL,'2026-04-29 21:43:12'),(23,'email','aa',NULL,'2026-04-29 21:43:12');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `username` varchar(50) NOT NULL COMMENT '用户名',
                         `password` varchar(255) NOT NULL COMMENT '密码',
                         `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
                         `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
                         `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                         `avatar` varchar(500) DEFAULT NULL COMMENT '头像URL',
                         `role` varchar(20) NOT NULL DEFAULT 'student' COMMENT '角色：student/teacher/admin/cleaner',
                         `student_id` varchar(50) DEFAULT NULL COMMENT '学号/工号',
                         `department` varchar(100) DEFAULT NULL COMMENT '院系/部门',
                         `wechat_open_id` varchar(100) DEFAULT NULL COMMENT '微信OpenID',
                         `wechat_union_id` varchar(100) DEFAULT NULL COMMENT '微信UnionID',
                         `status` tinyint DEFAULT '1',
                         `lost_count` int DEFAULT '0' COMMENT '发布失物数',
                         `found_count` int DEFAULT '0' COMMENT '发布拾获数',
                         `match_success_count` int DEFAULT '0' COMMENT '匹配成功数',
                         `credit_score` int DEFAULT '100' COMMENT '信用分',
                         `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `username` (`username`),
                         KEY `idx_username` (`username`),
                         KEY `idx_phone` (`phone`),
                         KEY `idx_wechat_open_id` (`wechat_open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','e10adc3949ba59abbe56e057f20f883e','管理员','','luo@qq.com','http://localhost:8080/api/uploads/2026/04/29/10dba64d-7dc9-4944-80fc-29524c759ead.png','admin',NULL,NULL,NULL,NULL,1,0,0,0,100,'2026-04-29 22:05:28','2026-04-08 13:03:23','2026-04-29 22:24:48'),(4,'student01','e10adc3949ba59abbe56e057f20f883e','张三','13800138001','zhangsan@school.com',NULL,'student','2024001','计算机学院',NULL,NULL,1,0,0,0,100,'2026-04-08 19:29:03','2026-04-08 15:12:25','2026-04-08 19:29:03'),(5,'student02','e10adc3949ba59abbe56e057f20f883e','李四','13800138002','lisi@school.com',NULL,'student','2024002','外国语学院',NULL,NULL,1,0,0,0,98,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(6,'student03','e10adc3949ba59abbe56e057f20f883e','王五','13800138003','wangwu@school.com',NULL,'student','2024003','机械学院',NULL,NULL,1,0,0,0,100,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(7,'student04','e10adc3949ba59abbe56e057f20f883e','赵六','13800138004','zhaoliu@school.com',NULL,'student','2024004','经管学院',NULL,NULL,1,0,0,0,95,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(8,'teacher01','e10adc3949ba59abbe56e057f20f883e','陈老师','13900139001','chen@school.com',NULL,'teacher','T202001','计算机学院',NULL,NULL,1,0,0,0,100,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(9,'teacher02','e10adc3949ba59abbe56e057f20f883e','刘老师','13900139002','liu@school.com',NULL,'teacher','T202002','教务处',NULL,NULL,1,0,0,0,100,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(10,'cleaner01','e10adc3949ba59abbe56e057f20f883e','张阿姨','13700137001','cleaner1@school.com',NULL,'cleaner','C001','后勤处',NULL,NULL,1,0,0,0,100,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(11,'cleaner02','e10adc3949ba59abbe56e057f20f883e','李叔','13700137002','cleaner2@school.com',NULL,'cleaner','C002','后勤处',NULL,NULL,1,0,0,0,100,NULL,'2026-04-08 15:12:25','2026-04-08 15:12:25'),(12,'testuser01','e10adc3949ba59abbe56e057f20f883e','测试用户','13600136001','test@school.com',NULL,'student','2024999','测试学院',NULL,NULL,1,0,0,0,90,'2026-04-11 22:07:59','2026-04-08 15:12:25','2026-04-11 22:07:59'),(15,'text2','3dbe00a167653a1aaee01d93e77e730e','asdfasdf','14556666666','',NULL,'student',NULL,NULL,NULL,NULL,1,0,0,0,100,'2026-04-12 14:00:05','2026-04-12 00:54:09','2026-04-12 14:00:05'),(16,'text3','3dbe00a167653a1aaee01d93e77e730e','hei','14878888888','',NULL,'student',NULL,NULL,NULL,NULL,1,0,0,0,100,'2026-04-12 13:41:44','2026-04-12 13:19:21','2026-04-12 13:41:44'),(17,'测试号1','3dbe00a167653a1aaee01d93e77e730e','luo','18580000000','',NULL,'student',NULL,NULL,NULL,NULL,1,0,0,0,100,'2026-04-29 22:26:23','2026-04-18 11:05:03','2026-04-29 22:26:23'),(18,'测试号2','3dbe00a167653a1aaee01d93e77e730e','luo2','18587000000','',NULL,'student',NULL,NULL,NULL,NULL,1,0,0,0,100,'2026-04-18 11:19:32','2026-04-18 11:06:59','2026-04-18 11:19:32');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-30 16:00:48
