# Users and other schemas for SecureSocial

# --- !Ups

CREATE TABLE `user`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255),
    `create_date` DATETIME NOT NULL,
    UNIQUE KEY `email_uk` (`email`)
);

INSERT INTO `user` VALUES (NULL, 'Chris', 'X', 'email@example.com', NOW());
INSERT INTO `user` VALUES (NULL, 'Mark', 'Y', 'email2@example.com', NOW());

# --- !Downs

DROP TABLE `user`;



