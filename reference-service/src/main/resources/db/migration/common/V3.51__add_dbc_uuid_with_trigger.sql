ALTER TABLE `DBC`
ADD COLUMN uuid varchar(36);

UPDATE `DBC` SET uuid =(SELECT uuid()) WHERE uuid IS NULL;

CREATE UNIQUE index idx_uuid_unique on `AssessmentType` (uuid);

DROP TRIGGER IF EXISTS before_insert_DBC;
CREATE TRIGGER before_insert_DBC
BEFORE INSERT ON `DBC`
FOR EACH ROW
SET new.uuid = ifnull(new.uuid,uuid());
