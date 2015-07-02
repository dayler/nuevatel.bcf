/*
 Navicat Premium Data Transfer

 Source Server         : 10.40.3.131
 Source Server Type    : MySQL
 Source Server Version : 50604
 Source Host           : 10.40.3.131
 Source Database       : bcf

 Target Server Type    : MySQL
 Target Server Version : 50604
 File Encoding         : utf-8

 Date: 07/02/2015 11:33:45 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `param` varchar(64) DEFAULT NULL,
  `value` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `media`
-- ----------------------------
DROP TABLE IF EXISTS `media`;
CREATE TABLE `media` (
  `media_id` int(11) NOT NULL,
  `media_name` varchar(128) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`media_id`),
  UNIQUE KEY `idx_name` (`name`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `regex`
-- ----------------------------
DROP TABLE IF EXISTS `regex`;
CREATE TABLE `regex` (
  `regex_id` int(11) NOT NULL,
  `regex_name` varchar(128) DEFAULT NULL,
  `regex` varchar(64) DEFAULT NULL,
  `new_media_id` int(11) DEFAULT NULL,
  `swap_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`regex_id`),
  KEY `new_media_id` (`new_media_id`),
  KEY `swap_id` (`swap_id`),
  CONSTRAINT `fk_for_media_new_media_id_2_media_id` FOREIGN KEY (`new_media_id`) REFERENCES `media` (`media_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_for_swap_swap_id_2_swap_id` FOREIGN KEY (`swap_id`) REFERENCES `swap` (`swap_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `swap`
-- ----------------------------
DROP TABLE IF EXISTS `swap`;
CREATE TABLE `swap` (
  `swap_id` int(11) NOT NULL,
  `swap_name` varchar(128) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`swap_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `unit`
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `name` varchar(64) NOT NULL,
  `regex_id` int(11) NOT NULL,
  `creation_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `start_timestamp` datetime DEFAULT NULL,
  `end_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`name`,`regex_id`),
  KEY `fk_for_regex_regex_id_2_regex_id` (`regex_id`),
  CONSTRAINT `fk_for_regex_regex_id_2_regex_id` FOREIGN KEY (`regex_id`) REFERENCES `regex` (`regex_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
