set autocommit=0;

ALTER TABLE `Grade`
DROP COLUMN `id`;

ALTER TABLE `Site`
DROP COLUMN `id`;

ALTER TABLE `Trust`
DROP COLUMN `id`;

ALTER TABLE `Grade`
ADD PRIMARY KEY (abbreviation);

ALTER TABLE `Site`
ADD PRIMARY KEY (siteCode);

ALTER TABLE `Trust`
ADD PRIMARY KEY (code);

commit;
