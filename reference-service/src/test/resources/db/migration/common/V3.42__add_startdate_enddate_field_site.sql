ALTER TABLE `Site`
  ADD COLUMN (
    `startDate` DATE NULL DEFAULT NULL,
    `endDate` DATE NULL DEFAULT NULL
  ) AFTER `siteCode`;
