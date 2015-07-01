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

 Date: 06/30/2015 11:50:19 AM
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
--  Records of `config`
-- ----------------------------
BEGIN;
INSERT INTO `config` VALUES ('1', 'sms_alarm', ''), ('2', 'email_alarm', 'ariel.salazar@nuevatel.com,claudia.velarde@nuevatel.com'), ('3', 'sms_unitname', '2233'), ('4', 'from_email', 'ariel.salazar@nuevatel.com'), ('5', 'endpoint_dispatcher_application', 'http://10.40.20.201:8217/dispatcherApplication'), ('6', 'endpoint_mail_middleware', 'http://10.40.20.201:8190/mailMiddleware'), ('7', 'alert_header', 'Se ha producido %s incidentes.'), ('8', 'email_subject', '[BCF]: Notificaiones de alertas');
COMMIT;

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
--  Records of `media`
-- ----------------------------
BEGIN;
INSERT INTO `media` VALUES ('1', 'cotel_new_media1', '59150200061', '40', '40'), ('2', 'cotel_new_media1', '5915040001233', '40', '40'), ('3', 'cotel_new_media3', '50200064', '40', '40');
COMMIT;

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
--  Records of `regex`
-- ----------------------------
BEGIN;
INSERT INTO `regex` VALUES ('1', 'cotel_regex_1', '0?0?14.+|(((591)|0)?(2|3|4|6|7).+)', null, '1'), ('2', 'cotel_regex_2', '0?0?14.+|(((591)|0)?(2|3|4|6|7).+)', '1', null), ('3', 'cotel_regex_3', '(((591)|0)?(2|3|4|6|7).+)', null, '1'), ('4', 'cotel_regex_4', '0?0?14.+', null, '1'), ('5', 'test_regex_01', '\\d', null, null), ('6', 'test_regex_02', '\\d', '1', null), ('7', 'test_regex_03', '\\d', null, '2'), ('8', 'cotel_regex_1', '\\d', '1', '2'), ('9', 'cotel_regex_9', '\\d', '1', null);
COMMIT;

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
--  Records of `swap`
-- ----------------------------
BEGIN;
INSERT INTO `swap` VALUES ('1', 'cotel_ivr', '50200060', '3'), ('2', 'test_swap', '50400073', '3'), ('3', 'test2_swap', '70710151', '3');
COMMIT;

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

-- ----------------------------
--  Records of `unit`
-- ----------------------------
BEGIN;
INSERT INTO `unit` VALUES ('111', '3', '2015-06-29 09:20:37', '2015-06-29 09:20:40', null), ('111', '4', '2015-06-29 09:23:54', '2015-06-29 09:23:58', null), ('123', '3', '2015-06-30 10:42:24', '2015-06-30 10:42:27', null), ('21345678', '3', '2015-06-25 18:52:40', '2015-06-25 18:52:44', null), ('24745487', '3', '2015-06-25 15:53:21', '2015-06-25 15:53:24', null), ('24752012', '4', '2015-06-25 15:55:29', '2015-06-25 15:55:32', null), ('24753028', '4', '2015-06-25 15:55:11', '2015-06-25 15:55:15', null), ('65380073', '3', '2015-06-29 09:21:18', '2015-06-29 09:21:22', null), ('70100044', '7', '2015-06-26 16:20:03', '2015-06-06 00:00:00', null), ('888', '3', '2015-06-29 09:20:47', '2015-06-29 09:20:50', null), ('_50400073', '6', '2015-06-26 02:48:32', '2015-06-06 00:00:00', null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
