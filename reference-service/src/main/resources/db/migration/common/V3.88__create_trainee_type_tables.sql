CREATE TABLE `TraineeType` (
  `id` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ignore PUBLIC HEALTH, DENTAL for initial migration; they will be added later if needed.
INSERT INTO `TraineeType` (`id`, `code`, `label`, `status`) VALUES
(uuid(), 'SPECIALTY', 'Specialty', 'CURRENT'),
(uuid(), 'FOUNDATION', 'Foundation', 'CURRENT');

-- No ON DELETE CASCADE: cascaded deletes are not written to the binlog, so would not reach TSS via DMS.
CREATE TABLE `LocalOfficeContactTraineeType` (
  `id` varchar(36) NOT NULL,
  `contactId` varchar(36) NOT NULL,
  `traineeTypeId` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_lo_contact_trainee_type_unique` (`contactId`, `traineeTypeId`),
  CONSTRAINT `fk_lo_contact_trainee_type_contact` FOREIGN KEY (`contactId`) REFERENCES `LocalOfficeContact` (`id`),
  CONSTRAINT `fk_lo_contact_trainee_type_trainee_type` FOREIGN KEY (`traineeTypeId`) REFERENCES `TraineeType` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Link each existing contact 1:1 to a trainee type, inferred from its contact type code suffix.
-- Existing data is left untouched; de-duplication is deferred until downstream consumers use these links.
-- Why defer: the Admin UI and TSS still read the flat contact list, so existing rows must not change yet.
-- Linking 1:1 (rather than pointing a FOUNDATION link at an identical SPECIALTY contact) also means an
-- edit to either contact via the current UI only affects its own trainee type, so the links stay correct
-- without triggers. Identical SPECIALTY/FOUNDATION pairs can be merged safely once TSS reads the links.
-- Re-runnable: only contacts without a link are inserted.
INSERT INTO `LocalOfficeContactTraineeType` (`id`, `contactId`, `traineeTypeId`)
SELECT uuid(), c.`id`, tt.`id`
FROM `LocalOfficeContact` c
JOIN `LocalOfficeContactType` ct ON ct.`id` = c.`contactTypeId`
JOIN `TraineeType` tt ON tt.`code` =
  CASE WHEN RIGHT(ct.`code`, 11) = '_FOUNDATION' THEN 'FOUNDATION' ELSE 'SPECIALTY' END
WHERE NOT EXISTS (
  SELECT 1 FROM `LocalOfficeContactTraineeType` l WHERE l.`contactId` = c.`id`
);
