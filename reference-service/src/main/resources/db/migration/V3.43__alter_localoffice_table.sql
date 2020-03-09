ALTER TABLE `LocalOffice`
ADD COLUMN `dbc` varchar(255) NOT NULL AFTER `id`;

ALTER TABLE `LocalOffice`
ADD COLUMN `entityId` bigint(20) NOT NULL AFTER `name`;

UPDATE `LocalOffice`
INNER JOIN `DBC`
ON `LocalOffice`.`abbreviation` = `DBC`.`abbr`
SET `LocalOffice`.`dbc` = `DBC`.`dbc`;

UPDATE `LocalOffice`
SET entityId = 1;

INSERT INTO `LocalOffice`
(`abbreviation`, `dbc`, `name`, `entityId`, `status`, `postAbbreviation`)
VALUES
('NIMDTA', 'NI-MOCK-DBC', 'Northern Ireland Medical and Dental Training Agency', 2, 'CURRENT', 'MDTA');