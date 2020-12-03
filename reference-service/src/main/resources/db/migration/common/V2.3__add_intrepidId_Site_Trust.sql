ALTER TABLE Site ADD COLUMN intrepidId varchar(255);
ALTER TABLE `Site` ADD INDEX `siteIntrepidId` (`intrepidId`);

ALTER TABLE Trust ADD COLUMN intrepidId varchar(255);
ALTER TABLE `Trust` ADD INDEX `trustIntrepidId` (`intrepidId`);
