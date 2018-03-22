
update Title set status = 'INACTIVE';

update Title set status = 'CURRENT' where code in ('Dr',
'Miss',
'Mr',
'Mrs',
'Ms',
'Professor');