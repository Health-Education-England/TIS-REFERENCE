ALTER TABLE `DBC`
ADD COLUMN `entityId` bigint(20) NOT NULL AFTER `name`;

UPDATE `DBC`
SET entityId = 1;

INSERT INTO `DBC`
(`dbc`, `name`, `entityId`, `abbr`, `status`)
VALUES
('NI-MOCK-DBC', 'Northern Ireland Medical and Dental Training Agency', 2, 'NIMDTA', 'CURRENT');