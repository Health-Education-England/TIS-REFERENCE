CREATE TABLE IF NOT EXISTS Grade (
  abbreviation varchar(50) NOT NULL primary key,
  label varchar(50) NOT NULL,
  trainingGrade boolean NOT NULL,
  postGrade boolean NOT NULL,
  placementGrade boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS Trust (
  code varchar(50) NOT NULL primary key,
  name varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS Site (
  siteCode varchar(50) NOT NULL primary key,
  trustCode varchar(50) NOT NULL,
  siteName varchar(200) NOT NULL,
  address varchar (1000),
  postCode varchar(50) NOT NULL,
  CONSTRAINT fk_site_trust FOREIGN KEY (trustCode) REFERENCES Trust (code) ON DELETE CASCADE ON UPDATE CASCADE
);




