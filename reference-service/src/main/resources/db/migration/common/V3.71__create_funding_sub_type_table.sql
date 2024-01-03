CREATE TABLE FundingSubType (
  `uuid` varchar(36) NOT NULL,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `parentId` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT fk_funding_sub_type_parent_id FOREIGN KEY (parentId) REFERENCES FundingType (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TRIGGER before_insert_FundingSubType
  BEFORE INSERT ON FundingSubType
  FOR EACH ROW
  SET new.uuid = uuid();

INSERT INTO FundingSubType (`code`, `label`, `parentId`) VALUES
('SALARY_RECHARGES', 'Salary recharges', 6),
('DENTAL_50%', 'Dental – 50%', 6),
('DENTAL_100%', 'Dental – 100%', 6),
('FY2GP', 'FY2GP', 6),
('PUBLIC_HEALTH', 'Public Health', 6),
('HOSPICE_OR_CHARITY', 'Hospice or Charity', 6),
('OCCUPATIONAL_MEDICINE', 'Occupational Medicine', 6),
('COMMUNITY_SETTING', 'Community setting', 6),
('FOUNDATION_ACADEMIC', 'Foundation academic', 6),
('OTHER_POST_FUNDING', 'Other post funding', 6),
('EXCEPTIONAL_TRAINING', 'Exceptional training', 11),
('SRTT', 'SRTT', 11),
('SALARY_RECHARGES', 'Salary recharges', 11);

