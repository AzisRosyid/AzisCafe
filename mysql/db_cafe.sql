CREATE DATABASE  IF NOT EXISTS `db_cafe` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_cafe`;
-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: db_cafe
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` char(128) NOT NULL,
  `phone_number` varchar(13) NOT NULL,
  `position` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Azis Rosyid','azisrosyid@gmail.com','5605b76cdd822e8ad32ba9cc29d4866b6338cde10c138439e557b42e14245a47adf370bb8cc767865290bf427920f17b8a292706e2beb1f4ed1b9d4170cdfe9b','0895421891378','Boss');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` double NOT NULL,
  `carbo` int NOT NULL,
  `protein` int NOT NULL,
  `image` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` VALUES (1,'Risotto',50000,150,50,'risotto.jpeg'),(2,'Lasagna',55000,150,200,'lasagna.jpg'),(3,'Mac and Cheese',60000,150,75,'mac and cheese.jpg'),(4,'French Fries',20000,120,70,'french fries.jpg'),(5,'Cheese Burger',35000,170,120,'cheese burger.jpeg'),(6,'Iced Coffee',18000,50,80,'iced coffee.jpg'),(7,'Matcha Latte',21000,75,75,'matcha latte.jpg'),(8,'Pepperoni Pizza',50000,160,100,'pepperoni pizza.jpg');
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `menu_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,3,2,1),(2,3,6,1),(3,3,7,1),(4,4,6,1),(5,4,3,1),(6,5,6,1),(7,6,6,1),(8,7,6,1),(9,8,2,1),(10,8,7,1),(11,9,4,1),(12,10,7,1),(13,11,5,1),(14,12,5,2),(15,12,4,3),(16,12,8,11),(17,12,3,5),(18,12,6,1),(19,13,5,2),(20,13,6,1),(21,13,8,1),(22,14,8,1),(23,15,8,1),(24,15,2,5),(25,16,4,1),(26,16,6,1),(27,17,6,4),(28,17,7,1),(29,17,8,3),(30,18,4,1),(31,18,7,3),(32,18,8,2);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_headers`
--

DROP TABLE IF EXISTS `order_headers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_headers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  `date` date NOT NULL,
  `method` varchar(50) NOT NULL,
  `payment_type` varchar(20) DEFAULT NULL,
  `card_number` varchar(16) DEFAULT NULL,
  `bank_name` varchar(50) DEFAULT NULL,
  `plastic_bag` tinyint NOT NULL,
  `coupon_id` int DEFAULT NULL,
  `information` text,
  `purchase_note` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_headers`
--

LOCK TABLES `order_headers` WRITE;
/*!40000 ALTER TABLE `order_headers` DISABLE KEYS */;
INSERT INTO `order_headers` VALUES (3,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Lasagna                    Rp55.000,00\n1 Iced Coffee                Rp18.000,00\n1 Matcha Latte               Rp21.000,00\n----------------------------------------\nSub Total :                  Rp94.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp4.230,00\n----------------------------------------\nGrand Total :                Rp98.230,00\nCash :                      Rp100.000,00\n----------------------------------------\nChange :                      Rp1.770,00\n========================================\n        Thank you for ordering!         \n========================================'),(4,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Iced Coffee                Rp18.000,00\n1 Mac and Cheese             Rp60.000,00\n----------------------------------------\nSub Total :                  Rp78.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp3.510,00\n----------------------------------------\nGrand Total :                Rp81.510,00\nCash :                      Rp700.000,00\n----------------------------------------\nChange :                    Rp618.490,00\n========================================\n        Thank you for ordering!         \n========================================'),(5,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Iced Coffee                Rp18.000,00\n----------------------------------------\nSub Total :                  Rp18.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                    Rp810,00\n----------------------------------------\nGrand Total :                Rp18.810,00\nCash :                       Rp50.000,00\n----------------------------------------\nChange :                     Rp31.190,00\n========================================\n        Thank you for ordering!         \n========================================'),(6,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Iced Coffee                Rp18.000,00\n----------------------------------------\nSub Total :                  Rp18.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                    Rp810,00\n----------------------------------------\nGrand Total :                Rp18.810,00\nCash :                       Rp20.000,00\n----------------------------------------\nChange :                      Rp1.190,00\n========================================\n        Thank you for ordering!         \n========================================'),(7,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Iced Coffee                Rp18.000,00\n----------------------------------------\nSub Total :                  Rp18.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                    Rp810,00\n----------------------------------------\nGrand Total :                Rp18.810,00\nCash :                       Rp90.000,00\n----------------------------------------\nChange :                     Rp71.190,00\n========================================\n        Thank you for ordering!         \n========================================'),(8,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Lasagna                    Rp55.000,00\n1 Matcha Latte               Rp21.000,00\n----------------------------------------\nSub Total :                  Rp76.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp3.420,00\n----------------------------------------\nGrand Total :                Rp79.420,00\nCash :                       Rp80.000,00\n----------------------------------------\nChange :                        Rp580,00\n========================================\n        Thank you for ordering!         \n========================================'),(9,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 French Fries               Rp20.000,00\n----------------------------------------\nSub Total :                  Rp20.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                    Rp900,00\n----------------------------------------\nGrand Total :                Rp20.900,00\nCash :                      Rp500.000,00\n----------------------------------------\nChange :                    Rp479.100,00\n========================================\n        Thank you for ordering!         \n========================================'),(10,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Matcha Latte               Rp21.000,00\n----------------------------------------\nSub Total :                  Rp21.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                    Rp945,00\n----------------------------------------\nGrand Total :                Rp21.945,00\nCash :                       Rp22.000,00\n----------------------------------------\nChange :                         Rp55,00\n========================================\n        Thank you for ordering!         \n========================================'),(11,1,'2023-05-29','Dine-in','cash',NULL,NULL,0,3,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Cheese Burger              Rp35.000,00\n----------------------------------------\nSub Total :                  Rp35.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp1.575,00\n----------------------------------------\nPrice Cut (50%) :            Rp17.500,00\n----------------------------------------\nGrand Total :                Rp19.075,00\nCash :                       Rp77.777,00\n----------------------------------------\nChange :                     Rp58.702,00\n========================================\n        Thank you for ordering!         \n========================================'),(12,1,'2023-05-29','Delivery','credit','8234238394937283','BRI',1,2,'Nama : Azis Rosyid\nAlamat : Jogja\n\nDelivery via Gojek','========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n2 Cheese Burger              Rp70.000,00\n3 French Fries               Rp60.000,00\n11 Pepperoni Pizza          Rp550.000,00\n5 Mac and Cheese            Rp300.000,00\n1 Iced Coffee                Rp18.000,00\n----------------------------------------\nSub Total :                 Rp998.000,00\nDelivery (20%) :            Rp199.600,00\nTax (4.5%) :                 Rp44.910,00\nPlastic Bag :                   Rp200,00\n----------------------------------------\nGrand Total :             Rp1.242.710,00\nCashback :                   Rp20.000,00\nCredit :                             BRI\n----------------------------------------\nChange :                     Rp20.000,00\n========================================\n        Thank you for ordering!         \n========================================'),(13,1,'2023-05-29','Delivery','cash',NULL,NULL,1,3,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-29\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n2 Cheese Burger              Rp70.000,00\n1 Iced Coffee                Rp18.000,00\n1 Pepperoni Pizza            Rp50.000,00\n----------------------------------------\nSub Total :                 Rp138.000,00\nDelivery (20%) :             Rp27.600,00\nTax (4.5%) :                  Rp6.210,00\nPlastic Bag :                   Rp200,00\n----------------------------------------\nPrice Cut (50%) :            Rp50.000,00\n----------------------------------------\nGrand Total :               Rp122.010,00\nCash :                      Rp200.000,00\n----------------------------------------\nChange :                     Rp77.990,00\n========================================\n        Thank you for ordering!         \n========================================'),(14,1,'2023-05-30','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-30\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Pepperoni Pizza            Rp50.000,00\n----------------------------------------\nSub Total :                  Rp50.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp2.250,00\n----------------------------------------\nGrand Total :                Rp52.250,00\nCash :                       Rp55.000,00\n----------------------------------------\nChange :                      Rp2.750,00\n========================================\n        Thank you for ordering!         \n========================================'),(15,1,'2023-05-30','Delivery','credit','2938427347474777','BRI',1,2,'Name : Azis Rosyid\n\nAlamat : 2003','========================================\n               Azis Cafe                \n========================================\nDate :                        2023-05-30\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 Pepperoni Pizza            Rp50.000,00\n5 Lasagna                   Rp275.000,00\n----------------------------------------\nSub Total :                 Rp325.000,00\nDelivery (20%) :             Rp65.000,00\nTax (4.5%) :                 Rp14.625,00\nPlastic Bag :                   Rp200,00\n----------------------------------------\nGrand Total :               Rp404.825,00\nCashback :                   Rp20.000,00\nCredit :                             BRI\n----------------------------------------\nChange :                     Rp20.000,00\n========================================\n        Thank you for ordering!         \n========================================'),(16,1,'2023-06-01','Dine-in','cash',NULL,NULL,0,NULL,NULL,'========================================\n               Azis Cafe                \n========================================\nDate :                        2023-06-01\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 French Fries               Rp20.000,00\n1 Iced Coffee                Rp18.000,00\n----------------------------------------\nSub Total :                  Rp38.000,00\nDine-in (0%) :                    Rp0,00\nTax (4.5%) :                  Rp1.710,00\n----------------------------------------\nGrand Total :                Rp39.710,00\nCash :                       Rp40.000,00\n----------------------------------------\nChange :                        Rp290,00\n========================================\n        Thank you for ordering!         \n========================================'),(17,1,'2023-06-03','Delivery','cash',NULL,NULL,1,2,'Nama : Azis Rosyid\nAlamat : Jogja\nDeskripsi : Ditaruh di depan teras','========================================\n               Azis Cafe                \n========================================\nDate :                        2023-06-03\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n4 Iced Coffee                Rp72.000,00\n1 Matcha Latte               Rp21.000,00\n3 Pepperoni Pizza           Rp150.000,00\n----------------------------------------\nSub Total :                 Rp243.000,00\nDelivery (20%) :             Rp48.600,00\nTax (4.5%) :                 Rp10.935,00\nPlastic Bag :                   Rp200,00\n----------------------------------------\nGrand Total :               Rp302.735,00\nCashback :                   Rp20.000,00\nCash :                      Rp600.000,00\n----------------------------------------\nChange :                    Rp317.265,00\n========================================\n        Thank you for ordering!         \n========================================'),(18,1,'2023-06-03','Takeaway','credit','2342342343323434','BRI',1,3,'Nama : Budi','========================================\n               Azis Cafe                \n========================================\nDate :                        2023-06-03\nCafe Boss :                  Azis Rosyid\nCashier :                    Azis Rosyid\n----------------------------------------\n1 French Fries               Rp20.000,00\n3 Matcha Latte               Rp63.000,00\n2 Pepperoni Pizza           Rp100.000,00\n----------------------------------------\nSub Total :                 Rp183.000,00\nTakeaway (5%) :               Rp9.150,00\nTax (4.5%) :                  Rp8.235,00\nPlastic Bag :                   Rp200,00\n----------------------------------------\nPrice Cut (50%) :            Rp50.000,00\n----------------------------------------\nGrand Total :               Rp150.585,00\nCredit :                             BRI\n----------------------------------------\nChange :                          Rp0,00\n========================================\n        Thank you for ordering!         \n========================================');
/*!40000 ALTER TABLE `order_headers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-03 14:16:47
