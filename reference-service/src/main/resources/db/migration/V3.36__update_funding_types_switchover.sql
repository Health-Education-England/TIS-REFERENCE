UPDATE FundingType SET status = 'CURRENT' where label = 'HEE Funded - Tariff';
UPDATE FundingType SET status = 'CURRENT' where label = 'HEE Funded - Non-Tariff';
UPDATE FundingType SET status = 'CURRENT' where label = 'Trust Funded';
UPDATE FundingType SET status = 'CURRENT' where label = 'Academic - NIHR';
UPDATE FundingType SET status = 'CURRENT' where label = 'Academic - HEE';
UPDATE FundingType SET status = 'CURRENT' where label = 'Academic - Trust';
UPDATE FundingType SET status = 'CURRENT' where label = 'Supernumerary';

UPDATE FundingType SET status = 'INACTIVE' where code = 'TARIFF';
UPDATE FundingType SET status = 'INACTIVE' where code = 'MADEL';
UPDATE FundingType SET status = 'INACTIVE' where code = 'TRUST';