/*
 Navicat Premium Data Transfer

 Source Server         : 10.40.3.131
 Source Server Type    : MySQL
 Source Server Version : 50604
 Source Host           : 10.40.3.131
 Source Database       : bcf_record

 Target Server Type    : MySQL
 Target Server Version : 50604
 File Encoding         : utf-8

 Date: 06/30/2015 11:50:39 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `wsi_record`
-- ----------------------------
DROP TABLE IF EXISTS `wsi_record`;
CREATE TABLE `wsi_record` (
  `id` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `regex_id` int(11) DEFAULT NULL,
  `action` varchar(64) DEFAULT NULL,
  `from_ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ixd_name_response` (`name`,`response`) USING HASH
) ENGINE=MRG_MyISAM DEFAULT CHARSET=utf8 UNION=(`wsi_record_0`,`wsi_record_1`);

-- ----------------------------
--  Table structure for `wsi_record_0`
-- ----------------------------
DROP TABLE IF EXISTS `wsi_record_0`;
CREATE TABLE `wsi_record_0` (
  `id` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `regex_id` int(11) DEFAULT NULL,
  `action` varchar(64) DEFAULT NULL,
  `from_ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ixd_name_response` (`name`,`response`) USING HASH
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wsi_record_1`
-- ----------------------------
DROP TABLE IF EXISTS `wsi_record_1`;
CREATE TABLE `wsi_record_1` (
  `id` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `regex_id` int(11) DEFAULT NULL,
  `action` varchar(64) DEFAULT NULL,
  `from_ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ixd_name_response` (`name`,`response`) USING HASH
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wsi_record_201506301106`
-- ----------------------------
DROP TABLE IF EXISTS `wsi_record_201506301106`;
CREATE TABLE `wsi_record_201506301106` (
  `id` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `regex_id` int(11) DEFAULT NULL,
  `action` varchar(64) DEFAULT NULL,
  `from_ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ixd_name_response` (`name`,`response`) USING HASH
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `wsi_record_201506301107`
-- ----------------------------
DROP TABLE IF EXISTS `wsi_record_201506301107`;
CREATE TABLE `wsi_record_201506301107` (
  `id` varchar(64) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `regex_id` int(11) DEFAULT NULL,
  `action` varchar(64) DEFAULT NULL,
  `from_ip_address` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `response` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ixd_name_response` (`name`,`response`) USING HASH
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Triggers structure for table wsi_record_0
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_autogenerate_id`;
delimiter ;;
CREATE TRIGGER `tr_autogenerate_id` BEFORE INSERT ON `wsi_record_0` FOR EACH ROW begin     if new.id is null  or new.id = ''  or new.id = '0'     then         set new.id = uuid_short();     end if; end
 ;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
