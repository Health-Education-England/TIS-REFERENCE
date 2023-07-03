USE `reference prod`;

/* compare_reference_values_type1: 
- SAME UUID IN BOTH REFERENCE TABLES, ACTIVE IN UK BUT NOT IN NIMDTA
- Comparison for same UUID with different status i.e. value already exist in both tables, but active in UK reference tables only. 
- Need to activate in nimdta instead of creating new value. To make the updates, please run the script update_reference_values_type1.sql
#====================================================================================================================================*/

#1 - GENDER
SELECT *
FROM `reference prod`.gender
INNER JOIN `reference nimdta`.gender
ON		`reference prod`.gender.uuid = `reference nimdta`.gender.uuid
WHERE	`reference prod`.gender.status <> `reference nimdta`.gender.status
AND		`reference prod`.gender.status = 'CURRENT';
#-----------------------------------------------------------------------

#2 - ETHNIC ORIGIN
SELECT *
FROM `reference prod`.ethnicorigin
INNER JOIN `reference nimdta`.ethnicorigin
ON		`reference prod`.ethnicorigin.uuid = `reference nimdta`.ethnicorigin.uuid
WHERE	`reference prod`.ethnicorigin.status <> `reference nimdta`.ethnicorigin.status
AND		`reference prod`.ethnicorigin.status = 'CURRENT';
#-----------------------------------------------------------------------

#3 - RELIGIOUS BELIEF
SELECT *
FROM `reference prod`.religiousbelief
INNER JOIN `reference nimdta`.religiousbelief
ON		`reference prod`.religiousbelief.uuid = `reference nimdta`.religiousbelief.uuid
WHERE	`reference prod`.religiousbelief.status <> `reference nimdta`.religiousbelief.status
AND		`reference prod`.religiousbelief.status = 'CURRENT';
#-----------------------------------------------------------------------

#4 - NATIONALITY
SELECT *
FROM `reference prod`.nationality
INNER JOIN `reference nimdta`.nationality
ON		`reference prod`.nationality.uuid = `reference nimdta`.nationality.uuid
WHERE	`reference prod`.nationality.status <> `reference nimdta`.nationality.status
AND		`reference prod`.nationality.status = 'CURRENT';
#-----------------------------------------------------------------------

#5 - SEXUAL ORIENTATION
SELECT *
FROM `reference prod`.sexualorientation
INNER JOIN `reference nimdta`.sexualorientation
ON		`reference prod`.sexualorientation.uuid = `reference nimdta`.sexualorientation.uuid
WHERE	`reference prod`.sexualorientation.status <> `reference nimdta`.sexualorientation.status
AND		`reference prod`.sexualorientation.status = 'CURRENT';
#-----------------------------------------------------------------------

#6 - MARITAL STATUS
SELECT *
FROM `reference prod`.maritalstatus
INNER JOIN `reference nimdta`.maritalstatus
ON		`reference prod`.maritalstatus.uuid = `reference nimdta`.maritalstatus.uuid
WHERE	`reference prod`.maritalstatus.status <> `reference nimdta`.maritalstatus.status
AND		`reference prod`.maritalstatus.status = 'CURRENT';
#-----------------------------------------------------------------------