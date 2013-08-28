
-- themute and SchoolProject interchangeable
CREATE DATABASE IF NOT EXISTS `themute` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `themute` ;

-- -----------------------------------------------------
-- Table `themute`.`Student`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Student` (
  `StudentID` INT NOT NULL AUTO_INCREMENT ,
  `FirstName` VARCHAR(45) NULL ,
  `LastName` VARCHAR(45) NULL ,
  `Email` VARCHAR(45) NULL ,
  `Password` VARCHAR(45) NULL ,
  PRIMARY KEY (`StudentID`) ,
  UNIQUE INDEX `StudentID_UNIQUE` (`StudentID` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Subject`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Subject` (
  `SubjectID` INT NOT NULL AUTO_INCREMENT ,
  `SubjectName` VARCHAR(45) NULL ,
  PRIMARY KEY (`SubjectID`) ,
  UNIQUE INDEX `SubjectID_UNIQUE` (`SubjectID` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Teacher`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Teacher` (
  `TeacherID` INT NOT NULL AUTO_INCREMENT ,
  `FirstName` VARCHAR(45) NULL ,
  `LastName` VARCHAR(45) NULL ,
  `Email` VARCHAR(45) NULL ,
  `Password` VARCHAR(45) NULL ,
  PRIMARY KEY (`TeacherID`) ,
  UNIQUE INDEX `TeacherID_UNIQUE` (`TeacherID` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Class`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Class` (
  `ClassID` INT NOT NULL AUTO_INCREMENT ,
  `ClassName` VARCHAR(45) NULL ,
  `SubjectID` INT NULL ,
  `TeacherID` INT NULL ,
  PRIMARY KEY (`ClassID`) ,
  INDEX `SubjectID_idx` (`SubjectID` ASC) ,
  INDEX `TeacherID_idx` (`TeacherID` ASC) ,
  CONSTRAINT `SubjectID`
    FOREIGN KEY (`SubjectID` )
    REFERENCES `themute`.`Subject` (`SubjectID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TeacherID`
    FOREIGN KEY (`TeacherID` )
    REFERENCES `themute`.`Teacher` (`TeacherID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`StudentClassRel`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`StudentClassRel` (
  `StudentID` INT NULL ,
  `ClassID` INT NULL ,
  INDEX `ClassID_idx` (`ClassID` ASC) ,
  CONSTRAINT `StudentID`
    FOREIGN KEY (`StudentID` )
    REFERENCES `themute`.`Student` (`StudentID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ClassID`
    FOREIGN KEY (`ClassID` )
    REFERENCES `themute`.`Class` (`ClassID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Assignment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Assignment` (
  `AssignmentID` INT NOT NULL AUTO_INCREMENT ,
  `AssignmentName` VARCHAR(45) NULL ,
  `ClassID` INT NULL ,
  `Question1` TEXT NULL ,
  `Question2` TEXT NULL ,
  `Question3` TEXT NULL ,
  `Question4` TEXT NULL ,
  `Question5` TEXT NULL ,
  PRIMARY KEY (`AssignmentID`) ,
  INDEX `ClassID_idx` (`ClassID` ASC) ,
  UNIQUE INDEX `AssignmentID_UNIQUE` (`AssignmentID` ASC) ,
  CONSTRAINT `ClassID1`
    FOREIGN KEY (`ClassID` )
    REFERENCES `themute`.`Class` (`ClassID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`CompletedAssignment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`CompletedAssignment` (
  `CompletedAssignmentID` INT NOT NULL AUTO_INCREMENT ,
  `StudentID` INT NULL ,
  `AssignmentID` INT NULL ,
  `Answer1` TEXT NULL ,
  `Answer2` TEXT NULL ,
  `Answer3` TEXT NULL ,
  `Answer4` TEXT NULL ,
  `Answer5` TEXT NULL ,
  PRIMARY KEY (`CompletedAssignmentID`) ,
  INDEX `StudentID_idx` (`StudentID` ASC) ,
  INDEX `AssignmentID_idx` (`AssignmentID` ASC) ,
  UNIQUE INDEX `CompletedAssignmentID_UNIQUE` (`CompletedAssignmentID` ASC) ,
  CONSTRAINT `StudentID1`
    FOREIGN KEY (`StudentID` )
    REFERENCES `themute`.`Student` (`StudentID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `AssignmentID`
    FOREIGN KEY (`AssignmentID` )
    REFERENCES `themute`.`Assignment` (`AssignmentID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`GradedAssignment`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`GradedAssignment` (
  `GradedAssignmentID` INT NOT NULL AUTO_INCREMENT ,
  `CompletedAssignmentID` INT NULL ,
  `CheckOff1` INT NULL ,
  `CheckOff2` INT NULL ,
  `CheckOff3` INT NULL ,
  `CheckOff4` INT NULL ,
  `CheckOff5` INT NULL ,
  PRIMARY KEY (`GradedAssignmentID`) ,
  INDEX `CompletedAssignmentID_idx` (`CompletedAssignmentID` ASC) ,
  CONSTRAINT `CompletedAssignmentID`
    FOREIGN KEY (`CompletedAssignmentID` )
    REFERENCES `themute`.`CompletedAssignment` (`CompletedAssignmentID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Test`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Test` (
  `TestID` INT NOT NULL AUTO_INCREMENT ,
  `TestName` VARCHAR(45) NULL ,
  `ClassID` INT NULL ,
  `Question1` TEXT NULL ,
  `Question2` TEXT NULL ,
  `Question3` TEXT NULL ,
  `Question4` TEXT NULL ,
  `Question5` TEXT NULL ,
  PRIMARY KEY (`TestID`) ,
  INDEX `TestClassID_idx` (`ClassID` ASC) ,
  CONSTRAINT `TestClassID`
    FOREIGN KEY (`ClassID` )
    REFERENCES `themute`.`Class` (`ClassID` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `themute`.`Message`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `themute`.`Message` (
  `MessageID` INT NOT NULL AUTO_INCREMENT ,
  `MessageName` VARCHAR(45) NULL ,
  `SenderUserType` INT NULL ,
  `SenderUserID` INT NULL ,
  `ReceiverUserType` INT NULL ,
  `ReceiverUserID` INT NULL ,
  `Message` TEXT NULL ,
  `DateCreated` DATE NULL ,
  PRIMARY KEY (`MessageID`) )
ENGINE = InnoDB;

USE `themute` ;

