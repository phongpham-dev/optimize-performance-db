## Table table
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
## Chú thích
- duration: thời gian chạy(milliseconds)
- Rows: số lượng row được kiểm tra
- Filtered: phần trăm row được lọc(Rows * Filtered)
- Key: Index được sử dụng
- Type:
    + ALL: quét qua toàn bộ Table
    + const: quét qua Const Table(sử dụng cho primary key và unique index)
    + ref: sử dụng Index để chọn row phù hợp(sử dụng cho cột không phải primary key và unique index và cột Index so sánh với constant sử dụng =)
    + range: sử dụng Index để chọn row phù hợp theo range(sử dụng cho cột Index so sánh với constant sử dụng <, >, IN, LIKE, BETWEEN)
    + index: tương tự All, nhưng quét trong **Index Tree**(khi query chỉ chọn những cột là cột Index)
    + index_merge: sử dụng Index Merge optimization
- Extra: thông tin bổ sung
  + Using where: lọc ra những giá trị phù hợp
  + Using filesort: sort toàn bộ Table
  + Using index: thông tin cột được lấy trong Index Tree không có thực hiện tìm kiếm bổ sụng để đọc row thực tế
  + Using index for group-by: tương tự **Using index**
  + Using index condition: các Table được đọc bằng cách truy cập các bộ chỉ mục và kiểm tra chúng trước để xác định xem có đọc toàn bộ các hàng trong bảng hay không
  