ALTER TABLE DBC

ADD
dbc_type varchar(255),
ADD
internal tinyint(1);

DELETE FROM DBC Where dbc = 'LDN-MOCK-DBC';

UPDATE DBC
SET dbc_type = 'LETB/Deanery'
WHERE dbc IN ('1-8W6121', '1-2SXJST');

UPDATE DBC SET internal = true;

