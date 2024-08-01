UPDATE DBC
SET `name` = concat('NHSE Education ', right(`name`,char_length(`name`)-25))
WHERE `name` LIKE 'Health Education England %';