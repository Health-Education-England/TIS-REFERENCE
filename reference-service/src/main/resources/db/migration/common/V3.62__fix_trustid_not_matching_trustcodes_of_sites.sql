UPDATE `Site` site
INNER JOIN `Trust` trust ON site.trustCode = trust.code AND site.status = trust.status
SET site.trustId = trust.id
WHERE site.trustId != trust.id AND site.status = 'CURRENT';
