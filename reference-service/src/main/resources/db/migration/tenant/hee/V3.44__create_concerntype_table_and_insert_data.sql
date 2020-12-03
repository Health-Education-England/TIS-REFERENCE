CREATE TABLE `ConcernType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `ConcernType` (code,label)
VALUES
('SERIOUS_INCIDENT','Serious Incident'),
('COMPLAINT','Complaint'),
('CAPABILITY','Capability'),
('CONDUCT','Conduct'),
('NEVER_EVENT','Never event'),
('OTHER','Other'),
('SIGNIFICANT_EVENT','Significant Event');
