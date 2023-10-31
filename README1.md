## Define table
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
- Duration: thời gian chạy
- Rows: số lượng row được duyệt
- Key: Index được sử dụng
- Extra:
  + Using where: duyệt qua toàn bộ table gốc
  + Using filesort: sort toàn bộ table gốc
  + Using index condition: duyệt qua Index Tree và table gốc để lọc giá trị phù hợp
  + Using index: duyệt qua Index Tree
  + Using union: duyệt đồng thời Index Tree sau đó merge lại
- Type:
  + ALL: duyệt qua toàn bộ table gốc
  + const: duyệt qua const table(dùng cho primary key và unique_key)
  + range: duyệt qua Index Tree và lọc theo range
  + ref: Lọc rows phù hợp với giá trị
  + index_merge: 