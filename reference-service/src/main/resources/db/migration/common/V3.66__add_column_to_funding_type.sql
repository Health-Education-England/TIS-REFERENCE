ALTER TABLE `FundingType`
ADD COLUMN `academic` bit DEFAULT 0;

UPDATE `FundingType`
SET academic = 1
WHERE code IN (
  'ACADEMIC_NIHR',
  'ACADEMIC_HEE',
  'ACADEMIC_TRUST',
  'SUPERNUMERARY'
);
