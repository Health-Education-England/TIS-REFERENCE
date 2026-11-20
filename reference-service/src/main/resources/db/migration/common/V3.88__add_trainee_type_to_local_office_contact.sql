CREATE TABLE `TraineeType` (
  `id` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `TraineeType` (`id`, `code`, `label`, `status`) VALUES
(uuid(), 'SPECIALTY', 'Specialty', 'CURRENT'),
(uuid(), 'FOUNDATION', 'Foundation', 'CURRENT');

-- Nullable while the current Admin UI (which does not set it) is still in use; any contacts it
-- creates are populated by re-running the UPDATEs below before the column is made NOT NULL.
ALTER TABLE `LocalOfficeContact` ADD COLUMN `traineeTypeId` varchar(36) DEFAULT NULL;

ALTER TABLE `LocalOfficeContact` ADD CONSTRAINT `fk_lo_contact_trainee_type`
  FOREIGN KEY (`traineeTypeId`) REFERENCES `TraineeType` (`id`);

-- Target uniqueness rule. Coexists with idx_lo_contact_unique (localOfficeId, contactTypeId), which
-- must be dropped before Foundation contacts are moved onto the base contact types.
ALTER TABLE `LocalOfficeContact` ADD CONSTRAINT `idx_lo_contact_trainee_type_unique`
  UNIQUE (`localOfficeId`, `contactTypeId`, `traineeTypeId`);

-- Set each contact's trainee type, inferred from its contact type code suffix.
-- Only the new column is written; existing column values are unchanged, so the current Admin UI and
-- TSS are unaffected (TSS sync ignores unknown columns). Note this emits an update CDC event per
-- contact, so TSS should be able to consume traineeTypeId before this runs to avoid a re-sync.
-- Foundation contacts are not moved onto the base contact types here: that is deferred until TSS
-- selects contacts by traineeTypeId instead of the " - Foundation" contact type label suffix.
-- Re-runnable: only contacts without a trainee type are updated.
UPDATE `LocalOfficeContact`
SET `traineeTypeId` = (SELECT `id` FROM `TraineeType` WHERE `code` = 'FOUNDATION')
WHERE `traineeTypeId` IS NULL
  AND `contactTypeId` IN (
    SELECT `id` FROM `LocalOfficeContactType` WHERE RIGHT(`code`, 11) = '_FOUNDATION'
  );

UPDATE `LocalOfficeContact`
SET `traineeTypeId` = (SELECT `id` FROM `TraineeType` WHERE `code` = 'SPECIALTY')
WHERE `traineeTypeId` IS NULL;
