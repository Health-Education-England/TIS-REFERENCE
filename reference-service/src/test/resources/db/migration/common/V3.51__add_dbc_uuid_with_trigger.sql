ALTER TABLE `DBC`
ADD COLUMN uuid varchar(36);

/* Skipping the trigger as H2 doesn't support the standard syntax */