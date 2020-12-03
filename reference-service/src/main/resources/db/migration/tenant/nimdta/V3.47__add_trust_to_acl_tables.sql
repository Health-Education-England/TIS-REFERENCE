/*
#---------------
# No data will exist int the database when theis runs and therefore needs to be cut down or removed
#---------------
INSERT INTO `acl_class` (`class`)
VALUES ('com.transformuk.hee.tis.reference.service.model.Trust');

INSERT IGNORE INTO `acl_sid` (`principal`, `sid`)
VALUES (0, 'HEE');

INSERT INTO `acl_object_identity` (`object_id_class`, `object_id_identity`, `owner_sid`,
                                   `entries_inheriting`)
SELECT (
           SELECT `id`
           FROM `acl_class`
           WHERE `class` = 'com.transformuk.hee.tis.reference.service.model.Trust'
       ),
       `id`,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1
FROM `Trust`;

-- Add read permissions
INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       0,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       1,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.reference.service.model.Trust'
      );

-- Add write permissions
INSERT INTO `acl_entry` (`acl_object_identity`, `ace_order`, `sid`, `mask`, `granting`,
                         `audit_success`, `audit_failure`)
SELECT `id`,
       1,
       (
           SELECT `id`
           FROM `acl_sid`
           WHERE `sid` = 'HEE'
       ),
       2,
       1,
       1,
       1
FROM `acl_object_identity`
WHERE `object_id_class` =
      (
          SELECT `id`
          FROM `acl_class`
          WHERE `class` = 'com.transformuk.hee.tis.reference.service.model.Trust'
      );
*/