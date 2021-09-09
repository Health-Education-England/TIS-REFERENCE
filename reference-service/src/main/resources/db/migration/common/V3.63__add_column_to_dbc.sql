ALTER TABLE reference.DBC

ADD
dbc_type varchar(255),
ADD
internal varchar(255);

DELETE FROM reference.DBC Where dbc = 'LDN-MOCK-DBC';

UPDATE reference.DBC
SET dbc_type = 'LETB/Deanery'
WHERE dbc IN ('1-8W6121', '1-2SXJST');

UPDATE reference.DBC SET internal = 'true';

