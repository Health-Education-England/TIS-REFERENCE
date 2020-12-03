insert into `RoleCategory` (`id`, `name`)
values
  (4, 'Leave Approvers');

update `Role`
  set `categoryId` = 4
  where `code` in ('Leave.Approver.NonAdministrator');