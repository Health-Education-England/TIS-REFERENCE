ALTER TABLE `LocalOffice`
ADD COLUMN `entity` VARCHAR(45) NOT NULL AFTER `name`;

UPDATE `LocalOffice`
SET entity = 'HEE';