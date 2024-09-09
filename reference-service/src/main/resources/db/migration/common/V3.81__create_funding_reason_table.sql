CREATE TABLE `FundingReason` (
  `id` varchar(36) NOT NULL,
  `reason` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO FundingReason (`id`, `reason`, `status`) VALUES
(uuid(), 'Long Term Plan', 'CURRENT'),
(uuid(), 'Redistribution', 'CURRENT'),
(uuid(), 'Expansion Posts', 'CURRENT'),
(uuid(), 'Cancer and Diagnostics', 'CURRENT');