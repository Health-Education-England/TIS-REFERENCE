CREATE TABLE `OrganizationType` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `uuid` varchar(36),
    `code` varchar(255) NOT NULL,
    `label` varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_org_type_code` (`code`),
    UNIQUE KEY `idx_uuid_unique` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
