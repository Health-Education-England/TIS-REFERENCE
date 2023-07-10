-- GENDER
INSERT INTO `Gender`(`code`, `label`, `status`, `uuid`)
VALUES
  ('Non-Binary','Non-Binary','CURRENT','a9601ee4-f0d1-11ed-9eb2-0638a616fc76'),
  ('Other (not listed)','Other (not listed)','CURRENT','b72fb5a6-f0d1-11ed-9eb2-0638a616fc76'),
  ('Not Stated','Not Stated','CURRENT','bdcf46fb-f0d1-11ed-9eb2-0638a616fc76');

-- ETHNIC ORIGIN
INSERT INTO `EthnicOrigin`(`code`, `status`, `uuid`)
VALUES
  ('White - Any other White background','CURRENT','e13b46e7-f0d1-11ed-9eb2-0638a616fc76'),
  ('Mixed - White and Black Caribbean','CURRENT','ef0e99fc-f0d1-11ed-9eb2-0638a616fc76'),
  ('Mixed - White and Black African','CURRENT','f5572db6-f0d1-11ed-9eb2-0638a616fc76'),
  ('Mixed - White and Asian','CURRENT','fc32c0c6-f0d1-11ed-9eb2-0638a616fc76'),
  ('Mixed - Any other mixed background','CURRENT','011118b8-f0d2-11ed-9eb2-0638a616fc76'),
  ('Asian or Asian British - Any other Asian background','CURRENT','07cd8151-f0d2-11ed-9eb2-0638a616fc76'),
  ('Black or Black British - Any other Black background','CURRENT','0cc94e7f-f0d2-11ed-9eb2-0638a616fc76'),
  ('Other Ethnic Groups - Chinese','CURRENT','131c7673-f0d2-11ed-9eb2-0638a616fc76'),
  ('Other Ethnic Groups - Any other ethnic group','CURRENT','1a06d430-f0d2-11ed-9eb2-0638a616fc76');
        
UPDATE `EthnicOrigin`
SET `status` = 'INACTIVE'
WHERE	`id` IN (86, 87, 88, 89, 93, 96, 98);

