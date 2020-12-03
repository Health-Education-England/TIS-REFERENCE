
update SexualOrientation set status = 'INACTIVE';

update SexualOrientation set status = 'CURRENT' where code in ('Lesbian',
'Gay',
'Bisexual',
'Heterosexual',
'I do not wish to disclose my sexual orientation');