USE `reference prod`;

/* update_reference_values_type1: (PLEASE RUN THE SCRIPT compare_reference_values_type1.sql TO SEE DIFFERENCE BEFORE UPDATING!!!!!)
- SAME UUID IN BOTH REFERENCE TABLES, ACTIVE IN UK BUT NOT IN NIMDTA
- Comparison for same UUID with different status i.e. value already exist in both tables, but active in UK reference tables only. 
- This script activates in nimdta instead of creating new value 
#====================================================================================================================================*/

#1 - GENDER
UPDATE `reference nimdta`.gender
INNER JOIN `reference prod`.gender
ON		`reference prod`.gender.uuid = `reference nimdta`.gender.uuid
SET 	`reference nimdta`.gender.status = `reference prod`.gender.status
WHERE	`reference prod`.gender.status <> `reference nimdta`.gender.status
AND		`reference prod`.gender.status = 'CURRENT';
#-----------------------------------------------------------------------

#2 - ETHNIC ORIGIN
UPDATE `reference nimdta`.ethnicorigin
INNER JOIN `reference prod`.ethnicorigin
ON		`reference prod`.ethnicorigin.uuid = `reference nimdta`.ethnicorigin.uuid
SET 	`reference nimdta`.ethnicorigin.status = `reference prod`.ethnicorigin.status
WHERE	`reference prod`.ethnicorigin.status <> `reference nimdta`.ethnicorigin.status
AND		`reference prod`.ethnicorigin.status = 'CURRENT';
#-----------------------------------------------------------------------

#3 - RELIGIOUS BELIEF
UPDATE `reference nimdta`.religiousbelief
INNER JOIN `reference prod`.religiousbelief
ON		`reference prod`.religiousbelief.uuid = `reference nimdta`.religiousbelief.uuid
SET		`reference nimdta`.religiousbelief.status = `reference prod`.religiousbelief.status
WHERE	`reference prod`.religiousbelief.status <> `reference nimdta`.religiousbelief.status
AND		`reference prod`.religiousbelief.status = 'CURRENT';
#-----------------------------------------------------------------------

#4 - NATIONALITY
UPDATE `reference nimdta`.nationality
INNER JOIN `reference prod`.nationality
ON		`reference prod`.nationality.uuid = `reference nimdta`.nationality.uuid
SET		`reference nimdta`.nationality.status = `reference prod`.nationality.status
WHERE	`reference prod`.nationality.status <> `reference nimdta`.nationality.status
AND		`reference prod`.nationality.status = 'CURRENT';
#-----------------------------------------------------------------------

#5 - SEXUAL ORIENTATION
UPDATE `reference nimdta`.sexualorientation
INNER JOIN `reference prod`.sexualorientation
ON		`reference prod`.sexualorientation.uuid = `reference nimdta`.sexualorientation.uuid
SET		`reference nimdta`.sexualorientation.status = `reference prod`.sexualorientation.status
WHERE	`reference prod`.sexualorientation.status <> `reference nimdta`.sexualorientation.status
AND		`reference prod`.sexualorientation.status = 'CURRENT';
#-----------------------------------------------------------------------

#6 - MARITAL STATUS
UPDATE `reference nimdta`.maritalstatus
INNER JOIN `reference prod`.maritalstatus
ON		`reference prod`.maritalstatus.uuid = `reference nimdta`.maritalstatus.uuid
SET		`reference nimdta`.maritalstatus.status = `reference prod`.maritalstatus.status
WHERE	`reference prod`.maritalstatus.status <> `reference nimdta`.maritalstatus.status
AND		`reference prod`.maritalstatus.status = 'CURRENT';
#-----------------------------------------------------------------------