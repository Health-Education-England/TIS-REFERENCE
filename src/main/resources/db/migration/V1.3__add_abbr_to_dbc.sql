ALTER TABLE DBC ADD abbr varchar(50);

INSERT INTO DBC (dbc,name,abbr) VALUES
('1-85KJU0', 'Kent, Surrey and Sussex', 'KSS');

UPDATE DBC
SET abbr='EOE'
WHERE dbc='1-AIIDWT';
