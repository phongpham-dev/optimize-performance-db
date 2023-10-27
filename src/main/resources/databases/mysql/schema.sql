CREATE DATABASE IF NOT EXISTS `op-research` CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `op-research`;

--DROP TABLE IF EXISTS product;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Primary key for Product records.',
  `name` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Name of the product.',
  `product_number` varchar(25) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Unique product identification number.',
  `make_flag` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0 = Product is purchased, 1 = Product is manufactured in-house.',
  `color` varchar(15) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Product color.',
  `list_price` decimal(19,4) NOT NULL COMMENT 'Selling price.',
  `size` varchar(5) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Product size.',
  `weight` decimal(8,2) DEFAULT NULL COMMENT 'Product weight.',
  `product_line` char(2) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'R = Road, M = Mountain, T = Touring, S = Standard',
  `product_subcategory_id` int(11) DEFAULT NULL COMMENT 'Product is a member of this product subcategory. Foreign key to ProductSubCategory.ProductSubCategoryID. ',
  `discontinued_date` bigint(20) DEFAULT '0' COMMENT 'Date the product was discontinued.',
  `modified_date` bigint(20) NOT NULL DEFAULT '0' COMMENT 'Date and time the record was last updated.',
  PRIMARY KEY (`id`),
  UNIQUE KEY `AK_Product_ProductNumber` (`product_number`),
  UNIQUE KEY `AK_Product_Name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Products sold or used in the manfacturing of sold products.';

SET SQL_SAFE_UPDATES=0;
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_statements_%';
UPDATE performance_schema.setup_consumers
       SET ENABLED = 'YES'
       WHERE NAME LIKE '%events_stages_%';