UPDATE `DBC` SET `abbr`='HEEOE' WHERE abbr='EOE';
UPDATE `DBC` SET `abbr`='HEKSS', `dbc`='1-AIIDR8' WHERE abbr='KSS';
UPDATE `DBC` SET `abbr`='HEEM' WHERE abbr='EM';
UPDATE `DBC` SET `abbr`='HETV' WHERE abbr='TV';

INSERT INTO `DBC` (`dbc`, `name`, `abbr`)
VALUES
	('1-AIIDWA', 'Health Education North West London', 'HENWL'),
  ('1-AIIDVS', 'Health Education North Central London', 'HENCEL'),
  ('1-AIIDWI', 'Health Education South London', 'HESL');
