CREATE TABLE `LocalOfficeContactType` (
  `id` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `LocalOfficeContactType` (`id`, `code`, `label`, `status`) VALUES
(uuid(), 'DEFERRAL', 'Deferral', 'CURRENT'),
(uuid(), 'LTFT', 'Less Than Full Time', 'CURRENT'),
(uuid(), 'ONBOARDING_SUPPORT', 'Onboarding Support', 'CURRENT'),
(uuid(), 'SPONSORSHIP', 'Sponsorship', 'CURRENT'),
(uuid(), 'TSS_SUPPORT', 'TIS Self-Service Support', 'CURRENT');

CREATE TABLE `LocalOfficeContact` (
  `id` varchar(36) NOT NULL,
  `localOfficeId` varchar(36) NOT NULL,
  `contactTypeId` varchar(36) NOT NULL,
  `contact` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_lo_contact_unique` (`localOfficeId`, `contactTypeId`),
  CONSTRAINT `fk_local_office` FOREIGN KEY (`localOfficeId`) REFERENCES `LocalOffice` (`uuid`),
  CONSTRAINT `fk_contact_type` FOREIGN KEY (`contactTypeId`) REFERENCES `LocalOfficeContactType` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
