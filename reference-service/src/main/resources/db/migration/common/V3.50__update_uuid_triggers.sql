DROP TRIGGER IF EXISTS before_insert_AssessmentType;
CREATE TRIGGER before_insert_AssessmentType
BEFORE INSERT ON `AssessmentType`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_College;
CREATE TRIGGER before_insert_College
BEFORE INSERT ON `College`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Country;
CREATE TRIGGER before_insert_Country
BEFORE INSERT ON `Country`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_CurriculumSubType;
CREATE TRIGGER before_insert_CurriculumSubType
BEFORE INSERT ON `CurriculumSubType`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_EthnicOrigin;
CREATE TRIGGER before_insert_EthnicOrigin
BEFORE INSERT ON `EthnicOrigin`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_FundingType;
CREATE TRIGGER before_insert_FundingType
BEFORE INSERT ON `FundingType`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_GdcStatus;
CREATE TRIGGER before_insert_GdcStatus
BEFORE INSERT ON `GdcStatus`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Gender;
CREATE TRIGGER before_insert_Gender
BEFORE INSERT ON `Gender`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_GmcStatus;
CREATE TRIGGER before_insert_GmcStatus
BEFORE INSERT ON `GmcStatus`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());


DROP TRIGGER IF EXISTS before_insert_Grade;
CREATE TRIGGER before_insert_Grade
BEFORE INSERT ON `Grade`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_LocalOffice;
CREATE TRIGGER before_insert_LocalOffice
BEFORE INSERT ON `LocalOffice`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_MaritalStatus;
CREATE TRIGGER before_insert_MaritalStatus
BEFORE INSERT ON `MaritalStatus`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_MedicalSchool;
CREATE TRIGGER before_insert_MedicalSchool
BEFORE INSERT ON `MedicalSchool`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Nationality;
CREATE TRIGGER before_insert_Nationality
BEFORE INSERT ON `Nationality`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_PermitToWork;
CREATE TRIGGER before_insert_PermitToWork
BEFORE INSERT ON `PermitToWork`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_PlacementType;
CREATE TRIGGER before_insert_PlacementType
BEFORE INSERT ON `PlacementType`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_ProgrammeMembershipType;
CREATE TRIGGER before_insert_ProgrammeMembershipType
BEFORE INSERT ON `ProgrammeMembershipType`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_ReligiousBelief;
CREATE TRIGGER before_insert_ReligiousBelief
BEFORE INSERT ON `ReligiousBelief`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Role;
CREATE TRIGGER before_insert_Role
BEFORE INSERT ON `Role`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Settled;
CREATE TRIGGER before_insert_Settled
BEFORE INSERT ON `Settled`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_SexualOrientation;
CREATE TRIGGER before_insert_SexualOrientation
BEFORE INSERT ON `SexualOrientation`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());

DROP TRIGGER IF EXISTS before_insert_Title;
CREATE TRIGGER before_insert_Title
BEFORE INSERT ON `Title`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());
