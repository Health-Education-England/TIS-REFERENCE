CREATE TABLE `ConnectionSource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sourceName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO ConnectionSource (sourceName) VALUES
('Lead Employer Trust (LET)'),
('Trainee'),
('TPD'),
('Specialty Team'),
('Previous HEE'),
('GMC'),
('Other');