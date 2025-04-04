-- Host: localhost    Database: library_db
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `book_id` int NOT NULL AUTO_INCREMENT,
  `isbn` varchar(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(100) NOT NULL,
  `genre` varchar(50) NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  `available_quantity` int NOT NULL DEFAULT '1',
  `publication_year` int DEFAULT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `description` text,
  `added_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT 'AVAILABLE',
  PRIMARY KEY (`book_id`),
  UNIQUE KEY `isbn` (`isbn`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (1,'978-0061120084','To Kill a Mockingbird','Harper Lee','Fiction',5,4,1960,'J. B. Lippincott & Co.','Classic novel about racial injustice','2025-03-28 01:14:25','AVAILABLE'),(2,'978-0451524935','1984','George Orwell','Dystopian',3,3,1949,'Secker & Warburg','Dystopian social science fiction','2025-03-28 01:14:25','AVAILABLE'),(3,'978-0743273565','The Great Gatsby','F. Scott Fitzgerald','Classic',4,4,1925,'Charles Scribner\'s Sons','Story of wealth and love in the 1920s','2025-03-28 01:14:25','AVAILABLE'),(4,'978-0316769488','The Catcher in the Rye','J.D. Salinger','Coming-of-age',2,2,1951,'Little, Brown and Company','Novel about teenage alienation','2025-03-28 01:14:25','LOW_IN_STOCK'),(5,'978-0553103540','A Game of Thrones','George R.R. Martin','Fantasy',4,4,1996,'Bantam Spectra','First book in A Song of Ice and Fire series','2025-03-28 01:14:25','AVAILABLE'),(6,'4567','Test Book','George Bush','Fantasy',3,3,2019,'Macarthur','I love this book','2025-03-29 22:48:24','AVAILABLE'),(11,'ee','ee','Changed','Non-Fiction',1,1,2023,'ee','','2025-03-29 22:49:24','LOW_IN_STOCK'),(14,'123435555','dndkf','ekldnd','Mystery',5,5,2023,'dwd','rwqrr','2025-03-29 22:53:22','AVAILABLE'),(15,'edfef34','qrrfdff','frftget4','Romance',5,5,2023,'rfqf44','fqf4r','2025-03-29 22:55:37','AVAILABLE'),(17,'rttfg','wrcv','fdewff','Mystery',4,4,2023,'feff','feff','2025-03-29 22:57:28','AVAILABLE'),(19,'978-0061120085','The Alchemist','Paulo Coelho','Fiction',5,5,1988,'HarperOne','A mystical story of self-discovery','2025-04-03 23:11:51','AVAILABLE'),(20,'978-0743477109','Romeo and Juliet','William Shakespeare','Fiction',4,4,1597,'Simon & Schuster','Classic tragic love story','2025-04-03 23:12:46','AVAILABLE'),(21,'978-0140283334','Lord of the Flies','William Golding','Fiction',3,3,1954,'Penguin Books','Allegorical novel about human nature','2025-04-03 23:12:46','AVAILABLE'),(22,'978-0307276650','The Da Vinci Code','Dan Brown','Fiction',5,5,2003,'Doubleday','Mystery thriller with religious themes','2025-04-03 23:12:46','AVAILABLE'),(23,'978-0553418026','The Martian','Andy Weir','Science Fiction',3,3,2011,'Crown Publishing','Survival story on Mars','2025-04-03 23:12:46','AVAILABLE'),(24,'978-0765377067','The Three-Body Problem','Liu Cixin','Science Fiction',2,2,2008,'Tor Books','Chinese science fiction novel','2025-04-03 23:12:46','AVAILABLE'),(25,'978-0316097763','Ready Player One','Ernest Cline','Science Fiction',4,4,2011,'Crown Publishing','Virtual reality adventure','2025-04-03 23:12:46','AVAILABLE'),(26,'978-0441013593','Dune','Frank Herbert','Science Fiction',3,3,1965,'Chilton Books','Epic science fiction novel','2025-04-03 23:12:46','AVAILABLE'),(27,'978-0345391803','The Hitchhiker\'s Guide to the Galaxy','Douglas Adams','Science Fiction',5,5,1979,'Pan Books','Comedic science fiction','2025-04-03 23:12:46','AVAILABLE'),(28,'978-0062073501','The Girl with the Dragon Tattoo','Stieg Larsson','Mystery',4,4,2005,'Norstedts Förlag','Crime thriller','2025-04-03 23:12:46','AVAILABLE'),(29,'978-0316015843','The Lincoln Lawyer','Michael Connelly','Mystery',3,3,2005,'Little, Brown','Legal thriller','2025-04-03 23:12:46','AVAILABLE'),(30,'978-0062255655','The Silent Patient','Alex Michaelides','Mystery',5,4,2019,'Celadon Books','Psychological thriller','2025-04-03 23:12:46','AVAILABLE'),(31,'978-1501175466','The Woman in the Window','A.J. Finn','Mystery',4,4,2018,'William Morrow','Psychological thriller','2025-04-03 23:12:46','AVAILABLE'),(32,'978-0062073563','The Girl Who Played with Fire','Stieg Larsson','Mystery',3,3,2006,'Norstedts Förlag','Second book in Millennium series','2025-04-03 23:12:46','AVAILABLE'),(33,'978-0062316110','Sapiens: A Brief History of Humankind','Yuval Noah Harari','Non-Fiction',5,5,2011,'Harper','History of human evolution','2025-04-03 23:12:46','AVAILABLE'),(34,'978-0062457715','The Subtle Art of Not Giving a F*ck','Mark Manson','Non-Fiction',4,4,2016,'HarperOne','Self-help book','2025-04-03 23:12:46','AVAILABLE'),(35,'978-0062225672','Quiet: The Power of Introverts','Susan Cain','Non-Fiction',3,3,2012,'Crown Publishing','Psychology of introversion','2025-04-03 23:12:46','AVAILABLE'),(36,'978-0062315007','The Power of Habit','Charles Duhigg','Non-Fiction',4,3,2012,'Random House','Science of habit formation','2025-04-03 23:12:46','AVAILABLE'),(37,'978-0062457716','Atomic Habits','James Clear','Non-Fiction',5,5,2018,'Avery','Building good habits','2025-04-03 23:12:46','AVAILABLE'),(38,'978-0062457718','The Notebook','Nicholas Sparks','Romance',5,5,1996,'Warner Books','Classic love story','2025-04-03 23:12:46','AVAILABLE'),(39,'978-0062315009','Pride and Prejudice','Jane Austen','Romance',4,4,1813,'T. Egerton','Classic romance novel','2025-04-03 23:12:46','AVAILABLE'),(40,'978-0062457719','Me Before You','Jojo Moyes','Romance',3,2,2012,'Pamela Dorman Books','Contemporary romance','2025-04-03 23:12:46','LOW_IN_STOCK'),(41,'978-0062316113','Outlander','Diana Gabaldon','Romance',4,3,1991,'Delacorte Press','Historical time-travel romance','2025-04-03 23:12:46','AVAILABLE');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrow_records`
--

DROP TABLE IF EXISTS `borrow_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrow_records` (
  `borrow_id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `status` enum('BORROWED','RETURNED','OVERDUE') NOT NULL DEFAULT 'BORROWED',
  PRIMARY KEY (`borrow_id`),
  KEY `fk_book` (`book_id`),
  KEY `fk_user` (`user_id`),
  CONSTRAINT `borrow_records_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`),
  CONSTRAINT `borrow_records_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `fk_book` FOREIGN KEY (`book_id`) REFERENCES `books` (`book_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- data for table `borrow_records`
--

LOCK TABLES `borrow_records` WRITE;
/*!40000 ALTER TABLE `borrow_records` DISABLE KEYS */;
INSERT INTO `borrow_records` VALUES (1,1,6,'2023-01-10','2023-01-24','2023-01-22','RETURNED'),(2,2,7,'2023-02-15','2023-03-01',NULL,'BORROWED'),(3,3,8,'2023-02-20','2023-03-06',NULL,'BORROWED'),(4,4,9,'2023-03-01','2023-03-15',NULL,'OVERDUE'),(5,2,6,'2025-04-01','2025-05-01','2025-04-03','RETURNED'),(6,1,6,'2025-04-01','2025-05-01',NULL,'BORROWED'),(7,41,6,'2025-04-03','2025-04-17',NULL,'BORROWED'),(8,4,6,'2025-04-03','2025-04-17','2025-04-03','RETURNED'),(9,32,6,'2025-04-03','2025-04-17','2025-04-03','RETURNED'),(10,36,6,'2025-04-03','2025-04-17',NULL,'BORROWED'),(11,25,6,'2025-04-03','2025-04-17','2025-04-03','RETURNED'),(12,31,6,'2025-04-03','2025-04-17','2025-04-03','RETURNED'),(13,30,6,'2025-04-03','2025-04-17',NULL,'BORROWED'),(14,40,6,'2025-04-03','2025-04-17',NULL,'BORROWED');
/*!40000 ALTER TABLE `borrow_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fines`
--

DROP TABLE IF EXISTS `fines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fines` (
  `fine_id` int NOT NULL AUTO_INCREMENT,
  `borrow_id` int NOT NULL,
  `user_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `reason` enum('OVERDUE','DAMAGE','LOST') DEFAULT 'OVERDUE',
  `status` enum('PENDING','PAID','WAIVED') DEFAULT 'PENDING',
  `issued_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `paid_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`fine_id`),
  KEY `borrow_id` (`borrow_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `fines_ibfk_1` FOREIGN KEY (`borrow_id`) REFERENCES `borrow_records` (`borrow_id`),
  CONSTRAINT `fines_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- data for table `fines`
--

LOCK TABLES `fines` WRITE;
/*!40000 ALTER TABLE `fines` DISABLE KEYS */;
INSERT INTO `fines` VALUES (1,1,6,2.50,'DAMAGE','PAID','2025-03-28 01:14:27',NULL),(2,4,9,5.00,'OVERDUE','PENDING','2025-03-28 01:14:27',NULL);
/*!40000 ALTER TABLE `fines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `user_type` enum('ADMIN','LIBRARIAN','MEMBER') NOT NULL,
  `member_type` enum('STUDENT','FACULTY') DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','ocSD7t3USVv5MPXeY2vUjgB4rKlxcSV4SVfWN82TARJDZi8zDNJmUCKoJIQW7ci0','System','Admin','admin@library.com','555-0001','ADMIN',NULL,'2025-03-28 01:14:25'),(2,'jsmith','admin456','John','Smith','jsmith@library.com','555-0002','ADMIN',NULL,'2025-03-28 01:14:25'),(3,'lib1','i7Fr3AB5wdpzAYxdbfDVgylTd9rPXxKwUkTWPr903yWrT8UB4u/1q/4roLthrJhh','Sarah','Johnson','sjohnson@library.com','555-0101','LIBRARIAN',NULL,'2025-03-28 01:14:25'),(4,'lib2','lib456','Michael','Brown','mbrown@library.com','555-0102','LIBRARIAN',NULL,'2025-03-28 01:14:25'),(5,'lib3','uxsljp+zCKaS0GCw2SMg6ZzgS+Tu4tq1K75upWB/gAwd8Kml/bKOuKmHKq+E4Kd0','Emil','Davis','edavis@library.com','555-0103','LIBRARIAN',NULL,'2025-03-28 01:14:25'),(6,'stu1','BeTv2+vpolE1KlAZoU06a2N4CT+YSKdGa3Flv6TU752JKBouoDfKnMyJR8fbUzfW','Alex','Wilson','awilson@university.edu','555-0201','MEMBER','STUDENT','2025-03-28 01:14:25'),(7,'stu2','student2','Jessica','Lee','jlee@university.edu','555-0202','MEMBER','STUDENT','2025-03-28 01:14:25'),(8,'stu3','student3','Daniel','Miller','dmiller@university.edu','555-0203','MEMBER','STUDENT','2025-03-28 01:14:25'),(9,'stu4','student4','Emma','Garcia','egarcia@university.edu','555-0204','MEMBER','STUDENT','2025-03-28 01:14:25'),(10,'stu5','student5','Noah','Rodriguez','nrodriguez@university.edu','555-0205','MEMBER','STUDENT','2025-03-28 01:14:25'),(11,'fac1','faculty1','Robert','Johnson','rjohnson@university.edu','555-0301','MEMBER','FACULTY','2025-03-28 01:14:25'),(12,'fac2','faculty2','Jennifer','Smith','jsmith@university.edu','555-0302','MEMBER','FACULTY','2025-03-28 01:14:25'),(13,'fac3','faculty3','David','Williams','dwilliams@university.edu','555-0303','MEMBER','FACULTY','2025-03-28 01:14:25'),(14,'koko','iUjryGVnaeB1/QhuaExqVFycHEjJ7cuVKAdJniJcMjst8WVCAxsbvo++Pqy6SSdh','kosy','bone','abonek@yahoo.com','6758456789','MEMBER','STUDENT','2025-03-29 13:51:27'),(15,'david','hzPxHxmeY91J9nUnFZTFGR9MGDOErfhmmcBN3WDCIDeOlw0MGGRxVkfjJVojMK9H','David','Ohigbai','dave@yahoo.com','46272637847','MEMBER','STUDENT','2025-03-29 14:24:00'),(16,'myName','AUukW7uVUQNw1YM16v5u/S92DMLTPwrgZ9fhG//Rz7bgP1jqr5Jrm4tqPHlB6y4X','kay','Charles','asdfe@yaoo.com','34245656','MEMBER','FACULTY','2025-03-30 20:54:01'),(17,'dave','jcazz43m0CZ1uD+Z9+lugdJMqoACblG696yh9fRO0ZgRHP2T0Eww0JoXxVmB+eeT','Daver','Admin','nan@yahoo.com','32455322','MEMBER','FACULTY','2025-03-30 20:55:41'),(20,'kok3','123456','koko','Bone','jsond@yahoi.com','23455656','MEMBER','STUDENT','2025-04-01 16:02:08');
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