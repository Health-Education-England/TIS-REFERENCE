UPDATE `Trust`
SET `status` = 'CURRENT'
WHERE `status` = 'Current';

UPDATE `Trust`
SET `status` = 'INACTIVE'
WHERE `status` = 'Inactive';

ALTER TABLE `Grade`
ADD COLUMN `status` varchar(255) DEFAULT NULL;

ALTER TABLE `LocalOffice`
ADD COLUMN `status` varchar(255) DEFAULT NULL;

ALTER TABLE `MedicalSchool`
ADD COLUMN `status` varchar(255) DEFAULT NULL;

ALTER TABLE `PlacementType`
ADD COLUMN `status` varchar(255) DEFAULT NULL;

ALTER TABLE `ProgrammeMembershipType`
ADD COLUMN `status` varchar(255) DEFAULT NULL;

ALTER TABLE `Site`
ADD COLUMN `status` varchar(255) DEFAULT NULL;
