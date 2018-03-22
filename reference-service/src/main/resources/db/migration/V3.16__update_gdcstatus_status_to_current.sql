
update GdcStatus set status = 'INACTIVE';

update GdcStatus set status = 'CURRENT' where code in ('Full',
'Limited',
'Provisional',
'Lapsed');