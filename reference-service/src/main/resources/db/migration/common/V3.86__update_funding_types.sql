UPDATE `FundingType`
SET
  `label` = REPLACE(`label`, 'HEE ', ''),
  `code` = REPLACE(`code`, 'HEE_', '')
WHERE `label` IN (
  'HEE Funded - Tariff',
  'HEE Funded - Non-Tariff'
);
