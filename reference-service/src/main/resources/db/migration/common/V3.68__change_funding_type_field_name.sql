ALTER TABLE `FundingType`
CHANGE COLUMN `academic` `allowDetails` bit;

UPDATE `FundingType`
SET allowDetails = 1
WHERE code IN (
  'OTHER'
);