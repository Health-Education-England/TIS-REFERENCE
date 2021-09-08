ALTER TABLE reference.DBC

ADD
dbc_type varchar(255),
ADD
is_internal_dbc varchar(255);

DELETE FROM reference.DBC Where dbc = 'LDN-MOCK-DBC';

UPDATE reference.DBC
SET dbc_type = 'LETB/Deanery'
WHERE dbc = '1-8W6121' || dbc = '1-2SXJST';

UPDATE reference.DBC SET is_internal_dbc = 'true';

