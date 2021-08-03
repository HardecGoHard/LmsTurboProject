CREATE TABLE `Users` (
	`user_id` INT NOT NULL AUTO_INCREMENT,
	`username` varchar(255) NOT NULL AUTO_INCREMENT,
	`password` varchar(30) NOT NULL AUTO_INCREMENT,
	`email` VARCHAR(255) NOT NULL,
	`fullname` VARCHAR(255) NOT NULL AUTO_INCREMENT,
	`avatar` blob NOT NULL,
	`registration_date` DATETIME NOT NULL,
	`admin` BOOLEAN NOT NULL,
	`delete_time` DATETIME NOT NULL,
	`id_delete_author` INT NOT NULL,
	`update_time` DATETIME NOT NULL,
	`id_update_author` INT NOT NULL,
	PRIMARY KEY (`user_id`)
);

CREATE TABLE `users_courses_static` (
	`user_id` INT NOT NULL,
	`course_id` INT NOT NULL,
	`score` INT NOT NULL
);

CREATE TABLE `courses` (
	`course_id` INT NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(255) NOT NULL,
	`description` TEXT NOT NULL,
	`complete_time` TIME NOT NULL,
	`rating` DECIMAL NOT NULL,
	`tag` VARCHAR(255) NOT NULL,
	`category_id` INT NOT NULL,
	`delete_time` DATETIME NOT NULL,
	`delete_author_id` INT NOT NULL,
	`update_time` DATETIME NOT NULL,
	`update_author_id` INT NOT NULL,
	`creation_time` DATETIME NOT NULL,
	`creation_author_id` DATETIME NOT NULL,
	PRIMARY KEY (`course_id`)
);

CREATE TABLE `Category` (
	`category_id` INT NOT NULL AUTO_INCREMENT,
	`category` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`category_id`)
);

CREATE TABLE `modules` (
	`module_id` INT NOT NULL AUTO_INCREMENT,
	`course_id` INT NOT NULL,
	`tittle` VARCHAR(255) NOT NULL,
	`description` VARCHAR(255) NOT NULL,
	`delete_time` DATETIME NOT NULL,
	`delete_author_id` INT NOT NULL,
	`update_time` DATETIME NOT NULL,
	`update_author_id` INT NOT NULL,
	`create_time` DATETIME NOT NULL,
	`create_author_id` INT NOT NULL,
	PRIMARY KEY (`module_id`)
);

CREATE TABLE `topic` (
	`topic_id` INT NOT NULL AUTO_INCREMENT,
	`module_id` INT NOT NULL,
	`title` VARCHAR(255) NOT NULL,
	`description` VARCHAR(255) NOT NULL,
	`delete_time` DATETIME NOT NULL,
	`update_time` DATETIME NOT NULL,
	`save_time` DATETIME NOT NULL,
	`save_author_id` INT NOT NULL,
	`delete_author_id` INT NOT NULL,
	`update_author_id` INT NOT NULL,
	`content_id` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`topic_id`)
);

CREATE TABLE `tasks` (
	`task_id` INT NOT NULL,
	`topic_id` INT NOT NULL,
	`any_tasks` VARCHAR(255) NOT NULL,
	PRIMARY KEY (`task_id`)
);

CREATE TABLE `content` (
	`content_id` INT NOT NULL,
	`content` blob NOT NULL,
	`type` VARCHAR(255) NOT NULL
);

ALTER TABLE `Users` ADD CONSTRAINT `Users_fk0` FOREIGN KEY (`password`) REFERENCES ``(``);

ALTER TABLE `Users` ADD CONSTRAINT `Users_fk1` FOREIGN KEY (`fullname`) REFERENCES ``(``);

ALTER TABLE `users_courses_static` ADD CONSTRAINT `users_courses_static_fk0` FOREIGN KEY (`user_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `users_courses_static` ADD CONSTRAINT `users_courses_static_fk1` FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`);

ALTER TABLE `courses` ADD CONSTRAINT `courses_fk0` FOREIGN KEY (`category_id`) REFERENCES `Category`(`category_id`);

ALTER TABLE `courses` ADD CONSTRAINT `courses_fk1` FOREIGN KEY (`delete_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `courses` ADD CONSTRAINT `courses_fk2` FOREIGN KEY (`update_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `courses` ADD CONSTRAINT `courses_fk3` FOREIGN KEY (`creation_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `modules` ADD CONSTRAINT `modules_fk0` FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`);

ALTER TABLE `modules` ADD CONSTRAINT `modules_fk1` FOREIGN KEY (`delete_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `modules` ADD CONSTRAINT `modules_fk2` FOREIGN KEY (`update_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `modules` ADD CONSTRAINT `modules_fk3` FOREIGN KEY (`create_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `topic` ADD CONSTRAINT `topic_fk0` FOREIGN KEY (`module_id`) REFERENCES `modules`(`module_id`);

ALTER TABLE `topic` ADD CONSTRAINT `topic_fk1` FOREIGN KEY (`save_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `topic` ADD CONSTRAINT `topic_fk2` FOREIGN KEY (`delete_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `topic` ADD CONSTRAINT `topic_fk3` FOREIGN KEY (`update_author_id`) REFERENCES `Users`(`user_id`);

ALTER TABLE `topic` ADD CONSTRAINT `topic_fk4` FOREIGN KEY (`content_id`) REFERENCES ``(``);

ALTER TABLE `tasks` ADD CONSTRAINT `tasks_fk0` FOREIGN KEY (`topic_id`) REFERENCES `topic`(`topic_id`);

ALTER TABLE `content` ADD CONSTRAINT `content_fk0` FOREIGN KEY (`content_id`) REFERENCES `topic`(`content_id`);









