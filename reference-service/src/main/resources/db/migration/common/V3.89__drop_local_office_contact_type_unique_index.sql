-- Contacts are now unique per local office, contact type and trainee type, enforced by
-- idx_lo_contact_trainee_type_unique (added in V3.88). The old two-column key would otherwise block
-- a second contact of the same type for a different trainee type.
-- fk_local_office now uses idx_lo_contact_trainee_type_unique, which also starts with localOfficeId.
ALTER TABLE `LocalOfficeContact` DROP INDEX `idx_lo_contact_unique`;
