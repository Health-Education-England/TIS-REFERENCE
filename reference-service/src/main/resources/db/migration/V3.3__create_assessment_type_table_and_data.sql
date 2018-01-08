CREATE TABLE `AssessmentType` (
  `code` varchar(255) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `AssessmentType` (`code`, `label`)
VALUES
  ('ARCP', 'ARCP'),
  ('RITA', 'RITA'),
  ('ACADEMIC', 'ACADEMIC'),
  ('IRCP', 'IRCP'),
  ('FRCP', 'FRCP'),
  ('FRCP_STAGE_2', 'FRCP (Stage 2)');



