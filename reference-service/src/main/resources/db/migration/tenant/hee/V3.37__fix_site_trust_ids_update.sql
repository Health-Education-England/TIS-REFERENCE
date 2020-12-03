update `Site` as s
left join `Trust` as t on s.trustId = t.id
left join `Trust` as t2 on s.trustCode = t2.code and t2.status = 'CURRENT'
set s.trustId = t2.id
where t.code <> s.trustCode;
