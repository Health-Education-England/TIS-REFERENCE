create table `RoleCategory` (
  `id` bigint(20) not null auto_increment,
  `name` varchar(50) not null,
  primary key (`id`)
) engine=InnoDB default charset=utf8;

alter table `Role`
  add column `categoryId` bigint(20) not null;

alter table `Role`
  add constraint fk_role_categoryId foreign key (`categoryId`) references `RoleCategory` (`id`);
