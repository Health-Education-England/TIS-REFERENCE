ALTER TABLE `College`
ADD COLUMN uuid varchar(36);

UPDATE `College` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_College
BEFORE INSERT ON `College`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `College` (uuid);

ALTER TABLE `Country`
ADD COLUMN uuid varchar(36);

UPDATE `Country` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Country
BEFORE INSERT ON `Country`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Country` (uuid);

ALTER TABLE `CurriculumSubType`
ADD COLUMN uuid varchar(36);

UPDATE `CurriculumSubType` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_CurriculumSubType
BEFORE INSERT ON `CurriculumSubType`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `CurriculumSubType` (uuid);

ALTER TABLE `EthnicOrigin`
ADD COLUMN uuid varchar(36);

UPDATE `EthnicOrigin` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_EthnicOrigin
BEFORE INSERT ON `EthnicOrigin`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `EthnicOrigin` (uuid);

ALTER TABLE `FundingType`
ADD COLUMN uuid varchar(36);

UPDATE `FundingType` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_FundingType
BEFORE INSERT ON `FundingType`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `FundingType` (uuid);

ALTER TABLE `GdcStatus`
ADD COLUMN uuid varchar(36);

UPDATE `GdcStatus` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_GdcStatus
BEFORE INSERT ON `GdcStatus`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `GdcStatus` (uuid);

ALTER TABLE `Gender`
ADD COLUMN uuid varchar(36);

UPDATE `Gender` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Gender
BEFORE INSERT ON `Gender`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Gender` (uuid);

ALTER TABLE `GmcStatus`
ADD COLUMN uuid varchar(36);

UPDATE `GmcStatus` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_GmcStatus
BEFORE INSERT ON `GmcStatus`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `GmcStatus` (uuid);

ALTER TABLE `Grade`
ADD COLUMN uuid varchar(36);

UPDATE `Grade` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Grade
BEFORE INSERT ON `Grade`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Grade` (uuid);

ALTER TABLE `LocalOffice`
ADD COLUMN uuid varchar(36);

UPDATE `LocalOffice` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_LocalOffice
BEFORE INSERT ON `LocalOffice`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `LocalOffice` (uuid);

ALTER TABLE `MaritalStatus`
ADD COLUMN uuid varchar(36);

UPDATE `MaritalStatus` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_MaritalStatus
BEFORE INSERT ON `MaritalStatus`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `MaritalStatus` (uuid);

ALTER TABLE `MedicalSchool`
ADD COLUMN uuid varchar(36);

UPDATE `MedicalSchool` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_MedicalSchool
BEFORE INSERT ON `MedicalSchool`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `MedicalSchool` (uuid);

ALTER TABLE `Nationality`
ADD COLUMN uuid varchar(36);

UPDATE `Nationality` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Nationality
BEFORE INSERT ON `Nationality`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Nationality` (uuid);

ALTER TABLE `PermitToWork`
ADD COLUMN uuid varchar(36);

UPDATE `PermitToWork` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_PermitToWork
BEFORE INSERT ON `PermitToWork`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `PermitToWork` (uuid);

ALTER TABLE `PlacementType`
ADD COLUMN uuid varchar(36);

UPDATE `PlacementType` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_PlacementType
BEFORE INSERT ON `PlacementType`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `PlacementType` (uuid);

ALTER TABLE `ProgrammeMembershipType`
ADD COLUMN uuid varchar(36);

UPDATE `ProgrammeMembershipType` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_ProgrammeMembershipType
BEFORE INSERT ON `ProgrammeMembershipType`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `ProgrammeMembershipType` (uuid);

ALTER TABLE `ReligiousBelief`
ADD COLUMN uuid varchar(36);

UPDATE `ReligiousBelief` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_ReligiousBelief
BEFORE INSERT ON `ReligiousBelief`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `ReligiousBelief` (uuid);

ALTER TABLE `Role`
ADD COLUMN uuid varchar(36);

UPDATE `Role` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Role
BEFORE INSERT ON `Role`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Role` (uuid);

ALTER TABLE `Settled`
ADD COLUMN uuid varchar(36);

UPDATE `Settled` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Settled
BEFORE INSERT ON `Settled`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Settled` (uuid);

ALTER TABLE `SexualOrientation`
ADD COLUMN uuid varchar(36);

UPDATE `SexualOrientation` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_SexualOrientation
BEFORE INSERT ON `SexualOrientation`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `SexualOrientation` (uuid);

ALTER TABLE `Title`
ADD COLUMN uuid varchar(36);

UPDATE `Title` SET uuid =(SELECT uuid());
CREATE TRIGGER before_insert_Title
BEFORE INSERT ON `Title`
FOR EACH ROW
SET new.uuid = uuid();

CREATE UNIQUE index idx_uuid_unique on `Title` (uuid);