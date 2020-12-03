
update MaritalStatus set status = 'INACTIVE';

update MaritalStatus set status = 'CURRENT' where code in ('Married',
'Single',
'Divorced',
'Widowed',
'Legally Separated',
'Civil Partnership',
'Unknown');