-- RELIGIOUS BELIEF
INSERT INTO `ReligiousBelief`(`id`, `code`, `label`, `status`, `uuid`)
VALUES
  (8,'Agnostic','Agnostic','CURRENT','0f8d7be9-3efb-11'),
  (15,'Atheist','Atheist','CURRENT','0f8d8039-3efb-11'),
  (22,'Christian','Christian','CURRENT','0f8d832e-3efb-11'),
  (24,'Church of England','Church of England','CURRENT','0f8d83ea-3efb-11'),
  (26,'Church of Scotland','Church of Scotland','CURRENT','0f8d84ab-3efb-11'),
  (27,'Druze','Druze','CURRENT','0f8d8510-3efb-11'),
  (29,'Greek Catholic','Greek Catholic','CURRENT','0f8d85d0-3efb-11'),
  (30,'Greek Orthodox','Greek Orthodox','CURRENT','0f8d862e-3efb-11'),
  (31,'Hindu','Hindu','CURRENT','0f8d868c-3efb-11'),
  (55,'Not religious','Not religious','CURRENT','0f8d8f98-3efb-11'),
  (57,'Orthodox Christian','Orthodox Christian','CURRENT','0f8d9054-3efb-11'),
  (65,'Quaker','Quaker','CURRENT','0f8d9374-3efb-11'),
  (70,'Scientologist','Scientologist','CURRENT','0f8d976d-3efb-11'),
  (71,'Shinto','Shinto','CURRENT','0f8d97db-3efb-11'),
  (80,'Zoroastrian','Zzoroastrian','CURRENT','0f8d9b3a-3efb-11'),
  (297,'Advaitin Hindu','Advaitin Hindu','CURRENT','ab48f6f9-f0d2-11ed-9eb2-0638a616fc76'),
  (298,'Ahmadi','Ahmadi','CURRENT','b5cff023-f0d2-11ed-9eb2-0638a616fc76'),
  (299,'Amish','Amish','CURRENT','d64c0725-f0d2-11ed-9eb2-0638a616fc76'),
  (300,'Anabaptist','Anabaptist','CURRENT','dac82b1a-f0d2-11ed-9eb2-0638a616fc76'),
  (301,'Ancestral Worship','Ancestral Worship','CURRENT','df42a848-f0d2-11ed-9eb2-0638a616fc76'),
  (302,'Anglican','Anglican','CURRENT','e3fcb849-f0d2-11ed-9eb2-0638a616fc76'),
  (303,'Animist','Animist','CURRENT','e8b5f614-f0d2-11ed-9eb2-0638a616fc76'),
  (304,'Anthroposophist','Anthroposophist','CURRENT','ede0673d-f0d2-11ed-9eb2-0638a616fc76'),
  (151,'Apostolic Pentecostalist','Apostolic Pentecostalist','CURRENT','f2fb4786-f0d2-11ed-9eb2-0638a616fc76'),
  (152,'Armenian Catholic','Armenian Catholic','CURRENT','f92086de-f0d2-11ed-9eb2-0638a616fc76'),
  (153,'Armenian Orthodox','Armenian Orthodox','CURRENT','fdd50b14-f0d2-11ed-9eb2-0638a616fc76'),
  (154,'Arya Samaj Hindu','Arya Samaj Hindu','CURRENT','0373b8c6-f0d3-11ed-9eb2-0638a616fc76'),
  (155,'Asatruar','Asatruar','CURRENT','0ad06d74-f0d3-11ed-9eb2-0638a616fc76'),
  (156,'Ashkenazi Jew','Ashkenazi Jew','CURRENT','1113d271-f0d3-11ed-9eb2-0638a616fc76'),
  (157,"Baha'i","Baha'i",'CURRENT','1683b351-f0d3-11ed-9eb2-0638a616fc76'),
  (159,'Black Magic','Black Magic','CURRENT','26587ad8-f0d3-11ed-9eb2-0638a616fc76'),
  (160,'Brahma Kumari','Brahma Kumari','CURRENT','2d149edd-f0d3-11ed-9eb2-0638a616fc76'),
  (161,'Brethren','Brethren','CURRENT','32c5c803-f0d3-11ed-9eb2-0638a616fc76'),
  (162,'British Israelite','British Israelite','CURRENT','37aed0b7-f0d3-11ed-9eb2-0638a616fc76'),
  (163,'Buddhist','Buddhist','CURRENT','3fa788d5-f0d3-11ed-9eb2-0638a616fc76'),
  (164,'Bulgarian Orthodox','Bulgarian Orthodox','CURRENT','4444b0ca-f0d3-11ed-9eb2-0638a616fc76'),
  (165,'Calvinist','Calvinist','CURRENT','48a336db-f0d3-11ed-9eb2-0638a616fc76'),
  (166,'Catholic: Not Roman Catholic','Catholic: Not Roman Catholic','CURRENT','4de7ad4e-f0d3-11ed-9eb2-0638a616fc76'),
  (167,'Celtic Christian','Celtic Christian','CURRENT','537ee0d0-f0d3-11ed-9eb2-0638a616fc76'),
  (168,'Celtic Orthodox Christian','Celtic Orthodox Christian','CURRENT','584485ba-f0d3-11ed-9eb2-0638a616fc76'),
  (169,'Celtic Pagan','Celtic Pagan','CURRENT','5d014d1d-f0d3-11ed-9eb2-0638a616fc76'),
  (170,'Chinese Evangelical Christian','Chinese Evangelical Christian','CURRENT','62049368-f0d3-11ed-9eb2-0638a616fc76'),
  (171,'Chondogyo','Chondogyo','CURRENT','670c492d-f0d3-11ed-9eb2-0638a616fc76'),
  (172,'Christadelphian','Christadelphian','CURRENT','6c4eaaaa-f0d3-11ed-9eb2-0638a616fc76'),
  (174,'Christian Existentialist','Christian Existentialist','CURRENT','81a5709c-f0d3-11ed-9eb2-0638a616fc76'),
  (175,'Christian Humanist','Christian Humanist','CURRENT','8c2d9a78-f0d3-11ed-9eb2-0638a616fc76'),
  (176,'Christian Scientists','Christian Scientists','CURRENT','928e336c-f0d3-11ed-9eb2-0638a616fc76'),
  (177,'Christian Spiritualist','Christian Spiritualist','CURRENT','983159d1-f0d3-11ed-9eb2-0638a616fc76'),
  (178,'Church in Wales','Church in Wales','CURRENT','9c447f40-f0d3-11ed-9eb2-0638a616fc76'),
  (180,'Church of God of Prophecy','Church of God of Prophecy','CURRENT','ab4160b2-f0d3-11ed-9eb2-0638a616fc76'),
  (183,'Confucianist','Confucianist','CURRENT','c1e5d4b8-f0d3-11ed-9eb2-0638a616fc76'),
  (184,'Congregationalist','Congregationalist','CURRENT','c6ac437d-f0d3-11ed-9eb2-0638a616fc76'),
  (185,'Coptic Orthodox','Coptic Orthodox','CURRENT','cb759af0-f0d3-11ed-9eb2-0638a616fc76'),
  (186,'Deist','Deist','CURRENT','d07932df-f0d3-11ed-9eb2-0638a616fc76'),
  (187,'Druid','Druid','CURRENT','d4e68866-f0d3-11ed-9eb2-0638a616fc76'),
  (189,'Eastern Catholic','Eastern Catholic','CURRENT','e15ae14e-f0d3-11ed-9eb2-0638a616fc76'),
  (190,'Eastern Orthodox','Eastern Orthodox','CURRENT','e8cb832b-f0d3-11ed-9eb2-0638a616fc76'),
  (191,'Elim Pentecostalist','Elim Pentecostalist','CURRENT','ed404e39-f0d3-11ed-9eb2-0638a616fc76'),
  (192,'Ethiopian Orthodox','Ethiopian Orthodox','CURRENT','f1f0e563-f0d3-11ed-9eb2-0638a616fc76'),
  (193,'Evangelical Christian','Evangelical Christian','CURRENT','f774d2e1-f0d3-11ed-9eb2-0638a616fc76'),
  (194,'Exclusive Brethren','Exclusive Brethren','CURRENT','fbaee783-f0d3-11ed-9eb2-0638a616fc76'),
  (195,'Free Church','Free Church','CURRENT','0027ba04-f0d4-11ed-9eb2-0638a616fc76'),
  (196,'Free Church of Scotland','Free Church of Scotland','CURRENT','057a17c1-f0d4-11ed-9eb2-0638a616fc76'),
  (197,'Free Evangelical Presbyterian','Free Evangelical Presbyterian','CURRENT','0a279d25-f0d4-11ed-9eb2-0638a616fc76'),
  (198,'Free Methodist','Free Methodist','CURRENT','0fd24e1a-f0d4-11ed-9eb2-0638a616fc76'),
  (199,'Free Presbyterian','Free Presbyterian','CURRENT','16405204-f0d4-11ed-9eb2-0638a616fc76'),
  (200,'French Protestant','French Protestant','CURRENT','1b3744ce-f0d4-11ed-9eb2-0638a616fc76'),
  (201,'Goddess','Goddess','CURRENT','1fc92df6-f0d4-11ed-9eb2-0638a616fc76'),
  (203,'Haredi Jew','Haredi Jew','CURRENT','3829ed57-f0d4-11ed-9eb2-0638a616fc76'),
  (204,'Hasidic Jew','Hasidic Jew','CURRENT','3e25d4ec-f0d4-11ed-9eb2-0638a616fc76'),
  (205,'Heathen','Heathen','CURRENT','426ab939-f0d4-11ed-9eb2-0638a616fc76'),
  (207,'Humanist','Humanist','CURRENT','536d6990-f0d4-11ed-9eb2-0638a616fc76'),
  (208,'Independent Methodist','Independent Methodist','CURRENT','57d780d5-f0d4-11ed-9eb2-0638a616fc76'),
  (209,'Indian Orthodox','Indian Orthodox','CURRENT','5c5ecb41-f0d4-11ed-9eb2-0638a616fc76'),
  (210,'Infinite Way','Infinite Way','CURRENT','6133a37e-f0d4-11ed-9eb2-0638a616fc76'),
  (211,'Ismaili Muslim','Ismaili Muslim','CURRENT','660728e1-f0d4-11ed-9eb2-0638a616fc76'),
  (212,'Jain','Jain','CURRENT','6c0fdad0-f0d4-11ed-9eb2-0638a616fc76'),
  (213,"Jehovah's Witness","Jehovah's Witness",'CURRENT','712177bb-f0d4-11ed-9eb2-0638a616fc76'),
  (214,'Jewish','Jewish','CURRENT','78e71126-f0d4-11ed-9eb2-0638a616fc76'),
  (215,'Judaic Christian','Judaic Christian','CURRENT','7dcbd357-f0d4-11ed-9eb2-0638a616fc76'),
  (216,'Kabbalist','Kabbalist','CURRENT','82f6c7d9-f0d4-11ed-9eb2-0638a616fc76'),
  (217,'Liberal Jew','Liberal Jew','CURRENT','882553d7-f0d4-11ed-9eb2-0638a616fc76'),
  (218,'Lightworker','Lightworker','CURRENT','8dbd0fab-f0d4-11ed-9eb2-0638a616fc76'),
  (219,'Lutheran','Lutheran','CURRENT','9337812b-f0d4-11ed-9eb2-0638a616fc76'),
  (220,'Mahayana Buddhist','Mahayana Buddhist','CURRENT','99aa8644-f0d4-11ed-9eb2-0638a616fc76'),
  (221,'Masorti Jew','Masorti Jew','CURRENT','9eb261a0-f0d4-11ed-9eb2-0638a616fc76'),
  (222,'Mennonite','Mennonite','CURRENT','a43d5092-f0d4-11ed-9eb2-0638a616fc76'),
  (223,'Messianic Jew','Messianic Jew','CURRENT','a9218328-f0d4-11ed-9eb2-0638a616fc76'),
  (225,'Moravian','Moravian','CURRENT','b4e3e582-f0d4-11ed-9eb2-0638a616fc76'),
  (226,'Mormon','Mormon','CURRENT','b9617143-f0d4-11ed-9eb2-0638a616fc76'),
  (227,'Muslim','Muslim','CURRENT','c118bdff-f0d4-11ed-9eb2-0638a616fc76'),
  (228,'Native American Religion','Native American Religion','CURRENT','c7bd4584-f0d4-11ed-9eb2-0638a616fc76'),
  (229,'Nazarene Church Synonym: Nazarene','Nazarene Church Synonym: Nazarene','CURRENT','cd197956-f0d4-11ed-9eb2-0638a616fc76'),
  (230,'New Age Practitioner','New Age Practitioner','CURRENT','d4cd1c73-f0d4-11ed-9eb2-0638a616fc76'),
  (231,'New Kadampa Tradition Buddhist','New Kadampa Tradition Buddhist','CURRENT','d90d2581-f0d4-11ed-9eb2-0638a616fc76'),
  (232,'New Testament Pentacostalist','New Testament Pentacostalist','CURRENT','de4e5e6e-f0d4-11ed-9eb2-0638a616fc76'),
  (233,'Nichiren Buddhist','Nichiren Buddhist','CURRENT','e2b35a6f-f0d4-11ed-9eb2-0638a616fc76'),
  (234,'Nonconformist','Nonconformist','CURRENT','e7d4fd88-f0d4-11ed-9eb2-0638a616fc76'),
  (235,'Occultist','Occultist','CURRENT','ed275f37-f0d4-11ed-9eb2-0638a616fc76'),
  (236,'Old Catholic','Old Catholic','CURRENT','f26508f6-f0d4-11ed-9eb2-0638a616fc76'),
  (237,'Open Brethren','Open Brethren','CURRENT','f724eafc-f0d4-11ed-9eb2-0638a616fc76'),
  (239,'Orthodox Jew','Orthodox Jew','CURRENT','04f68133-f0d5-11ed-9eb2-0638a616fc76'),
  (240,'Pagan','Pagan','CURRENT','098b4d62-f0d5-11ed-9eb2-0638a616fc76'),
  (241,'Pantheist','Pantheist','CURRENT','0d8e66be-f0d5-11ed-9eb2-0638a616fc76'),
  (242,'Pentecostalist Synonym: Pentacostal Christian','Pentecostalist Synonym: Pentacostal Christian','CURRENT','12c8ee44-f0d5-11ed-9eb2-0638a616fc76'),
  (243,'Peyotist','Peyotist','CURRENT','177ae75d-f0d5-11ed-9eb2-0638a616fc76'),
  (244,'Plymouth Brethren','Plymouth Brethren','CURRENT','1c0c9447-f0d5-11ed-9eb2-0638a616fc76'),
  (246,'Protestant','Protestant','CURRENT','291bc1bd-f0d5-11ed-9eb2-0638a616fc76'),
  (247,'Pure Land Buddhist','Pure Land Buddhist','CURRENT','2d819d23-f0d5-11ed-9eb2-0638a616fc76'),
  (249,'Radha Soami Synonym: Sant Mat','Radha Soami Synonym: Sant Mat','CURRENT','3b2ff295-f0d5-11ed-9eb2-0638a616fc76'),
  (250,'Rastafari','Rastafari','CURRENT','3f320b92-f0d5-11ed-9eb2-0638a616fc76'),
  (251,'Reform Jew','Reform Jew','CURRENT','43999ce9-f0d5-11ed-9eb2-0638a616fc76'),
  (252,'Reformed Christian','Reformed Christian','CURRENT','48bd7d5a-f0d5-11ed-9eb2-0638a616fc76'),
  (253,'Reformed Presbyterian','Reformed Presbyterian','CURRENT','4f0e0d05-f0d5-11ed-9eb2-0638a616fc76'),
  (254,'Reformed Protestant','Reformed Protestant','CURRENT','54146ce2-f0d5-11ed-9eb2-0638a616fc76'),
  (255,'Religion (Other Not Listed)','Religion (Other Not Listed)','CURRENT','583b30f9-f0d5-11ed-9eb2-0638a616fc76'),
  (257,'Romanian Orthodox','Romanian Orthodox','CURRENT','6504b48b-f0d5-11ed-9eb2-0638a616fc76'),
  (258,'Russian Orthodox','Russian Orthodox','CURRENT','69913199-f0d5-11ed-9eb2-0638a616fc76'),
  (259,'Salvation Army Member','Salvation Army Member','CURRENT','6ec34f4e-f0d5-11ed-9eb2-0638a616fc76'),
  (260,'Santeri','Santeri','CURRENT','767d7ca6-f0d5-11ed-9eb2-0638a616fc76'),
  (261,'Satanist','Satanist','CURRENT','7a88af84-f0d5-11ed-9eb2-0638a616fc76'),
  (263,'Scottish Episcopalian','Scottish Episcopalian','CURRENT','85da8782-f0d5-11ed-9eb2-0638a616fc76'),
  (264,'Secularist','Secularist','CURRENT','8ae0150c-f0d5-11ed-9eb2-0638a616fc76'),
  (265,'Serbian Orthodox','Serbian Orthodox','CURRENT','8f899270-f0d5-11ed-9eb2-0638a616fc76'),
  (266,'Seventh Day Adventist','Seventh Day Adventist','CURRENT','961ae938-f0d5-11ed-9eb2-0638a616fc76'),
  (267,'Shakti Hindu','Shakti Hindu','CURRENT','9a1bc3ef-f0d5-11ed-9eb2-0638a616fc76'),
  (268,'Shaman','Shaman','CURRENT','9e95e7fb-f0d5-11ed-9eb2-0638a616fc76'),
  (269,"Shi'ite Muslim","Shi'ite Muslim",'CURRENT','a331fede-f0d5-11ed-9eb2-0638a616fc76'),
  (271,'Shiva Hindu','Shiva Hindu','CURRENT','b01e65ec-f0d5-11ed-9eb2-0638a616fc76'),
  (272,'Shumei','Shumei','CURRENT','b483d282-f0d5-11ed-9eb2-0638a616fc76'),
  (273,'Sikh','Sikh','CURRENT','b900bf81-f0d5-11ed-9eb2-0638a616fc76'),
  (274,'Spiritualist','Spiritualist','CURRENT','bd987ed0-f0d5-11ed-9eb2-0638a616fc76'),
  (275,'Sunni Muslim','Sunni Muslim','CURRENT','c1e967c3-f0d5-11ed-9eb2-0638a616fc76'),
  (276,'Swedenborgian Synonym: Neo-Christian','Swedenborgian Synonym: Neo-Christian','CURRENT','c60359d6-f0d5-11ed-9eb2-0638a616fc76'),
  (277,'Syrian Orthodox','Syrian Orthodox','CURRENT','cb04d192-f0d5-11ed-9eb2-0638a616fc76'),
  (278,'Taoist','Taoist','CURRENT','cf1edbe4-f0d5-11ed-9eb2-0638a616fc76'),
  (279,'Theravada Buddhist','Theravada Buddhist','CURRENT','d41f06a1-f0d5-11ed-9eb2-0638a616fc76'),
  (280,'Tibetan Buddhist','Tibetan Buddhist','CURRENT','d80365b7-f0d5-11ed-9eb2-0638a616fc76'),
  (281,'Ukrainian Catholic','Ukrainian Catholic','CURRENT','df40d17d-f0d5-11ed-9eb2-0638a616fc76'),
  (282,'Ukrainian Orthodox','Ukrainian Orthodox','CURRENT','e4a448ff-f0d5-11ed-9eb2-0638a616fc76'),
  (283,'Uniate Catholic','Uniate Catholic','CURRENT','ebe6f081-f0d5-11ed-9eb2-0638a616fc76'),
  (284,'Unitarian','Unitarian','CURRENT','f111272f-f0d5-11ed-9eb2-0638a616fc76'),
  (285,'Unitarian-Universalist','Unitarian-Universalist','CURRENT','f6836529-f0d5-11ed-9eb2-0638a616fc76'),
  (286,'United Reform','United Reform','CURRENT','fac539b6-f0d5-11ed-9eb2-0638a616fc76'),
  (287,'Universalist','Universalist','CURRENT','005e80a0-f0d6-11ed-9eb2-0638a616fc76'),
  (288,'Vaishnava Hindu Synonym: Hare Krishna','Vaishnava Hindu Synonym: Hare Krishna','CURRENT','072f8f2b-f0d6-11ed-9eb2-0638a616fc76'),
  (289,'Vodun','Vodun','CURRENT','0c055abc-f0d6-11ed-9eb2-0638a616fc76'),
  (290,'Wiccan','Wiccan','CURRENT','11a023b2-f0d6-11ed-9eb2-0638a616fc76'),
  (291,'Yoruba','Yoruba','CURRENT','16de1178-f0d6-11ed-9eb2-0638a616fc76'),
  (292,'Zen Buddhist','Zen Buddhist','CURRENT','1b95f497-f0d6-11ed-9eb2-0638a616fc76'),
  (294,'Zwinglian','Zwinglian','CURRENT','2de80b85-f0d6-11ed-9eb2-0638a616fc76'),
  (296,'Not Stated','Not Stated','CURRENT','39cc8d78-f0d6-11ed-9eb2-0638a616fc76');

