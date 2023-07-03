USE `reference prod`;

/* update_reference_values_type2: 
- UUID EXISTS IN UK REFERENCE TABLE AND ACTIVE, BUT UUID NOT IN NIMDTA
- Comparison to see where new refeference values with new UUIDs have been added into UK reference and activated, but have not been added to nimdta. 
- Need to add the new values in UK reference table to nimdta and ensure values are consistent with same UUID acrossboth tables, rather than creating new UUIDs for same reference values, activate in nimdta instead of creating new value. 
- This script add the new values already in UK reference table to nimdta and activates them
- - Please take particular notice of Section for #3 - RELIGIOUS BELIEF. Appropriate comments already added
#====================================================================================================================================*/

#1 - GENDER
INSERT INTO `reference nimdta`.gender (id, code, label, status, uuid)
SELECT `reference prod`.gender.id,
		`reference prod`.gender.code,
        `reference prod`.gender.label,
        `reference prod`.gender.status,
        `reference prod`.gender.uuid  
        
FROM `reference prod`.gender
LEFT JOIN `reference nimdta`.gender
ON		`reference prod`.gender.uuid = `reference nimdta`.gender.uuid
WHERE	`reference prod`.gender.status = 'CURRENT'
AND 	`reference nimdta`.gender.uuid IS NULL;
#-----------------------------------------------------------------------

#2 - ETHNIC ORIGIN
INSERT INTO `reference nimdta`.ethnicorigin (code, status, uuid)
SELECT 	`reference prod`.ethnicorigin.code,
        `reference prod`.ethnicorigin.status,
        `reference prod`.ethnicorigin.uuid
        
FROM `reference prod`.ethnicorigin
LEFT JOIN `reference nimdta`.ethnicorigin
ON		`reference prod`.ethnicorigin.uuid = `reference nimdta`.ethnicorigin.uuid
WHERE	`reference prod`.ethnicorigin.status = 'CURRENT'
AND 	`reference nimdta`.ethnicorigin.uuid IS NULL;
#-----------------------------------------------------------------------

#3 - RELIGIOUS BELIEF - There are 2 scripts to this section - see below
#Part A - Checks for where uuid is null for every existing, currevnt values
INSERT IGNORE INTO `reference nimdta`.religiousbelief (code, label, status, uuid)
SELECT 	`reference prod`.religiousbelief.code,
        `reference prod`.religiousbelief.label,
        `reference prod`.religiousbelief.status,
        `reference prod`.religiousbelief.uuid  
        
FROM `reference prod`.religiousbelief
LEFT JOIN `reference nimdta`.religiousbelief
ON		`reference prod`.religiousbelief.uuid = `reference nimdta`.religiousbelief.uuid
WHERE	`reference prod`.religiousbelief.status = 'CURRENT'
AND 	`reference nimdta`.religiousbelief.uuid IS NULL;

/* Part B - Checks for where uuid is null for every existing, current values, but exists as a different uuid and set to inactive.
These are activated by setting status to 'CURRENT' rather than creating a new value in this part of the script
The reason for this extra check and update is because this table is built different and has 4 of the 5 fields set as unique. Don't know why.
*/
WITH cteExistingButInactivePostNewInsert AS (
SELECT tp.*
FROM `reference prod`.religiousbelief tp
LEFT JOIN `reference nimdta`.religiousbelief tn
ON		tp.uuid = tn.uuid
WHERE	tp.status = 'CURRENT'
AND 	tn.uuid IS NULL
)
UPDATE `reference nimdta`.religiousbelief t1
INNER JOIN cteExistingButInactivePostNewInsert t2
ON t1.code = t2.code 
SET t1.status = REPLACE(t1.status, 'INACTIVE', 'CURRENT')
WHERE	t1.status = 'INACTIVE';
#-----------------------------------------------------------------------
 
#4 - NATIONALITY
INSERT IGNORE INTO `reference nimdta`.nationality (id, countryNumber, nationality, status, uuid)
SELECT `reference prod`.nationality.id,
		`reference prod`.nationality.countryNumber,
        `reference prod`.nationality.nationality,
        `reference prod`.nationality.status,
        `reference prod`.nationality.uuid  
        
FROM `reference prod`.nationality
LEFT JOIN `reference nimdta`.nationality
ON		`reference prod`.nationality.uuid = `reference nimdta`.nationality.uuid
WHERE	`reference prod`.nationality.status = 'CURRENT'
AND 	`reference nimdta`.nationality.uuid IS NULL;
#-----------------------------------------------------------------------

#5 - SEXUAL ORIENTATION
INSERT IGNORE INTO `reference nimdta`.sexualorientation (id, code, label, status, uuid)
SELECT `reference prod`.sexualorientation.id,
		`reference prod`.sexualorientation.code,
        `reference prod`.sexualorientation.label,
        `reference prod`.sexualorientation.status,
        `reference prod`.sexualorientation.uuid  
        
FROM `reference prod`.sexualorientation
LEFT JOIN `reference nimdta`.sexualorientation
ON		`reference prod`.sexualorientation.uuid = `reference nimdta`.sexualorientation.uuid
WHERE	`reference prod`.sexualorientation.status = 'CURRENT'
AND 	`reference nimdta`.sexualorientation.uuid IS NULL;
#-----------------------------------------------------------------------

#6 - MARITAL STATUS
INSERT IGNORE INTO `reference nimdta`.maritalstatus (id, code, label, status, uuid)
SELECT `reference prod`.maritalstatus.id,
		`reference prod`.maritalstatus.code,
        `reference prod`.maritalstatus.label,
        `reference prod`.maritalstatus.status,
        `reference prod`.maritalstatus.uuid  
        
FROM `reference prod`.maritalstatus
LEFT JOIN `reference nimdta`.maritalstatus
ON		`reference prod`.maritalstatus.uuid = `reference nimdta`.maritalstatus.uuid
WHERE	`reference prod`.maritalstatus.status = 'CURRENT'
AND 	`reference nimdta`.maritalstatus.uuid IS NULL;
#-----------------------------------------------------------------------
