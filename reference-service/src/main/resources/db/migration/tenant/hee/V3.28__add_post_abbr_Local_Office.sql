ALTER TABLE `LocalOffice`
ADD COLUMN `postAbbreviation` varchar(255) DEFAULT NULL;

UPDATE LocalOffice SET postAbbreviation='WES' WHERE abbreviation='HEWESSEX';
UPDATE LocalOffice SET postAbbreviation='EOE' WHERE abbreviation='HEEOE';
UPDATE LocalOffice SET postAbbreviation='LDN' WHERE abbreviation='HEKSS';
UPDATE LocalOffice SET postAbbreviation='EMD' WHERE abbreviation='HEEM';
UPDATE LocalOffice SET postAbbreviation='OXF' WHERE abbreviation='HETV';
UPDATE LocalOffice SET postAbbreviation='LDN' WHERE abbreviation='HENWL';
UPDATE LocalOffice SET postAbbreviation='LDN' WHERE abbreviation='HENCEL';
UPDATE LocalOffice SET postAbbreviation='LDN' WHERE abbreviation='HESL';
UPDATE LocalOffice SET postAbbreviation='NTH' WHERE abbreviation='HENE';
UPDATE LocalOffice SET postAbbreviation='NWN' WHERE abbreviation='HENW';
UPDATE LocalOffice SET postAbbreviation='WMD' WHERE abbreviation='HEWMD';
UPDATE LocalOffice SET postAbbreviation='YHD' WHERE abbreviation='HEYHD';
UPDATE LocalOffice SET postAbbreviation='SW' WHERE abbreviation='HESW';

