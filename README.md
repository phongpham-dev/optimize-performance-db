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
<html>
<meta >
<head>
    <title> SQL monitor - Realtime Profiler</title>
</head>
<body>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">constant table</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>1.75</td>
        <td width="500">(q) SELECT * FROM t1 WHERE primary_key &#x3D; 1</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>PRIMARY</td>
        <td>const</td>
    </tr>
    <tr>
        <td>0.74</td>
        <td width="500">(q) SELECT * FROM t1 WHERE unique_key &#x3D; &#39;name1&#39;</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>unique_key</td>
        <td>const</td>
    </tr>
    <tr>
        <td>0.61</td>
        <td width="500">(q) SELECT * FROM t1 WHERE unique_key &#x3D; &#39;name1&#39;</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>unique_key</td>
        <td>const</td>
    </tr>
    <tr>
        <td>0.69</td>
        <td width="500">(o) SELECT * FROM t1 WHERE primary_key &#x3D; 1 AND unique_key &#x3D; &#39;name1&#39;</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>PRIMARY</td>
        <td>const</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">index merge optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>1.11</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 50 OR single_key1 &#x3D; 2</td>
        <td>76937</td>
        <td>100.0</td>
        <td>Using union(single_key,
single_key1),
 Using where</td>
        <td>single_key,
single_key1</td>
        <td>index_merge</td>
    </tr>
    <tr>
        <td>1.26</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 50 OR single_key1 &lt; 2</td>
        <td>191503</td>
        <td>55.55</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.57</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 50 AND single_key1 &#x3D; 2</td>
        <td>104</td>
        <td>40.14</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.42</td>
        <td width="500">(q) SELECT * FROM t1 WHERE (single_key &#x3D; 50 AND col_not_index &#x3D; 50) OR single_key1 &#x3D; 2</td>
        <td>76937</td>
        <td>100.0</td>
        <td>Using union(single_key,
single_key1),
 Using where</td>
        <td>single_key,
single_key1</td>
        <td>index_merge</td>
    </tr>
    <tr>
        <td>0.50</td>
        <td width="500">(o) SELECT * FROM t1 WHERE primary_key &lt; 50 AND single_key &#x3D; 5</td>
        <td>1</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">WHERE optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>0.25</td>
        <td width="500">(b) SELECT * FROM t1 WHERE col_not_index &#x3D; 500</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3</td>
        <td>88</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &#x3D; 3</td>
        <td>88</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 OR col_not_index &#x3D; 50</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.25</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 AND col_not_index &#x3D; 50</td>
        <td>88</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.32</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &gt; 5</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &gt; 5</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key2 &#x3D; 12</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 OR part_key2 &#x3D; 12</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 500 OR col_not_index &#x3D; 500</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 500</td>
        <td>99</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &#x3D; 12</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &gt; 12</td>
        <td>112</td>
        <td>33.33</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.28</td>
        <td width="500">(o) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &lt; 12</td>
        <td>6</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">RANGE optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT * FROM t1 WHERE col_not_index &gt;&#x3D; 1 AND col_not_index &lt; 5</td>
        <td>191503</td>
        <td>11.11</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &gt;&#x3D; 1 AND single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &gt;&#x3D; 1 AND single_key &lt; 5 AND col_not_index &#x3D; 50</td>
        <td>317</td>
        <td>10.0</td>
        <td>Using index condition,
 Using where</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key IN(1, 2, 3, 4)</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 1 OR single_key &#x3D; 2</td>
        <td>140</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(o) SELECT * FROM t1 WHERE single_key BETWEEN 1 AND 4</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">LIKE optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key_as_string LIKE &#39;%1&#39;</td>
        <td>191503</td>
        <td>11.11</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.34</td>
        <td width="500">(o) SELECT * FROM t1 WHERE single_key_as_string LIKE &#39;name1%&#39;</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">ORDER BY optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT * FROM t1 ORDER BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(q) SELECT single_key FROM t1 ORDER BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY single_key DESC LIMIT 100</td>
        <td>100</td>
        <td>100.0</td>
        <td>Backward index scan</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &gt; 5 ORDER BY single_key DESC</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where,
 Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 ORDER BY single_key DESC</td>
        <td>88</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.15</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &#x3D; 3 ORDER BY single_key DESC</td>
        <td>88</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &lt; 5 ORDER BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &lt; 5 ORDER BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100</td>
        <td>97</td>
        <td>100.0</td>
        <td>Backward index scan</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100</td>
        <td>100</td>
        <td>100.0</td>
        <td>Backward index scan</td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY part_key1 ASC, part_key2 ASC LIMIT 100</td>
        <td>100</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.25</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 ORDER BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 AND part_key2 &gt; 12 ORDER BY part_key2</td>
        <td>93</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(o) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 AND part_key2 &lt; 12 ORDER BY part_key2</td>
        <td>3</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    </tbody>
</table>
<br/>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:5%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:paleturquoise;">GROUP BY optimization</th>
    </tr>
    <tr style="background-color: aliceblue">
        <th>Duration<br/>(milisecs)</th>
        <th>SQL text</th>
        <th>Rows</th>
        <th>Fitered</th>
        <th style="width:10%">Extra</th>
        <th style="width:10%">Key</th>
        <th style="width:10%">Type</th>
    </tr>
    </thead>
    <tbody style="text-align: right">
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT col_not_index FROM t1 WHERE col_not_index &gt; 5 GROUP BY col_not_index</td>
        <td>191503</td>
        <td>33.33</td>
        <td>Using where,
 Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT * FROM t1 GROUP BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT single_key FROM t1 GROUP BY single_key</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &lt; 5 GROUP BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &lt; 5 GROUP BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &#x3D; 5 GROUP BY single_key</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &#x3D; 5 GROUP BY single_key</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.25</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>1043</td>
        <td>100.0</td>
        <td>Using where,
 Using index for group-by</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT id, single_key FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(q) SELECT id, single_key, col_not_index FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.16</td>
        <td width="500">(b) SELECT DISTINCT single_key FROM t1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT COUNT(DISTINCT single_key) FROM t1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT SUM(single_key) FROM t1</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT SUM(single_key) FROM t1 GROUP BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(q) SELECT MIN(single_key) FROM t1</td>
        <td>0</td>
        <td>0.0</td>
        <td>Select tables optimized away</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(b) SELECT * FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.16</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.14</td>
        <td width="500">(b) SELECT part_key1, part_key2, col_not_index FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1, part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &lt; 5 GROUP BY part_key1, part_key2</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key1, part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key1, part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 AND part_key2 &gt; 12 GROUP BY part_key1, part_key2</td>
        <td>91</td>
        <td>100.0</td>
        <td>Using where,
 Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1, part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(q) SELECT part_key1, id FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where,
 Using index</td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT part_key1, part_key2 FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using where,
 Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(q) SELECT part_key1, MIN(part_key2) FROM t1 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.16</td>
        <td width="500">(q) SELECT part_key1, part_key2 FROM t1 WHERE col_not_index &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.16</td>
        <td width="500">(q) SELECT part_key1 FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using where,
 Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(b) SELECT * FROM t1 GROUP BY part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where,
 Using index,
 Using temporary</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &lt; 5 GROUP BY part_key2</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where,
 Using index,
 Using temporary</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(o) SELECT * FROM t1 GROUP BY part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    </tbody>
</table>
<br/>
</body>
</html>

