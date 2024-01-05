CREATE TABLE FundingSubType (
  `uuid` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `parentUuid` varchar(36) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT fk_funding_sub_type_parent_id FOREIGN KEY (`parentUuid`) REFERENCES FundingType (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SELECT @uuidHeeNonTariff := `uuid` FROM FundingType WHERE `code` = 'HEE_FUNDED_NON_TARIFF';
SELECT @uuidSuperNumerary := `uuid` FROM FundingType WHERE `code` = 'SUPERNUMERARY';

INSERT INTO FundingSubType (`uuid`, `code`, `label`, `parentUuid`) VALUES
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', @uuidHeeNonTariff),
(uuid(), 'DENTAL_50%', 'Dental – 50%', @uuidHeeNonTariff),
(uuid(), 'DENTAL_100%', 'Dental – 100%', @uuidHeeNonTariff),
(uuid(), 'FY2GP', 'FY2GP', @uuidHeeNonTariff),
(uuid(), 'PUBLIC_HEALTH', 'Public Health', @uuidHeeNonTariff),
(uuid(), 'HOSPICE_OR_CHARITY', 'Hospice or Charity', @uuidHeeNonTariff),
(uuid(), 'OCCUPATIONAL_MEDICINE', 'Occupational Medicine', @uuidHeeNonTariff),
(uuid(), 'COMMUNITY_SETTING', 'Community setting', @uuidHeeNonTariff),
(uuid(), 'FOUNDATION_ACADEMIC', 'Foundation academic', @uuidHeeNonTariff),
(uuid(), 'OTHER_POST_FUNDING', 'Other post funding', @uuidHeeNonTariff),
(uuid(), 'EXCEPTIONAL_TRAINING', 'Exceptional training', @uuidSuperNumerary),
(uuid(), 'SRTT', 'SRTT', @uuidSuperNumerary),
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', @uuidSuperNumerary);