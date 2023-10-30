EXPLAIN SELECT * FROM `op-research`.product LIMIT 100000;
EXPLAIN SELECT id,product_line,size FROM `op-research`.product LIMIT 100000;

EXPLAIN SELECT *
FROM `op-research`.product
WHERE name like '%name';

SELECT COUNT(*)
FROM `op-research`.product;

EXPLAIN SELECT COUNT(*)
FROM `op-research`.product
WHERE name like 'name%'
LIMIT 5000;


EXPLAIN SELECT DISTINCT product_line
FROM `op-research`.product
WHERE product_line IN('M', 'R');

EXPLAIN SELECT product_line
FROM `op-research`.product
WHERE product_line IN('M', 'R')
GROUP BY product_line;

EXPLAIN SELECT * FROM `op-research`.product
WHERE name < 'name 7';

EXPLAIN SELECT * FROM `op-research`.product
WHERE name = 'name 7' OR name = 'name 8';

CREATE INDEX ind on `op-research`.product(product_line);

EXPLAIN SELECT COUNT(*) FROM `op-research`.product
WHERE product_line = 'M' OR product_line = 'L'
LIMIT 200000000;

SHOW INDEX FROM `op-research`.product;

EXPLAIN SELECT * from `op-research`.product
GROUP BY id, product_number, name, product_line;

SELECT COUNT(*) from `op-research`.product;

EXPLAIN SELECT COUNT(*) from `op-research`.product
WHERE product_line = 'M'
GROUP BY product_line;

EXPLAIN SELECT * from `op-research`.product
WHERE size = 'M'
LIMIT 100000000;

EXPLAIN SELECT COUNT(*) from `op-research`.product
WHERE size = 'M'
GROUP BY size;

EXPLAIN SELECT * from `op-research`.product
GROUP BY name;

SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
