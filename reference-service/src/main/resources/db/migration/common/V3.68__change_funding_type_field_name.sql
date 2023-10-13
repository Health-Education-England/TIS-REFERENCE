ALTER TABLE `FundingType`
CHANGE COLUMN `academic` `allowDetails` bit DEFAULT 0;

UPDATE `FundingType`
SET `allowDetails` = 1
WHERE `code` IN (
  'OTHER'
);