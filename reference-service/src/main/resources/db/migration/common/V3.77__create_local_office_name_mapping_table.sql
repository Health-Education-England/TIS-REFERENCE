DROP TABLE IF EXISTS LocalOfficeNameMapping;

CREATE TABLE `LocalOfficeNameMapping` (
     `uuid`       varchar(36) NOT NULL,
     `oldName`    varchar(255) NULL,
     `newName`    varchar(255) NULL,
     `reason`     varchar(255) NULL,
     PRIMARY KEY (`uuid`)
  );

INSERT INTO LocalOfficeNameMapping (`uuid`, `oldName`, `newName`, `reason`) VALUES
(uuid(), 'Health Education England Yorkshire and the Humber', 'Yorkshire and the Humber', "NHSE-MERGER"),
(uuid(), 'Health Education England East Midlands', 'East Midlands', "NHSE-MERGER"),
(uuid(), 'Health Education England East of England', 'East of England', "NHSE-MERGER"),
(uuid(), 'Health Education England North East', 'North East', "NHSE-MERGER"),
(uuid(), 'Health Education England North West', 'North West', "NHSE-MERGER"),
(uuid(), 'Health Education England South London', 'South London', "NHSE-MERGER"),
(uuid(), 'Health Education England Thames Valley', 'Thames Valley', "NHSE-MERGER"),
(uuid(), 'Health Education England Wessex', 'Wessex', "NHSE-MERGER"),
(uuid(), 'Health Education England South West', 'South West', "NHSE-MERGER"),
(uuid(), 'Health Education England West Midlands', 'West Midlands', "NHSE-MERGER"),
(uuid(), 'Health Education England Kent, Surrey and Sussex', 'Kent, Surrey and Sussex', "NHSE-MERGER"),
(uuid(), 'Health Education England North Central and East London', 'North Central and East London', "NHSE-MERGER"),
(uuid(), 'Health Education England North West London', 'North West London', "NHSE-MERGER");
