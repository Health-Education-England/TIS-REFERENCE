ALTER TABLE `Site`
  ADD COLUMN `organizationTypeId` bigint(20);
ALTER TABLE `Site`
  ADD CONSTRAINT `fk_site_organization_type_id` FOREIGN KEY (`organizationTypeId`) REFERENCES `OrganizationType`(`id`);

ALTER TABLE `Trust`
  ADD COLUMN `organizationTypeId` bigint(20);
ALTER TABLE `Trust`
  ADD CONSTRAINT `fk_trust_organization_type_id` FOREIGN KEY (`organizationTypeId`) REFERENCES `OrganizationType`(`id`);
