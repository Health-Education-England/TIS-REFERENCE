CREATE TABLE `ConcernSource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sourceName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO ConcernSource (sourceName) VALUES
('Lead Employer Trust (LET)'),
('Trainee'),
('TPD'),
('Specialty Team'),
('Previous HEE'),
('GMC'),
('Other');