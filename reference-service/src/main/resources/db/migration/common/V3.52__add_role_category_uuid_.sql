ALTER TABLE `RoleCategory`
    ADD COLUMN uuid varchar(36);

UPDATE `RoleCategory` SET uuid =(SELECT uuid()) WHERE uuid IS NULL;

CREATE UNIQUE index idx_uuid_unique on `RoleCategory` (uuid);

DROP TRIGGER IF EXISTS before_insert_RoleCategory;
CREATE TRIGGER before_insert_RoleCategory
    BEFORE INSERT ON `RoleCategory`
    FOR EACH ROW
    SET new.uuid = ifnull(new.uuid,uuid());
