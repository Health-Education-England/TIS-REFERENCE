ALTER TABLE `AssessmentType`
DROP PRIMARY KEY;

ALTER TABLE `AssessmentType`
ADD UNIQUE KEY (`code`);

ALTER TABLE `AssessmentType`
ADD COLUMN `id` BIGINT(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `AssessmentType`
ADD PRIMARY KEY (`id`);
