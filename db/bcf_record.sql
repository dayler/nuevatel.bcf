CREATE DATABASE  IF NOT EXISTS `bcf_record` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `bcf_record`;
-- MySQL dump 10.13  Distrib 5.6.24, for osx10.8 (x86_64)
--
-- Host: 10.40.3.131    Database: bcf_record
-- ------------------------------------------------------
-- Server version	5.6.4-m7

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `wsi_record`
--

DROP TABLE IF EXISTS `wsi_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wsi_record_0`
--

DROP TABLE IF EXISTS `wsi_record_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = latin1 */ ;
/*!50003 SET character_set_results = latin1 */ ;
/*!50003 SET collation_connection  = latin1_swedish_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `wsi_record_1`
--

DROP TABLE IF EXISTS `wsi_record_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'bcf_record'
--

--
-- Dumping routines for database 'bcf_record'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-07-02 12:30:51
