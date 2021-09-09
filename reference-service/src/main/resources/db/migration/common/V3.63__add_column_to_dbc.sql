ALTER TABLE DBC ADD type varchar(255);
ALTER TABLE DBC ADD internal tinyint(1);

DELETE FROM DBC Where dbc = 'LDN-MOCK-DBC';

UPDATE DBC
SET type = 'LETB/Deanery'
WHERE dbc IN ('1-8W6121', '1-2SXJST');

UPDATE DBC SET internal = true;

