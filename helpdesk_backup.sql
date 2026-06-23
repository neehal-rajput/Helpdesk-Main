-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: helpdesk_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `assignment_id` bigint NOT NULL AUTO_INCREMENT,
  `assigned_date` datetime(6) DEFAULT NULL,
  `assigned_to` bigint DEFAULT NULL,
  `ticket_id` bigint DEFAULT NULL,
  PRIMARY KEY (`assignment_id`),
  UNIQUE KEY `UK_ju8aiped4rd70tyxxvkme47se` (`ticket_id`),
  KEY `FK3wm4vepyceb348quud9y1bx7g` (`assigned_to`),
  CONSTRAINT `FK3wm4vepyceb348quud9y1bx7g` FOREIGN KEY (`assigned_to`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKo9wcactgruf2l6m8tw49vm7kv` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`ticket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
INSERT INTO `assignments` VALUES (1,'2026-06-18 16:11:03.935296',2,2),(2,'2026-06-18 16:30:02.907976',2,1),(3,'2026-06-19 12:25:07.006885',2,4);
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resolutions`
--

DROP TABLE IF EXISTS `resolutions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resolutions` (
  `resolution_id` bigint NOT NULL AUTO_INCREMENT,
  `resolution_notes` text,
  `resolved_date` datetime(6) DEFAULT NULL,
  `resolved_by` bigint DEFAULT NULL,
  `ticket_id` bigint DEFAULT NULL,
  PRIMARY KEY (`resolution_id`),
  UNIQUE KEY `UK_81ufyyq2fnwntu12hns6k77m8` (`ticket_id`),
  KEY `FKesk74q130tbypi91qg3uloop` (`resolved_by`),
  CONSTRAINT `FKefyo45s4l6jhde3d03yrpe903` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`ticket_id`),
  CONSTRAINT `FKesk74q130tbypi91qg3uloop` FOREIGN KEY (`resolved_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resolutions`
--

LOCK TABLES `resolutions` WRITE;
/*!40000 ALTER TABLE `resolutions` DISABLE KEYS */;
INSERT INTO `resolutions` VALUES (1,'resolved','2026-06-18 16:12:13.615992',2,2),(2,'resolved','2026-06-18 17:30:26.846267',2,1),(3,'resolved','2026-06-19 12:25:52.793362',2,4);
/*!40000 ALTER TABLE `resolutions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tickets` (
  `ticket_id` bigint NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `description` text,
  `priority` enum('LOW','MEDIUM','HIGH','CRITICAL') NOT NULL,
  `status` enum('OPEN','IN_PROGRESS','RESOLVED','CLOSED') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `FK4eqsebpimnjen0q46ja6fl2hl` (`user_id`),
  CONSTRAINT `FK4eqsebpimnjen0q46ja6fl2hl` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tickets`
--

LOCK TABLES `tickets` WRITE;
/*!40000 ALTER TABLE `tickets` DISABLE KEYS */;
INSERT INTO `tickets` VALUES (1,'2026-06-18 16:07:48.500515','not geting login','MEDIUM','RESOLVED','error','2026-06-19 12:28:11.172378',1),(2,'2026-06-18 16:09:46.152966','login','CRITICAL','RESOLVED','login error','2026-06-18 16:09:46.152966',3),(3,'2026-06-18 16:29:11.998532','testing will be done tomorrow','CRITICAL','OPEN','testing error','2026-06-18 16:29:11.998532',4),(4,'2026-06-19 12:24:24.140316','12345678','CRITICAL','RESOLVED','login error','2026-06-19 12:24:24.141332',4);
/*!40000 ALTER TABLE `tickets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('ADMIN','AGENT','END_USER') NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@helpdesk.com','System Admin','$2a$10$KVNnsn2LvCCICaC0IQBfxedUWuPF9w0per2RsfPBRu1yCcjRMJ6EW','ADMIN'),(2,'agent@helpdesk.com','Support Agent','$2a$10$ubzI6a1f2JC5yrOd7bYJtuoYGUSyO1hmZuyjMt.Bbq.xpgHswfMu6','AGENT'),(3,'user@helpdesk.com','End User','$2a$10$oNvdB9ujOMU2vDQRmD/Gze5tazOEBqHQw8lr8YE1sh8CZst56JRWu','END_USER'),(4,'geetanshu99999@gmail.com','Geetanshu Kalra','$2a$10$oUnxn9VyeFcbjG.9CYZneevyNEAUPGG5dWgxaGnk8nNSio/OxkTRq','END_USER'),(5,'vish@gmail.com','Vishwajeet','$2a$10$GKpLiAvb0A6LXEsvoZO6HuI77OcPDqbLevvW4Kswxhq6LJPNABRLa','AGENT'),(6,'neehalsingh10@gmail.com','neehalsingh','$2a$10$jUYEXm15tEOYl1yzkbCkL.cySwf8Bta2aPhJQdOizJeuocrIuEcsW','ADMIN');
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

-- Dump completed on 2026-06-23  4:43:46
