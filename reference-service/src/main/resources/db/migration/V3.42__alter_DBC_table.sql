ALTER TABLE `DBC`
ADD COLUMN `entity` VARCHAR(45) NOT NULL AFTER `name`;

UPDATE `DBC`
SET entity = 'HEE';