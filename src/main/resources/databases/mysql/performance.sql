--https://medium.com/datadenys/using-explain-in-mysql-to-analyze-and-improve-query-performance-f58357deb2aa
--https://stackoverflow.com/questions/41534802/what-is-the-filtered-column-in-mysql-explain-telling-me-and-how-can-i-make-us
SELECT * FROM `op-research`.product;


EXPLAIN SELECT * FROM `op-research`.product
WHERE size='L' and product_line='T';

EXPLAIN SELECT * FROM `op-research`.product
WHERE (size='L' and product_line='T');

SELECT * FROM performance_schema.setup_actors;

UPDATE performance_schema.setup_actors
       SET ENABLED = 'NO', HISTORY = 'NO'
       WHERE HOST = '%' AND USER = '%';
INSERT INTO performance_schema.setup_actors
       (HOST,USER,ROLE,ENABLED,HISTORY)
       VALUES('localhost','test_user','%','YES','YES');

UPDATE performance_schema.setup_instruments
       SET ENABLED = 'YES', TIMED = 'YES'
       WHERE NAME LIKE '%statement/%';
UPDATE performance_schema.setup_instruments
       SET ENABLED = 'YES', TIMED = 'YES'
       WHERE NAME LIKE '%stage/%';

SET SQL_SAFE_UPDATES=0;
SELECT * FROM performance_schema.setup_consumers;
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_statements_%';
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_stages_%';

SELECT EVENT_ID, TRUNCATE(TIMER_WAIT/1000000000000,6) as Duration, SQL_TEXT FROM performance_schema.events_statements_history_long;

SELECT * FROM performance_schema.events_statements_history_long;

SELECT EVENT_ID, TRUNCATE(TIMER_WAIT/1000000000000,6) as Duration, SQL_TEXT
FROM performance_schema.events_statements_history_long
WHERE CURRENT_SCHEMA='op-research' and EVENT_NAME='statement/sql/select' AND SQL_TEXT like '%`op-research`.product%'
ORDER BY EVENT_ID;