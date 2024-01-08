CREATE TABLE FundingSubType (
  `uuid` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `parentId` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT fk_funding_sub_type_parent_id FOREIGN KEY (`parentId`) REFERENCES FundingType (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT @idHeeNonTariff := `id` FROM FundingType WHERE `code` = 'HEE_FUNDED_NON_TARIFF';
SELECT @idSuperNumerary := `id` FROM FundingType WHERE `code` = 'SUPERNUMERARY';

INSERT INTO FundingSubType (`uuid`, `code`, `label`, `status`, `parentId`) VALUES
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', 'CURRENT', @idHeeNonTariff),
(uuid(), 'DENTAL_50%', 'Dental – 50%', 'CURRENT', @idHeeNonTariff),
(uuid(), 'DENTAL_100%', 'Dental – 100%', 'CURRENT', @idHeeNonTariff),
(uuid(), 'FY2GP', 'FY2GP', 'CURRENT', @idHeeNonTariff),
(uuid(), 'PUBLIC_HEALTH', 'Public Health', 'CURRENT', @idHeeNonTariff),
(uuid(), 'HOSPICE_OR_CHARITY', 'Hospice or Charity', 'CURRENT', @idHeeNonTariff),
(uuid(), 'OCCUPATIONAL_MEDICINE', 'Occupational Medicine', 'CURRENT', @idHeeNonTariff),
(uuid(), 'COMMUNITY_SETTING', 'Community setting', 'CURRENT', @idHeeNonTariff),
(uuid(), 'FOUNDATION_ACADEMIC', 'Foundation academic', 'CURRENT', @idHeeNonTariff),
(uuid(), 'OTHER_POST_FUNDING', 'Other post funding', 'CURRENT', @idHeeNonTariff),
(uuid(), 'EXCEPTIONAL_TRAINING', 'Exceptional training', 'CURRENT', @idSuperNumerary),
(uuid(), 'SRTT', 'SRTT', 'CURRENT', @idSuperNumerary),
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', 'CURRENT', @idSuperNumerary);