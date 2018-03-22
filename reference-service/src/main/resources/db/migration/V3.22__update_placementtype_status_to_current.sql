
update PlacementType set status = 'INACTIVE';

update PlacementType set status = 'CURRENT' where code in ('In Post',
'OOPC',
'OOPE',
'OOPT',
'OOPR',
'Parental Leave',
'In Post - Acting Up',
'In Post - Extension',
'Long-term sick',
'Suspended',
'Phased Return');