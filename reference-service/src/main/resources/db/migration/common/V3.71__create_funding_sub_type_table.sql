CREATE TABLE FundingSubType (
  `uuid` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `parentId` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT fk_funding_sub_type_parent_id FOREIGN KEY (parentId) REFERENCES FundingType (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO FundingSubType (`uuid`, `code`, `label`, `parentId`) VALUES
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', 6),
(uuid(), 'DENTAL_50%', 'Dental – 50%', 6),
(uuid(), 'DENTAL_100%', 'Dental – 100%', 6),
(uuid(), 'FY2GP', 'FY2GP', 6),
(uuid(), 'PUBLIC_HEALTH', 'Public Health', 6),
(uuid(), 'HOSPICE_OR_CHARITY', 'Hospice or Charity', 6),
(uuid(), 'OCCUPATIONAL_MEDICINE', 'Occupational Medicine', 6),
(uuid(), 'COMMUNITY_SETTING', 'Community setting', 6),
(uuid(), 'FOUNDATION_ACADEMIC', 'Foundation academic', 6),
(uuid(), 'OTHER_POST_FUNDING', 'Other post funding', 6),
(uuid(), 'EXCEPTIONAL_TRAINING', 'Exceptional training', 11),
(uuid(), 'SRTT', 'SRTT', 11),
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', 11);

