| Duration(milisecs)| SQL text | Rows | Fitered | Extra          | Key | Type        |
|------------| -- |----|-- |-------------|- |-------------|
 0.67       |(q) SELECT * FROM t1 WHERE primary_key = 1|1|100.0| |PRIMARY| const       
 0.53       |(q) SELECT * FROM t1 WHERE unique_key = 'name 1'|0|0.0|no matching row in const table| | 
 0.41       |(q) SELECT * FROM t1 WHERE unique_key = 'name 1'|0|0.0|no matching row in const table| | 
 0.75       |(o) SELECT * FROM t1 WHERE primary_key = 1 AND unique_key = 'name 1'|0|0.0|Impossible WHERE noticed after reading const tables| | 
 0.53       |(q) SELECT * FROM t1 WHERE single_key = 50 OR single_key1 = 2|76937|100.0|Using union(single_key,single_key1); Using where|single_key,single_key1| index_merge 
 0.59       |(q) SELECT * FROM t1 WHERE single_key < 50 OR single_key1 < 2|191503|55.55|Using where| | ALL         
 0.58       |(q) SELECT * FROM t1 WHERE single_key = 50 AND single_key1 = 2|104|40.14|Using where|single_key| ref         
 0.74       |(q) SELECT * FROM t1 WHERE (single_key = 50 AND col_not_index = 50) OR single_key1 = 2|76937|100.0|Using union(single_key,single_key1); Using where|single_key,single_key1| index_merge 
 0.57       |(o) SELECT * FROM t1 WHERE primary_key < 50 AND single_key = 5|1|100.0|Using index condition|single_key| range       
 0.33       |(b) SELECT * FROM t1 WHERE col_not_index = 500|191503|10.0|Using where| | ALL         
 0.68       |(q) SELECT * FROM t1 WHERE single_key = 3|88|100.0| |single_key| ref         
 0.61       |(q) SELECT single_key FROM t1 WHERE single_key = 3|88|100.0|Using index|single_key| ref         
 0.52       |(q) SELECT * FROM t1 WHERE single_key = 3 OR col_not_index = 50|191503|19.0|Using where| | ALL         
 0.65       |(q) SELECT * FROM t1 WHERE single_key = 3 AND col_not_index = 50|88|10.0|Using where|single_key| ref         
 0.71       |(q) SELECT * FROM t1 WHERE single_key > 5|191503|50.0|Using where| | ALL         
 0.53       |(q) SELECT single_key FROM t1 WHERE single_key > 5|95751|100.0|Using where; Using index|single_key| range       
 0.52       |(q) SELECT * FROM t1 WHERE single_key < 5|317|100.0|Using index condition|single_key| range       
 0.56       |(o) SELECT single_key FROM t1 WHERE single_key < 5|317|100.0|Using where; Using index|single_key| range       
 0.41       |(b) SELECT * FROM t1 WHERE part_key2 = 12|191503|10.0|Using where| | ALL         
 0.36       |(b) SELECT * FROM t1 WHERE part_key1 = 1000 OR part_key2 = 12|191503|19.0|Using where| | ALL         
 0.50       |(b) SELECT * FROM t1 WHERE part_key1 = 500 OR col_not_index = 500|191503|19.0|Using where| | ALL         
 0.53       |(q) SELECT * FROM t1 WHERE part_key1 = 500|99|100.0| |single_key| ref         
 0.70       |(q) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 = 12|1|100.0| |part_key1_key2| ref         
 0.50       |(q) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 > 12|112|33.33|Using where|single_key| ref         
 0.36       |(o) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 < 12|6|100.0|Using index condition|part_key1_key2| range       
 0.70       |(b) SELECT * FROM t1 WHERE col_not_index >= 1 AND col_not_index < 5|191503|11.11|Using where| | ALL         
 0.57       |(q) SELECT * FROM t1 WHERE single_key >= 1 AND single_key < 5|317|100.0|Using index condition|single_key| range       
 0.50       |(q) SELECT * FROM t1 WHERE single_key >= 1 AND single_key < 5 AND col_not_index = 50|317|10.0|Using index condition; Using where|single_key| range       
 0.34       |(q) SELECT * FROM t1 WHERE single_key IN(1, 2, 3, 4)|317|100.0|Using index condition|single_key| range       
 0.40       |(q) SELECT * FROM t1 WHERE single_key = 1 OR single_key = 2|140|100.0|Using index condition|single_key| range       
 0.61       |(q) SELECT * FROM t1 WHERE single_key < 5|317|100.0|Using index condition|single_key| range       
 0.49       |(o) SELECT * FROM t1 WHERE single_key BETWEEN 1 AND 4|317|100.0|Using index condition|single_key| range       
 0.39       |(b) SELECT * FROM t1 WHERE single_key_as_string LIKE '%1'|191503|11.11|Using where| | ALL         
 0.38       |(o) SELECT * FROM t1 WHERE single_key_as_string LIKE 'name1%'|191503|50.0|Using where| | ALL         
 0.54       |(b) SELECT * FROM t1 ORDER BY single_key|191503|100.0|Using filesort| | ALL         
 0.47       |(q) SELECT single_key FROM t1 ORDER BY single_key|191503|100.0|Using index|single_key| index       
 0.41       |(q) SELECT * FROM t1 ORDER BY single_key DESC LIMIT 100|100|100.0|Backward index scan|single_key| index       
 0.66       |(b) SELECT * FROM t1 WHERE single_key > 5 ORDER BY single_key DESC|191503|50.0|Using where; Using filesort| | ALL         
 0.52       |(q) SELECT * FROM t1 WHERE single_key = 3 ORDER BY single_key DESC|88|100.0| |single_key| ref         
 0.48       |(b) SELECT single_key FROM t1 WHERE single_key = 3 ORDER BY single_key DESC|88|100.0|Using index|single_key| ref         
 0.40       |(b) SELECT * FROM t1 WHERE single_key < 5 ORDER BY single_key|317|100.0|Using index condition|single_key| range       
 0.37       |(o) SELECT single_key FROM t1 WHERE single_key < 5 ORDER BY single_key|317|100.0|Using where; Using index|single_key| range       
 0.58       |(q) SELECT * FROM t1 ORDER BY part_key1, part_key2|191503|100.0|Using filesort| | ALL         
 0.52       |(q) SELECT * FROM t1 WHERE part_key1 = 2 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100|97|100.0|Backward index scan|part_key1_key2| ref         
 0.33       |(q) SELECT * FROM t1 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100|100|100.0|Backward index scan|part_key1_key2| index       
 0.33       |(q) SELECT * FROM t1 ORDER BY part_key1 ASC, part_key2 ASC LIMIT 100|100|100.0| |part_key1_key2| index       
 0.53       |(q) SELECT * FROM t1 WHERE part_key1 = 2 ORDER BY part_key2|97|100.0| |part_key1_key2| ref         
 0.58       |(q) SELECT * FROM t1 WHERE part_key1 = 2 AND part_key2 > 12 ORDER BY part_key2|93|100.0|Using index condition|part_key1_key2| range       
 0.60       |(o) SELECT * FROM t1 WHERE part_key1 = 2 AND part_key2 < 12 ORDER BY part_key2|3|100.0|Using index condition|part_key1_key2| range       
 0.39       |(b) SELECT col_not_index FROM t1 WHERE col_not_index > 5 GROUP BY col_not_index|191503|33.33|Using where; Using temporary| | ALL         
 0.40       |(b) SELECT * FROM t1 GROUP BY single_key|191503|100.0| |single_key| index       
 0.61       |(b) SELECT single_key FROM t1 GROUP BY single_key|1936|100.0|Using index for group-by|part_key1_key2| range       
 0.44       |(b) SELECT * FROM t1 WHERE single_key < 5 GROUP BY single_key|317|100.0|Using index condition|single_key| range       
 0.46       |(b) SELECT single_key FROM t1 WHERE single_key < 5 GROUP BY single_key|317|100.0|Using where; Using index|single_key| range       
 0.39       |(b) SELECT * FROM t1 WHERE single_key = 5 GROUP BY single_key|97|100.0| |single_key| ref         
 0.39       |(b) SELECT single_key FROM t1 WHERE single_key = 5 GROUP BY single_key|97|100.0|Using index|single_key| ref         
 0.71       |(b) SELECT * FROM t1 WHERE single_key > 5 GROUP BY single_key|191503|50.0|Using where|single_key| index       
 0.57       |(b) SELECT single_key FROM t1 WHERE single_key > 5 GROUP BY single_key|1043|100.0|Using where; Using index for group-by|single_key| range       
 0.46       |(b) SELECT id, single_key FROM t1 WHERE single_key > 5 GROUP BY single_key|95751|100.0|Using where; Using index|single_key| range       
 0.60       |(o) SELECT id, single_key, col_not_index FROM t1 WHERE single_key > 5 GROUP BY single_key|191503|50.0|Using where|single_key| index       
 0.53       |(b) SELECT DISTINCT single_key FROM t1|1936|100.0|Using index for group-by|part_key1_key2| range       
 0.34       |(b) SELECT COUNT(DISTINCT single_key) FROM t1|1936|100.0|Using index for group-by|part_key1_key2| range       
 0.25       |(b) SELECT SUM(single_key) FROM t1|191503|100.0|Using index|single_key| index       
 0.28       |(b) SELECT SUM(single_key) FROM t1 GROUP BY single_key|191503|100.0|Using index|single_key| index       
 0.37       |(o) SELECT MIN(single_key) FROM t1|0|0.0|Select tables optimized away| | 
 0.66       |(b) SELECT * FROM t1 GROUP BY part_key1, part_key2|191503|100.0| |part_key1_key2| index       
 0.36       |(b) SELECT part_key1, part_key2 FROM t1 GROUP BY part_key1, part_key2|191503|100.0|Using index|part_key1_key2| index       
 0.34       |(b) SELECT part_key1, part_key2, col_not_index FROM t1 GROUP BY part_key1, part_key2|191503|100.0| |part_key1_key2| index       
 0.45       |(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key1, part_key2|97|100.0| |part_key1_key2| ref         
 0.73       |(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 < 5 GROUP BY part_key1, part_key2|317|100.0|Using where; Using index|part_key1_key2| range       
 0.48       |(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 > 5 GROUP BY part_key1, part_key2|95751|100.0|Using where; Using index|part_key1_key2| range       
 0.55       |(b) SELECT part_key1 FROM t1 WHERE part_key1 > 5 GROUP BY part_key1, part_key2|95751|100.0|Using where; Using index|part_key1_key2| range       
 0.56       |(b) SELECT part_key1 FROM t1 WHERE part_key1 = 5 AND part_key2 > 12 GROUP BY part_key1, part_key2|91|100.0|Using where; Using index|part_key1_key2| range       
 0.74       |(o) SELECT part_key1 FROM t1 WHERE part_key1 = 5 GROUP BY part_key1, part_key2|97|100.0|Using index|part_key1_key2| ref         
 0.47       |(b) SELECT * FROM t1 WHERE part_key2 = 5 GROUP BY part_key1|191503|10.0|Using where|single_key| index       
 0.42       |(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key1|97|100.0| |single_key| ref         
 0.29       |(q) SELECT part_key1, id FROM t1 WHERE part_key2 = 5 GROUP BY part_key1|191503|10.0|Using where; Using index|part_key1_key2| index       
 0.33       |(q) SELECT part_key1, part_key2 FROM t1 WHERE part_key2 = 5 GROUP BY part_key1|1936|100.0|Using where; Using index for group-by|part_key1_key2| range       
 0.53       |(q) SELECT part_key1, MIN(part_key2) FROM t1 GROUP BY part_key1|1936|100.0|Using index for group-by|part_key1_key2| range       
 0.39       |(q) SELECT part_key1, part_key2 FROM t1 WHERE col_not_index = 5 GROUP BY part_key1|191503|10.0|Using where|single_key| index       
 0.31       |(o) SELECT part_key1 FROM t1 WHERE part_key2 = 5 GROUP BY part_key1|1936|100.0|Using where; Using index for group-by|part_key1_key2| range       
 0.33       |(b) SELECT * FROM t1 GROUP BY part_key2|191503|100.0|Using temporary| | ALL         
 0.37       |(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key2|97|100.0| |part_key1_key2| ref         
 0.54       |(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2|97|100.0|Using index|part_key1_key2| ref         
 0.65       |(b) SELECT part_key2 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2|97|100.0|Using index|part_key1_key2| ref         
 0.57       |(b) SELECT part_key2 FROM t1 WHERE part_key1 > 5 GROUP BY part_key2|95751|100.0|Using where; Using index; Using temporary|part_key1_key2| range       
 0.44       |(b) SELECT part_key2 FROM t1 WHERE part_key1 < 5 GROUP BY part_key2|317|100.0|Using where; Using index; Using temporary|part_key1_key2| range       
 0.34       |(b) SELECT part_key1 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2|97|100.0|Using index|part_key1_key2| ref         
 0.77       |(o) SELECT * FROM t1 GROUP BY part_key2|191503|100.0|Using temporary| | ALL         
