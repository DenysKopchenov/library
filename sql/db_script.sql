CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `publisher` varchar(100) NOT NULL,
  `publishing_date` datetime NOT NULL,
  `amount` int NOT NULL,
  `on_order` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`title`,`author`,`publisher`,`publishing_date`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `books` VALUES (DEFAULT, 'The Da Vinci Code', 'Dan Brown', 'Corgi', '2009-08-28', 5, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'Harry Potter and the Deathly Hallows', 'J. K. Rowling', 'Bloomsbury', '2007-07-14', 10, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'A Thousand Splendid Suns', 'Khaled Hosseini', 'Bloomsbury', '2007-05-22', 3, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'Life of Pi', 'Yann Martel', 'Knopf Canada', '2001-09-11', 2, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'The Fellowship of the Ring', 'J. R. R. Tolkien', 'Allen & Unwin', '1954-07-29', 1, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'The Two Towers', 'J. R. R. Tolkien', 'Allen & Unwin', '1954-11-11', 1, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'The Return of the King', 'J. R. R. Tolkien', 'Allen & Unwin', '1955-10-20', 1, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'Зелёная миля', 'Стивен Кинг', 'АСТ', '1999-05-20', 7, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'Бойцовский клуб', 'Чак Паланик', 'АСТ', '1996-08-17', 7, DEFAULT);
INSERT INTO `books` VALUES (DEFAULT, 'Магия утра', 'Хэл Элрод', 'Манн, Иванов и Фербер', '2016-03-18', 10, DEFAULT);

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45),
  `last_name` varchar(45),
  `email` varchar(50),
  `password` varchar(100),
  `role` varchar(25) NOT NULL DEFAULT 'reader',
  `status` varchar(45) NOT NULL DEFAULT 'active',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `e_mail` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users` VALUES (DEFAULT, 'admin', 'admin', 'admin@mail.com', 'd493e49324a9e63324538d4f9896570e740c3c430d71d1c6ca1ee78080f53695', 'admin', DEFAULT);

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `type` varchar(45) NOT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'pending',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `approved_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expected_return_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actual_return_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`type`,`user_id`,`book_id`,`status`,`actual_return_date`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `users.id_idx` (`user_id`),
  KEY `books.id_idx` (`book_id`),
  CONSTRAINT `book_id` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34252448 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

