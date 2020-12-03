
update Settled set status = 'INACTIVE';

update Settled set status = 'CURRENT' where code in ('Yes',
'No',
'Unknown');