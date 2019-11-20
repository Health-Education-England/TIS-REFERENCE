CREATE TABLE `LeavingReason` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `LeavingReason` (code,label,status)
VALUES
  ('Completed training', 'Completed training', 'CURRENT'),
  ('Removed from training programme', 'Removed from training programme','CURRENT'),
  ('Inter deanery transfer', 'Inter deanery transfer', 'CURRENT'),
  ('Left without CCT – employment not known', 'Left without CCT – employment not known', 'CURRENT');
