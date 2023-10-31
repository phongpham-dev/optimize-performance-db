CREATE DATABASE IF NOT EXISTS `op-research` CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `op-research`;

--DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key for Product records.',
  `name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Name of the product.',
  `product_number` varchar(25) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Unique product identification number.',
  `make_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 = Product is purchased, 1 = Product is manufactured in-house.',
  `color` varchar(15) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Product color.',
  `list_price` decimal(19,0) NOT NULL COMMENT 'Selling price.',
  `col_not_index` decimal(19,0) NOT NULL COMMENT 'Col not index.',
  `size` varchar(5) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Product size.',
  `weight` decimal(8, 0) DEFAULT NULL COMMENT 'Product weight.',
  `product_line` char(2) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'R = Road, M = Mountain, T = Touring, S = Standard',
  `product_subcategory_id` int(11) DEFAULT NULL COMMENT 'Product is a member of this product subcategory. Foreign key to ProductSubCategory.ProductSubCategoryID. ',
  `discontinued_date` bigint(20) DEFAULT '0' COMMENT 'Date the product was discontinued.',
  `modified_date` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Date and time the record was last updated.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `k_product_number` (`product_number`),
  UNIQUE KEY `unique_key` (`name`),
  INDEX product_line_size (`product_line`, `size`),
  INDEX product_line_size_w (`product_line`, `size`, `weight`),
  INDEX key1(`list_price`),
  INDEX part_key1_key2(`list_price`, `weight`),
  KEY `key2` (`product_subcategory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Products sold or used in the manfacturing of sold products.';

DROP TABLE IF EXISTS category;

CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL COMMENT 'Primary key for ProductCategory records.',
  `name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Category description.',
  `modified_date` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Date and time the record was last updated.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='High-level product categorization.';

INSERT INTO `category` VALUES
(1,'Bikes','1698547768018'),
(2,'Components','1698547768018'),
(3,'Clothing','1698547768018'),
(4,'Accessories','1698547768018');

SET SQL_SAFE_UPDATES=0;
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_statements_%';
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_stages_%';
SET profiling = 1;
SET sql_mode = '';