UPDATE `ReligiousBelief`
SET `status` = 'CURRENT', `uuid` = '0f8d81a9-3efb-11'
WHERE	`code` = 'Baptist';

UPDATE `ReligiousBelief`
SET `status` = 'CURRENT', `uuid` = '0f8d8448-3efb-11'
WHERE	`code` = 'Church of Ireland';

UPDATE `ReligiousBelief`
SET `status` = 'CURRENT', `uuid` = '0f8d8be1-3efb-11'
WHERE	`code` = 'Methodist';

UPDATE `ReligiousBelief`
SET `status` = 'CURRENT', `uuid` = '0f8d9257-3efb-11'
WHERE	`code` = 'Presbyterian';

UPDATE `ReligiousBelief`
SET `status` = 'CURRENT', `uuid` = '0f8d9682-3efb-11'
WHERE	`code` = 'Roman Catholic';
 
-- NATIONALITY
INSERT IGNORE INTO `Nationality`(`id`, `countryNumber`, `nationality`, `status`, `uuid`)
VALUES
  (299,'Republic of Botswana','Batswana','CURRENT','43f29aee-a365-11'),
  (300,'Seychelles','Citizen of Seychelles','CURRENT','47b7fe94-e8f8-11ed-9eb2-0638a616fc76'),
  (301,'Filipino','Filipino','CURRENT','3bbdaa71-e988-11ed-9eb2-0638a616fc76'),
  (302,'Citizen of Seychelles',' Citizen of Seychelles','CURRENT','4f5613aa-e988-11');

