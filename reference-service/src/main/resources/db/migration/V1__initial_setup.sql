CREATE TABLE `AssessmentType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `College` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `abbreviation` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`abbreviation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Country` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `countryNumber` varchar(255) NOT NULL DEFAULT '',
  `nationality` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`countryNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `CurriculumSubType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `DBC` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dbc` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `abbr` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`dbc`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `EthnicOrigin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `FundingIssue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `FundingType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `GdcStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Gender` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `GmcStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Grade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `abbreviation` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `trainingGrade` bit(1) NOT NULL,
  `postGrade` bit(1) NOT NULL,
  `placementGrade` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`abbreviation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `InactiveReason` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `LeavingDestination` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `LocalOffice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `abbreviation` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`abbreviation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `MaritalStatus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `MedicalSchool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Nationality` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `countryNumber` varchar(255) NOT NULL DEFAULT '',
  `nationality` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `PlacementType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ProgrammeMembershipType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `RecordType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ReligiousBelief` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Settled` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `SexualOrientation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Trust` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `localOffice` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `trustKnownAs` varchar(255) DEFAULT NULL,
  `trustName` varchar(255) DEFAULT NULL,
  `trustNumber` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `postCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Site` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `siteCode` varchar(255) NOT NULL,
  `trustCode` varchar(255) NOT NULL,
  `localOffice` varchar(255) DEFAULT NULL,
  `siteName` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `postCode` varchar(255) DEFAULT NULL,
  `siteKnownAs` varchar(255) DEFAULT NULL,
  `siteNumber` varchar(255) DEFAULT NULL,
  `organisationalUnit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`siteCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TariffRate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `gradeAbbreviation` varchar(255) DEFAULT NULL,
  `tariffRate` varchar(255) DEFAULT NULL,
  `tariffRateFringe` varchar(255) DEFAULT NULL,
  `tariffRateLondon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `Title` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `TrainingNumberType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
