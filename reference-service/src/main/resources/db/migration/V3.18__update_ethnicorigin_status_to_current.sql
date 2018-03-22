INSERT INTO EthnicOrigin (code)
SELECT 'White - British' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='White - British')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'White - Irish' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='White - Irish')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Any other white background' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Any other white background')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Mixed White and Black Caribbean' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Mixed White and Black Caribbean')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Mixed White and Black African' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Mixed White and Black African')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Mixed White and Asian' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Mixed White and Asian')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Asian or Asian British - Indian' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Asian or Asian British - Indian')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Asian or Asian British - Pakistani' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Asian or Asian British - Pakistani')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Asian or Asian British - Bangladeshi' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Asian or Asian British - Bangladeshi')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Any other Asian background' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Any other Asian background')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Black or Black British - Caribbean' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Black or Black British - Caribbean')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Black or Black British - African' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Black or Black British - African')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Any other Black background' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Any other Black background')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Chinese' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Chinese')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Any other ethnic group' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Any other ethnic group')
LIMIT 1;

INSERT INTO EthnicOrigin (code)
SELECT 'Not stated' FROM DUAL
WHERE NOT EXISTS (SELECT * FROM EthnicOrigin
      WHERE code='Not stated')
LIMIT 1;

update EthnicOrigin set status = 'INACTIVE';

update EthnicOrigin set status = 'CURRENT' where code in ('White - British',
'White - Irish',
'Any other white background',
'Mixed White and Black Caribbean',
'Mixed White and Black African',
'Mixed White and Asian',
'Any other mixed background',
'Asian or Asian British - Indian',
'Asian or Asian British - Pakistani',
'Asian or Asian British - Bangladeshi',
'Any other Asian background',
'Black or Black British - Caribbean',
'Black or Black British - African',
'Any other Black background',
'Chinese',
'Any other ethnic group',
'Not stated');