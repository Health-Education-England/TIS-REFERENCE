CREATE TABLE `QualificationType` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `QualificationType` (code,label,status)
VALUES ('Bachelors Degree','Bachelors Degree', 'CURRENT'),
('Bachelors Degree Hons','Bachelors Degree Hons', 'CURRENT'),
('Basic Degree','Basic Degree','CURRENT'),
('Certification','Certification','CURRENT'),
('College/Faculty','College/Faculty','CURRENT'),
('Diploma','Diploma','CURRENT'),
('Foundation Degree','Foundation Degree','CURRENT'),
('Higher Degree','Higher Degree','CURRENT'),
('Masters Degree','Masters Degree','CURRENT'),
('Non Medical Degree','Non Medical Degree','CURRENT'),
('NVQ','NVQ','CURRENT'),
('PhD','PhD','CURRENT');