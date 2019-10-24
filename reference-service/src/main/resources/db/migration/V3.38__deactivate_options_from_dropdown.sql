-- Remove 'RITA' from Assessment type
UPDATE `reference`.`assessmenttype` SET `status` = 'INACTIVE' WHERE (`code` = 'RITA');

-- Remove 'LAT' and 'FISTA' from Programme membership type
UPDATE `reference`.`programmemembershiptype` SET `status` = 'INACTIVE' WHERE (`code` = 'LAT');
UPDATE `reference`.`programmemembershiptype` SET `status` = 'INACTIVE' WHERE (`code` = 'FTSTA');