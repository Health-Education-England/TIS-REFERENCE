ALTER TABLE `Site`
  ADD COLUMN `startDate` DATE NULL DEFAULT NULL AFTER `siteCode`,
  ADD COLUMN `endDate` DATE NULL DEFAULT NULL AFTER `startDate`;
