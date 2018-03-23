
UPDATE GmcStatus set code = 'Not Registered - Erased for false declaration',
label = 'Not Registered - Erased for false declaration'
where code = 'Not Registered – Erased for false declaration';

UPDATE GmcStatus set code = 'Not Registered - Erased for fraudulent application',
label = 'Not Registered - Erased for fraudulent application'
where code = 'Not Registered – Erased for fraudulent application';

UPDATE GmcStatus set code = 'Not Registered - Provisional registration expired',
label = 'Not Registered - Provisional registration expired'
where code = 'Not Registered – Provisional registration expired';
