ALTER TABLE `LocalOffice`
ADD COLUMN `entityId` bigint(20) NOT NULL AFTER `name`;

UPDATE `LocalOffice`
SET entityId = 1;

INSERT INTO `LocalOffice`
(`abbreviation`, `name`, `entityId`, `status`, `postAbbreviation`)
VALUES
('NIMDTA', 'Northern Ireland Medical and Dental Training Agency', 2, 'CURRENT', 'MDTA');