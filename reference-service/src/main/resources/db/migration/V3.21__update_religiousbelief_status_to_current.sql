INSERT INTO ReligiousBelief (code,label)
SELECT 'Neither community','Neither community' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM ReligiousBelief
      WHERE code='Neither community')
LIMIT 1;

update ReligiousBelief set status = 'INACTIVE';

update ReligiousBelief set status = 'CURRENT' where code in ('Atheism',
'Buddhism',
'Christianity',
'Hinduism',
'Islam',
'Jainism',
'Judaism',
'Sikhism',
'Other',
'I do not wish to disclose my religious belief',
'Protestant Community',
'Roman Catholic Community',
'Neither community');