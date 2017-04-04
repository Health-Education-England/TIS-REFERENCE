INSERT INTO `AssessmentType` (`code`, `label`)
VALUES
  ('ARCP', 'ARCP'),
  ('RITA', 'RITA'),
  ('ACADEMIC', 'Academic');

INSERT INTO `CurriculumSubType` (`code`, `label`)
VALUES
  ('MedicalCurriculum', 'Medical Curriculum - As defined by the GMC'),
  ('MedicalSpR', 'Medical SpR - As defined by the GMC'),
  ('DentalCurriculum', 'Dental Curriculum - As defined by the GDC'),
  ('DentalSpR', 'Dental SpR - As defined by the GDC'),
  ('SubSpecialty', 'Sub-specialty - As defined by the GMC'),
  ('DentalPostCCST', 'Dental Post-CCST - As defined by the GDC'),
  ('ACFOtherFunding', 'ACF (other funding)'),
  ('ACL', 'ACL (N Ireland)'),
  ('AFT', 'AFT'),
  ('ACLOtherFunding', 'ACL (other funding)'),
  ('ClinicalLecturer', 'Clinical Lecturer (Scotland)'),
  ('ClinicalTeachingFellow', 'Clinical Teaching Fellow (Scotland)'),
  ('ClinicalResearchFellow', 'Clinical Research Fellow (Scotland)'),
  ('ACFNIHRFunding', 'ACF (NIHR funding)'),
  ('ACLNIHRFunding', 'ACL (NIHR funding)'),
  ('Otherfellowship', 'Other fellowship');

INSERT INTO `FundingType` (`code`, `label`)
VALUES
  ('TARIFF', 'Tariff'),
  ('MADEL', 'MADEL'),
  ('TRUST', 'Trust'),
  ('OTHER', 'Other');

INSERT INTO `GdcStatus` (`code`, `label`)
VALUES
  ('MEMBER', 'Member'),
  ('NON_MEMBER', 'Non Member');

INSERT INTO `Gender` (`code`, `label`)
VALUES
  ('MALE', 'Male'),
  ('FEMALE', 'Female'),
  ('UNDISCLOSED', 'I do not wish to disclose my gender');

INSERT INTO `GmcStatus` (`code`, `label`)
VALUES
  ('MEMBER', 'Member'),
  ('NON_MEMBER', 'Non Member');

INSERT INTO `MaritalStatus` (`code`, `label`)
VALUES
  ('SINGLE', 'Single'),
  ('DIVORCED', 'Divorced'),
  ('WIDOWED', 'Widowed'),
  ('SEPARATED', 'Separated'),
  ('PARTNER', 'Partner'),
  ('CIVIL_PARTNERSHIP', 'Civil Partnership');

INSERT INTO `PlacementType` (`code`, `label`)
VALUES
  ('InPostStandard', 'In Post Standard Full or part-time placement.'),
  ('InPostExtension', 'In Post - Extension  Extra time may be required due to an outcome 3 assessment.'),
  ('OOPC', 'OOPC Out of Programme Career Break.'),
  ('OOPE', 'OOPE Out of Programme Experience that doesn’t contribute to the completion of a trainee’s programme.'),
  ('OOPR', 'OOPR Out of Programme Research.'),
  ('OOPT', 'OOPT Out of Programme Training'),
  ('ParentalLeave', 'Parental Leave This replaces both Maternity and Paternity Leave; for reporting the type of parental leave the gender of the trainee can be derived from their staff record.'),
  ('SickLeave', 'Long-Term Sick Leave Approved long-term sick.');

INSERT INTO `ProgrammeMembershipType` (`code`, `label`)
VALUES
  ('SUBSTANTIVE', 'Substantive'),
  ('LAT', 'LAT'),
  ('FTSTA', 'FTSTA'),
  ('MILITARY', 'Military'),
  ('VISITOR', 'Visitor');

INSERT INTO `RecordType` (`code`, `label`)
VALUES
  ('STAFF_TRAINEE', 'Staff Trainee'),
  ('CONTRACT_NON_TRAINEE', 'Contract Non-trainee'),
  ('BOTH', 'Both');

INSERT INTO `Role` (`code`, `label`)
VALUES
  ('DR_IN_TRAINING', 'Dr in Training');

INSERT INTO `Settled` (`code`, `label`)
VALUES
  ('YES', 'Yes'),
  ('NO', 'No'),
  ('UNKNOWN', 'Unknown');

INSERT INTO `SexualOrientation` (`code`, `label`)
VALUES
  ('HOMOSEXUAL', 'Homosexual'),
  ('HETEROSEXUAL', 'Heterosexual'),
  ('OTHER', 'Other'),
  ('UNSPECIFIED', 'Unspecified');

INSERT INTO `Title` (`code`, `label`)
VALUES
  ('MR', 'Mr'),
  ('MS', 'Ms'),
  ('MRS', 'Mrs'),
  ('DR', 'Dr');

INSERT INTO `TrainingNumberType` (`code`, `label`)
VALUES
  ('NTN', 'NTN'),
  ('DRN', 'DRN');

INSERT INTO DBC (dbc,name, abbr) VALUES
  ('1-AIIDWT', 'East of England', 'EOE'),
  ('1-85KJU0', 'Kent, Surrey and Sussex', 'KSS'),
  ('1-AIIDSA', 'Health Education East Midlands', 'EM'),
  ('1-AIIDH1', 'Health Education Thames Valley', 'TV');





