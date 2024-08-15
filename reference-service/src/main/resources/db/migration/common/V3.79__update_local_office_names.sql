SET SQL_SAFE_UPDATES = 0;
UPDATE `LocalOffice` SET `name` = REPLACE(`name`, "Health Education England ", "");
UPDATE `Trust` SET `localOffice` = REPLACE(`localOffice`, "Health Education England ", "");
UPDATE `Site` SET `localOffice` = REPLACE(`localOffice`, "Health Education England ", "");
SET SQL_SAFE_UPDATES = 1;