-- SEXUAL ORIENTATION
INSERT IGNORE INTO `SexualOrientation`(`id`, `code`, `label`, `status`, `uuid`)
VALUES
  (434,'Heterosexual or Straight','Heterosexual or Straight','CURRENT','33e9fddd-f0d2-11ed-9eb2-0638a616fc76'),
  (435,'Gay or Lesbian','Gay or Lesbian','CURRENT','40744b16-f0d2-11ed-9eb2-0638a616fc76'),
  (436,'Other sexual orientation not listed','Other sexual orientation not listed','CURRENT','45d1d4e7-f0d2-11ed-9eb2-0638a616fc76'),
  (437,'Not Stated','Not Stated','CURRENT','4ad3f20b-f0d2-11ed-9eb2-0638a616fc76');
        
UPDATE `SexualOrientation`
SET `status` = 'INACTIVE'
WHERE	`id` IN (2,414,424);

-- MARITAL STATUS
INSERT IGNORE INTO `MaritalStatus`(`id`, `code`, `label`, `status`, `uuid`)
VALUES
  (20,'Married/Civil Partner','Married/Civil Partner','CURRENT','708687ce-f0d2-11ed-9eb2-0638a616fc76'),
  (21,'Divorced/Person whose Civil Partnership has been dissolved','Divorced/Person whose Civil Partnership has been dissolved','CURRENT','7836f402-f0d2-11ed-9eb2-0638a616fc76'),
  (22,'Widowed/Surviving Civil Partner','Widowed/Surviving Civil Partner','CURRENT','7de8a7c9-f0d2-11ed-9eb2-0638a616fc76'),
  (23,'Not Stated','Not Stated','CURRENT','868c97e3-f0d2-11ed-9eb2-0638a616fc76'),
  (24,'Cohabiting','Cohabiting','CURRENT','8ba20356-f0d2-11ed-9eb2-0638a616fc76'),
  (25,'Married (first marriage)','Married (first marriage)','CURRENT','906ba0a9-f0d2-11ed-9eb2-0638a616fc76'),
  (26,'Remarried','Remarried','CURRENT','95aebbf2-f0d2-11ed-9eb2-0638a616fc76'),
  (27,'Widow','Widow','CURRENT','9ac3dbb1-f0d2-11ed-9eb2-0638a616fc76');
        
UPDATE `MaritalStatus`
SET `status` = 'INACTIVE'
WHERE	`id` IN (2,3,8,12);
