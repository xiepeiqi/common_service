/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50626
Source Host           : localhost:3306
Source Database       : cs

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2019-08-07 09:50:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_person
-- ----------------------------
DROP TABLE IF EXISTS `tbl_person`;
CREATE TABLE `tbl_person` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `name` varchar(20) NOT NULL COMMENT '人员名称',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `type` tinyint(4) DEFAULT NULL COMMENT '人员类型',
  `salary` double(11,3) DEFAULT NULL COMMENT '一年薪水',
  `image_byte` blob COMMENT '人员注册照（不能超过65k,业务设成不能超过30k）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员表';

-- ----------------------------
-- Records of tbl_person
-- ----------------------------
