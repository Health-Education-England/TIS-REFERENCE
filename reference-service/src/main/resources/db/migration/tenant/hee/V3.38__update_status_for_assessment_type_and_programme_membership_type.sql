-- Remove 'RITA' from Assessment type
UPDATE AssessmentType SET status = 'INACTIVE' WHERE code = 'RITA';

-- Remove 'LAT' and 'FISTA' from Programme membership type
UPDATE ProgrammeMembershipType SET status = 'INACTIVE' WHERE code in ('LAT', 'FTSTA');