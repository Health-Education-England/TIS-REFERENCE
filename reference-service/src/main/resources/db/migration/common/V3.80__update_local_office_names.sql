
UPDATE `LocalOffice` SET `name` = REPLACE(`name`, "Health Education England ", "") WHERE `name` LIKE "Health Education England %";
UPDATE `Trust` SET `localOffice` = REPLACE(`localOffice`, "Health Education England ", "") WHERE `localOffice` LIKE "Health Education England %";
UPDATE `Site` SET `localOffice` = REPLACE(`localOffice`, "Health Education England ", "") WHERE `localOffice` LIKE "Health Education England %";