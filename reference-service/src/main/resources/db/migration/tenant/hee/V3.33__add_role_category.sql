create table `RoleCategory` (
  `id` bigint(20) not null auto_increment,
  `name` varchar(50) not null,
  primary key (`id`)
) engine=InnoDB default charset=utf8;

alter table `Role`
  add column `categoryId` bigint(20) DEFAULT NULL;

insert into `RoleCategory` (`id`, `name`) values
  (1, 'Clinical Supervisors'),
  (2, 'Educational Supervisors'),
  (3, 'Others');

update `Role`
set `categoryId` = 1
where `label` in (
  'Clinical Supervisor',
  'Dental Clinical Supervisor',
  'Foundation Clinical Supervisor',
  'GP Clinical Supervisor',
  'GP Foundation Clinical Supervisor',
  'GP Out of Hours Clinical Supervisor',
  'HEEWM Clinical Supervisor',
  'HEEWM Dental Clinical Supervisor',
  'HEEWM GP Clinical Supervisor',
  'NE.Clinical Supervisor',
  'YH Clinical Supervisor');

update `Role`
set `categoryId` = 2
where `label` in (
  'Dental Educational Supervisor',
  'Educational Supervisor',
  'Educational Supervisors',
  'Foundation Educational Supervisor',
  'GP Educational Supervisor',
  'HEEWM Dental Educational Supervisor',
  'HEEWM Educational Supervisor',
  'HEEWM GP Educational Supervisor',
  'NE.Educational Supervisor',
  'YH Educational Supervisor');

update `Role`
set `categoryId` = 3
where `categoryId` is null;

alter table `Role`
  modify `categoryId` bigint(20) not null;

alter table `Role`
  add constraint fk_role_categoryId foreign key (`categoryId`) references `RoleCategory` (`id`)
