CREATE TABLE FundingSubType (
  `uuid` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `parentUuid` varchar(36) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT fk_funding_sub_type_parent_id FOREIGN KEY (`parentUuid`) REFERENCES FundingType (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO FundingSubType (`uuid`, `code`, `label`, `parentUuid`) VALUES
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'DENTAL_50%', 'Dental – 50%', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'DENTAL_100%', 'Dental – 100%', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'FY2GP', 'FY2GP', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'PUBLIC_HEALTH', 'Public Health', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'HOSPICE_OR_CHARITY', 'Hospice or Charity', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'OCCUPATIONAL_MEDICINE', 'Occupational Medicine', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'COMMUNITY_SETTING', 'Community setting', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'FOUNDATION_ACADEMIC', 'Foundation academic', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'OTHER_POST_FUNDING', 'Other post funding', '0e92d91b-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'EXCEPTIONAL_TRAINING', 'Exceptional training', '0e92dc0e-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'SRTT', 'SRTT', '0e92dc0e-3efb-11eb-9f9a-0638a616fc76'),
(uuid(), 'SALARY_RECHARGES', 'Salary recharges', '0e92dc0e-3efb-11eb-9f9a-0638a616fc76');

