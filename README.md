## table
```text
CREATE TABLE IF NOT EXISTS `t1` (
  `primary_key` int(11) NOT NULL AUTO_INCREMENT 
  `unique_key` varchar(100) CHARACTER SET utf8mb4 NOT NULL
  `col_not_index` decimal(19,0) NOT NULL
  `size` varchar(5) CHARACTER SET utf8mb4 DEFAULT NULL
  `part_key1` decimal(19,0) NOT NULL
  `part_key2` decimal(8, 0) DEFAULT NULL COMMENT
  `single_key` decimal(19,0) NOT NULL COMMENT
  `single_key1` int(11) DEFAULT NULL COMMENT
  PRIMARY KEY (`primary_key`),
  UNIQUE KEY `unique_key` (`unique_key`),
  INDEX single_key(`single_key`),
  KEY `single_key1` (`product_subcategory_id`),
  INDEX part_key1_key2(`part_key1`, `part_key2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `t2` (
  `id` int(11) NOT NULL
  `unique_key` varchar(100) CHARACTER SET utf8mb4 NOT NULL
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_key` (`unique_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='High-level product categorization.';
```

## Optimizing at the database level
Attempt | #1 | #2 | #3 | #4 | #5 | #6 | #7
--- | --- | --- | --- |--- |--- |--- |--- 
Seconds | 301 | 283 | 290 | 286 | 289 | 285 | 287


## Optimizing at the database level