INSERT INTO `DBC` (`dbc`, `name`, `abbr`)
VALUES
	('1-AIIDNQ', 'Health Education North West', 'HENW'),
	('1-AIIDHJ', 'Health Education Wessex', 'HEWESSEX');

TRUNCATE TABLE `LocalOffice`;

INSERT INTO `LocalOffice` (`name`,`abbreviation`) VALUES

	('Health Education England Wessex', 'HEWESSEX'),
	('Health Education England East of England', 'HEEOE'),
	('Health Education England Kent, Surrey and Sussex', 'HEKSS'),
	('Health Education England East Midlands', 'HEEM'),
	('Health Education England Thames Valley', 'HETV'),
	('Health Education England North West London', 'HENWL'),
	('Health Education England North Central and East London', 'HENCEL'),
	('Health Education England South London', 'HESL'),
	('Health Education England North East', 'HENE'),
	('Health Education England North West', 'HENW'),
	('Health Education England West Midlands', 'HEWMD'),
	('Health Education England Yorkshire and the Humber', 'HEYHD'),
	('Health Education England South West', 'HESW');

TRUNCATE TABLE `ProgrammeMembershipType`;

INSERT INTO `ProgrammeMembershipType` (`id`, `code`, `label`)
VALUES
	(1, 'SUBSTANTIVE', 'Substantive'),
	(2, 'LAT', 'LAT'),
	(3, 'FTSTA', 'FTSTA'),
	(4, 'MILITARY', 'Military'),
	(5, 'VISITOR', 'Visitor');
