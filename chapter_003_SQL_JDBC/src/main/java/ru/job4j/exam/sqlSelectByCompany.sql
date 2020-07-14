

SELECT p.name, c.name FROM person AS p
JOIN company AS c ON p.company_id = c.id
WHERE p.company_id != 5;

SELECT c.name, COUNT(p.name) FROM company AS c
JOIN person AS p ON p.company_id = c.id
GROUP BY c.name
ORDER BY COUNT(p.name) DESC
LIMIT 1;
