CREATE TABLE `PermitToWork` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `PermitToWork` (code,label,status)
VALUES ('Tier 1 - points based system - no endoresment regarding ''employment as a doctor or dentist in training''','Tier 1 - points based system - no endoresment regarding ''employment as a doctor or dentist in training''','CURRENT'),
('Tier 1 - points based system - with endorsement ''no employment as a doctor or dentist in training''','Tier 1 - points based system - with endorsement ''no employment as a doctor or dentist in training''','CURRENT'),
('Tier 2 - points based system','Tier 2 - points based system','CURRENT'),
('Highly Skilled Migrant Programme (with start and end dates of endorsement stamp in passport)','Highly Skilled Migrant Programme (with start and end dates of endorsement stamp in passport)','CURRENT'),
('Tier 4 - graduate of UK medical/dental school currently in Foundation Programme','Tier 4 - graduate of UK medical/dental school currently in Foundation Programme','CURRENT'),
('Tier 4 - currently a student at a UK university/medical school','Tier 4 - currently a student at a UK university/medical school','CURRENT'),
('Tier 4 - studying for a Masters/PhD','Tier 4 - studying for a Masters/PhD','CURRENT'),
('Tier 5 - Medical Training Initiative','Tier 5 - Medical Training Initiative','CURRENT'),
('UK ancestry','UK ancestry','CURRENT'),
('Refugee in the UK','Refugee in the UK','CURRENT'),
('Visitor visa/ PLAB visa/ Business Visitor visa','Visitor visa/ PLAB visa/ Business Visitor visa','CURRENT'),
('Other immigration categories i.e. overseas government employees, innovators etc.','Other immigration categories i.e. overseas government employees, innovators etc.','CURRENT'),
('Dependent - other immigration category','Dependent - other immigration category','CURRENT'),
('Dependent - partner holds Tier 2 visa which you are a dependent on','Dependent - partner holds Tier 2 visa which you are a dependent on','CURRENT'),
('Dependent with endorsement - You are the partner/civil partner/spouse of a UK/EEA national and have an endorsement regarding ''no employment as a doctor or dentist in training''','Dependent with endorsement - You are the partner/civil partner/spouse of a UK/EEA national and have an endorsement regarding ''no employment as a doctor or dentist in training''','CURRENT'),
('Dependent without endorsement - You are the partner/civil partner/spouse of a UK/EEA national and do not have an endorsement regarding ''employment as a doctor or dentist in training''','Dependent without endorsement - You are the partner/civil partner/spouse of a UK/EEA national and do not have an endorsement regarding ''employment as a doctor or dentist in training''','CURRENT'),
('No current immigration status in the UK','No current immigration status in the UK','CURRENT'),
('Tier 4 (Adult Student) graduate of UK medical school completed foundation','Tier 4 (Adult Student) graduate of UK medical school completed foundation','CURRENT'),
('Tier 4 (General) student','Tier 4 (General) student','CURRENT'),
('British National Overseas','British National Overseas','CURRENT'),
('Discretionary Leave to Remain','Discretionary Leave to Remain','CURRENT'),
('I am a fiancé of a UK national','I am a fiancé of a UK national','CURRENT'),
('Indefinite Leave to Remain','Indefinite Leave to Remain','CURRENT'),
('My immigration status is not listed as an option','My immigration status is not listed as an option','CURRENT'),
('Short-term study visa (not over 11 months)','Short-term study visa (not over 11 months)','CURRENT'),
('Tier 1','Tier 1','CURRENT'),
('Tier 2','Tier 2','CURRENT'),
('Tier 4 (General)','Tier 4 (General)','CURRENT'),
('Tier 5','Tier 5','CURRENT'),
('Turkish Businessperson','Turkish Businessperson','CURRENT'),
('Turkish Worker','Turkish Worker','CURRENT'),
('Visitor visa / PLAB visa / Business Visitor visa','Visitor visa / PLAB visa / Business Visitor visa','CURRENT'),
('You are the partner/civil partner/spouse of a UK/EEA national','You are the partner/civil partner/spouse of a UK/EEA national','CURRENT'),
('Tier 3','Tier 3','CURRENT');


