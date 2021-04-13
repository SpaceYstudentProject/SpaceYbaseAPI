-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema spaceYdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema spaceYdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `spaceYdb` DEFAULT CHARACTER SET utf8 ;
USE `spaceYdb` ;

-- -----------------------------------------------------
-- Table `spaceYdb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`users` (
  `id_user` BIGINT(15) NOT NULL,
  `login` VARCHAR(60) NOT NULL,
  `email` VARCHAR(80) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `id_user_UNIQUE` (`id_user` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`roles` (
  `id_role` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_role`),
  UNIQUE INDEX `id_role_UNIQUE` (`id_role` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`user_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`user_roles` (
  `id_user_role` INT NOT NULL AUTO_INCREMENT,
  `roles_id_role` INT NOT NULL,
  `users_id_user` BIGINT(15) NOT NULL,
  PRIMARY KEY (`id_user_role`),
  UNIQUE INDEX `id_role_UNIQUE` (`id_user_role` ASC),
  INDEX `fk_user_roles_roles1_idx` (`roles_id_role` ASC),
  INDEX `fk_user_roles_users1_idx` (`users_id_user` ASC),
  CONSTRAINT `fk_user_roles_roles1`
    FOREIGN KEY (`roles_id_role`)
    REFERENCES `spaceYdb`.`roles` (`id_role`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_roles_users1`
    FOREIGN KEY (`users_id_user`)
    REFERENCES `spaceYdb`.`users` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`permission` (
  `id_permission` INT NOT NULL AUTO_INCREMENT,
  `permissions` VARCHAR(255) NOT NULL,
  `roles_id_role` INT NOT NULL,
  PRIMARY KEY (`id_permission`),
  UNIQUE INDEX `id_permission_UNIQUE` (`id_permission` ASC),
  INDEX `fk_permission_roles_idx` (`roles_id_role` ASC),
  CONSTRAINT `fk_permission_roles`
    FOREIGN KEY (`roles_id_role`)
    REFERENCES `spaceYdb`.`roles` (`id_role`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`tokens` (
  `id_token` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL,
  `expire_at` DATE NOT NULL,
  `issued_at` DATE NOT NULL,
  `users_id_user` BIGINT(15) NOT NULL,
  PRIMARY KEY (`id_token`),
  UNIQUE INDEX `id_token_UNIQUE` (`id_token` ASC),
  INDEX `fk_tokens_users1_idx` (`users_id_user` ASC),
  CONSTRAINT `fk_tokens_users1`
    FOREIGN KEY (`users_id_user`)
    REFERENCES `spaceYdb`.`users` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`launches`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`launches` (
  `id_launch` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `id_launch_api` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_launch`),
  UNIQUE INDEX `id_launch_UNIQUE` (`id_launch` ASC),
  UNIQUE INDEX `id_launch_api_UNIQUE` (`id_launch_api` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`launch_interactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`launch_interactions` (
  `id_launch_interaction` BIGINT(15) NOT NULL,
  `subscribed` TINYINT NULL,
  `success_prediction` TINYINT NULL,
  `failure_prediction` TINYINT NULL,
  `users_id_user` BIGINT(15) NOT NULL,
  `launches_id_launch` BIGINT(15) NOT NULL,
  PRIMARY KEY (`id_launch_interaction`),
  UNIQUE INDEX `id_launch_interaction_UNIQUE` (`id_launch_interaction` ASC),
  INDEX `fk_launch_interactions_users1_idx` (`users_id_user` ASC),
  INDEX `fk_launch_interactions_launches1_idx` (`launches_id_launch` ASC),
  CONSTRAINT `fk_launch_interactions_users1`
    FOREIGN KEY (`users_id_user`)
    REFERENCES `spaceYdb`.`users` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_launch_interactions_launches1`
    FOREIGN KEY (`launches_id_launch`)
    REFERENCES `spaceYdb`.`launches` (`id_launch`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `spaceYdb`.`comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `spaceYdb`.`comments` (
  `id_comment` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `value` VARCHAR(255) NOT NULL,
  `users_id_user` BIGINT(15) NOT NULL,
  `launches_id_launch` BIGINT(15) NOT NULL,
  PRIMARY KEY (`id_comment`),
  UNIQUE INDEX `id_comment_UNIQUE` (`id_comment` ASC),
  INDEX `fk_comments_users1_idx` (`users_id_user` ASC),
  INDEX `fk_comments_launches1_idx` (`launches_id_launch` ASC),
  CONSTRAINT `fk_comments_users1`
    FOREIGN KEY (`users_id_user`)
    REFERENCES `spaceYdb`.`users` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_launches1`
    FOREIGN KEY (`launches_id_launch`)
    REFERENCES `spaceYdb`.`launches` (`id_launch`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
