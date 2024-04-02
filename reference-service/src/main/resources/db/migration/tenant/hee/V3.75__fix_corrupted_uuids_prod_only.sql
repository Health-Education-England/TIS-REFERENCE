-- Grade
UPDATE `Grade` SET `uuid` = uuid() WHERE `name` = 'Local Clinical Lecturer - HENW' and `uuid` = '0ed71f2e-3efb-11';
UPDATE `Grade` SET `uuid` = uuid() WHERE `name` = 'Specialty Training Year 9' and `uuid` = '0ed80653-3efb-11';
-- use the valid uuid from NIMDTA
UPDATE `Grade` SET `uuid` = 'e20a8576-5a1b-11eb-b9f5-06521f467108' WHERE `name` = 'Core Training Year 4' and `uuid` = 'e20a8576-5a1b-11';

-- MedicalSchool
UPDATE `MedicalSchool` SET `uuid` = uuid() WHERE `code` = 'ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ' and `uuid` = 'fab8afcd-f081-11';

-- Nationality
UPDATE `Nationality` SET `uuid` = uuid() WHERE `nationality` = 'Malta X' and `uuid` = '0f47827c-3efb-11';
UPDATE `Nationality` SET `uuid` = uuid() WHERE `nationality` = 'Botswanan' and `uuid` = 'd008601f-a364-11';
-- use the valid uuid from NIMDTA
UPDATE `Nationality` SET `uuid` = 'e7f68a45-5a1b-11eb-b9f5-06521f467108' WHERE `nationality` = 'Filipino' and `uuid` = '0f4743a0-3efb-11';
UPDATE `Nationality` SET `uuid` = '0f4734b9-3efb-11eb-9f9a-0638a616fc76' WHERE `nationality` = 'Maltese' and `uuid` = '0f4734b9-3efb-11';

-- PermitToWork
-- use the valid uuid from NIMDTA
UPDATE `PermitToWork` SET `uuid` = '0f58a98f-3efb-11eb-9f9a-0638a616fc76' WHERE `code` = 'British National Overseas' and `uuid` = '0f58a98f-3efb-11';
