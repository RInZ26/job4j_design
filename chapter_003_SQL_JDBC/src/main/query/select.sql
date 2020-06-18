--1. Написать запрос получение всех продуктов с типом "СЫР"
SELECT p.id, p.name, p.type_id, p.expired_date, p.price FROM products AS p
INNER JOIN types AS t ON p.type_id = t.id
WHERE t.name = 'СЫР';

-- 2. Написать запрос получения всех продуктов, у кого в имени есть слово "мороженное"
SELECT * FROM products AS p
WHERE p.name LIKE '%мороженное%';

-- 3. Написать запрос, который выводит все продукты, срок годности которых заканчивается в следующем месяце.
SELECT * FROM products AS p
WHERE p.expired_date BETWEEN '2020-07-01 00:00:00' AND '2020-08-01 00:00:00'

-- 4. Написать запрос, который выводит самый дорогой продукт.
SELECT p.id, p.name, p.type_id, p.expired_date, p.price FROM products AS p
WHERE p.price = (SELECT MAX(p.price) FROM products AS p)

-- 5. Написать запрос, который выводит количество всех продуктов определенного типа.
SELECT t.name, p.count FROM types AS t
INNER JOIN (SELECT COUNT(*), p.type_id FROM products AS p GROUP BY p.type_id) AS p ON p.type_id = t.id

-- 6. Написать запрос получение всех продуктов с типом "СЫР" и "МОЛОКО"
SELECT p.id, p.name, p.expired_date FROM products AS p
INNER JOIN types AS t ON p.type_id = t.id
WHERE t.name IN ('СЫР', 'МОЛОКО')

-- 7. Написать запрос, который выводит тип продуктов, которых осталось меньше 10 штук.
SELECT t.name, p.count FROM types AS t
INNER JOIN (SELECT COUNT(*), p.type_id FROM products AS p GROUP BY p.type_id) AS p ON t.id = p.type_id
WHERE p.count <= 10

-- 8. Вывести все продукты и их тип.
SELECT p.id as products_id, p.name, p.expired_date, t.id as type_id, t.name FROM products AS p
INNER JOIN types AS t ON p.type_id = t.id