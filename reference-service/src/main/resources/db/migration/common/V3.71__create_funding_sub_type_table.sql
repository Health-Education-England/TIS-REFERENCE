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

INSERT INTO FundingSubType (`uuid`, `code`, `label`, `parentId`) VALUES
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', @idHeeNonTariff),
(uuid(), 'DENTAL_50%', 'Dental – 50%', @idHeeNonTariff),
(uuid(), 'DENTAL_100%', 'Dental – 100%', @idHeeNonTariff),
(uuid(), 'FY2GP', 'FY2GP', @idHeeNonTariff),
(uuid(), 'PUBLIC_HEALTH', 'Public Health', @idHeeNonTariff),
(uuid(), 'HOSPICE_OR_CHARITY', 'Hospice or Charity', @idHeeNonTariff),
(uuid(), 'OCCUPATIONAL_MEDICINE', 'Occupational Medicine', @idHeeNonTariff),
(uuid(), 'COMMUNITY_SETTING', 'Community setting', @idHeeNonTariff),
(uuid(), 'FOUNDATION_ACADEMIC', 'Foundation academic', @idHeeNonTariff),
(uuid(), 'OTHER_POST_FUNDING', 'Other post funding', @idHeeNonTariff),
(uuid(), 'EXCEPTIONAL_TRAINING', 'Exceptional training', @idSuperNumerary),
(uuid(), 'SRTT', 'SRTT', @idSuperNumerary),
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', @idSuperNumerary);