<html>
<meta >
<head>
    <title> SQL monitor - Realtime Profiler</title>
    <style>
        td:nth-child(2) {
            width: 50%;
      }
    </style>
</head>
<body>
<table style="border-collapse:collapse;" border=1 cellpadding=5 cellspacing=5 width="100%">
    <col style="width:10%">
    <col style="width:40%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <thead>
    <tr>
        <th colspan="7" style="background-color:aliceblue;">Request Profiling</th>
    </tr>
    <tr style="background-color: paleturquoise">
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
        <td>0.43</td>
        <td style="text-align:left">(q) SELECT * FROM t1 WHERE primary_key &#x3D; 1</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>PRIMARY</td>
        <td>const</td>
    </tr>
    <tr>
        <td>0.28</td>
        <td width="500">(q) SELECT * FROM t1 WHERE unique_key &#x3D; &#39;name 1&#39;</td>
        <td>0</td>
        <td>0.0</td>
        <td>no matching row in const table</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(q) SELECT * FROM t1 WHERE unique_key &#x3D; &#39;name 1&#39;</td>
        <td>0</td>
        <td>0.0</td>
        <td>no matching row in const table</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(o) SELECT * FROM t1 WHERE primary_key &#x3D; 1 AND unique_key &#x3D; &#39;name 1&#39;</td>
        <td>0</td>
        <td>0.0</td>
        <td>Impossible WHERE noticed after reading const tables</td>
        <td></td>
        <td></td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.60</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 50 OR single_key1 &#x3D; 2</td>
        <td>76937</td>
        <td>100.0</td>
        <td>Using union(single_key,single_key1); Using where</td>
        <td>single_key,single_key1</td>
        <td>index_merge</td>
    </tr>
    <tr>
        <td>1.04</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 50 OR single_key1 &lt; 2</td>
        <td>191503</td>
        <td>55.55</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.38</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 50 AND single_key1 &#x3D; 2</td>
        <td>104</td>
        <td>40.14</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.37</td>
        <td width="500">(q) SELECT * FROM t1 WHERE (single_key &#x3D; 50 AND col_not_index &#x3D; 50) OR single_key1 &#x3D; 2</td>
        <td>76937</td>
        <td>100.0</td>
        <td>Using union(single_key,single_key1); Using where</td>
        <td>single_key,single_key1</td>
        <td>index_merge</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(o) SELECT * FROM t1 WHERE primary_key &lt; 50 AND single_key &#x3D; 5</td>
        <td>1</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT * FROM t1 WHERE col_not_index &#x3D; 500</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.28</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3</td>
        <td>88</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.28</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &#x3D; 3</td>
        <td>88</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.28</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 OR col_not_index &#x3D; 50</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 AND col_not_index &#x3D; 50</td>
        <td>88</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.37</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &gt; 5</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(q) SELECT single_key FROM t1 WHERE single_key &gt; 5</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.19</td>
        <td width="500">(o) SELECT single_key FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key2 &#x3D; 12</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 OR part_key2 &#x3D; 12</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 500 OR col_not_index &#x3D; 500</td>
        <td>191503</td>
        <td>19.0</td>
        <td>Using where</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.32</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 500</td>
        <td>99</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.32</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &#x3D; 12</td>
        <td>1</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &gt; 12</td>
        <td>112</td>
        <td>33.33</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(o) SELECT * FROM t1 WHERE part_key1 &#x3D; 1000 AND part_key2 &lt; 12</td>
        <td>6</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.20</td>
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
        <td>0.25</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &gt;&#x3D; 1 AND single_key &lt; 5 AND col_not_index &#x3D; 50</td>
        <td>317</td>
        <td>10.0</td>
        <td>Using index condition; Using where</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key IN(1, 2, 3, 4)</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 1 OR single_key &#x3D; 2</td>
        <td>140</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &lt; 5</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(o) SELECT * FROM t1 WHERE single_key BETWEEN 1 AND 4</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.21</td>
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
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.16</td>
        <td width="500">(b) SELECT * FROM t1 ORDER BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.25</td>
        <td width="500">(q) SELECT single_key FROM t1 ORDER BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY single_key DESC LIMIT 100</td>
        <td>100</td>
        <td>100.0</td>
        <td>Backward index scan</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &gt; 5 ORDER BY single_key DESC</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where; Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT * FROM t1 WHERE single_key &#x3D; 3 ORDER BY single_key DESC</td>
        <td>88</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.17</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &#x3D; 3 ORDER BY single_key DESC</td>
        <td>88</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &lt; 5 ORDER BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(o) SELECT single_key FROM t1 WHERE single_key &lt; 5 ORDER BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(q) SELECT * FROM t1 ORDER BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using filesort</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100</td>
        <td>97</td>
        <td>100.0</td>
        <td>Backward index scan</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.32</td>
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
        <td>0.29</td>
        <td width="500">(q) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 AND part_key2 &gt; 12 ORDER BY part_key2</td>
        <td>93</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.25</td>
        <td width="500">(o) SELECT * FROM t1 WHERE part_key1 &#x3D; 2 AND part_key2 &lt; 12 ORDER BY part_key2</td>
        <td>3</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(b) SELECT col_not_index FROM t1 WHERE col_not_index &gt; 5 GROUP BY col_not_index</td>
        <td>191503</td>
        <td>33.33</td>
        <td>Using where; Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.21</td>
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
        <td>0.30</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &lt; 5 GROUP BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using index condition</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &lt; 5 GROUP BY single_key</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &#x3D; 5 GROUP BY single_key</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &#x3D; 5 GROUP BY single_key</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(b) SELECT * FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.37</td>
        <td width="500">(b) SELECT single_key FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>1043</td>
        <td>100.0</td>
        <td>Using where; Using index for group-by</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT id, single_key FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>single_key</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.35</td>
        <td width="500">(o) SELECT id, single_key, col_not_index FROM t1 WHERE single_key &gt; 5 GROUP BY single_key</td>
        <td>191503</td>
        <td>50.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT DISTINCT single_key FROM t1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(b) SELECT COUNT(DISTINCT single_key) FROM t1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT SUM(single_key) FROM t1</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.24</td>
        <td width="500">(b) SELECT SUM(single_key) FROM t1 GROUP BY single_key</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.22</td>
        <td width="500">(o) SELECT MIN(single_key) FROM t1</td>
        <td>0</td>
        <td>0.0</td>
        <td>Select tables optimized away</td>
        <td></td>
        <td></td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(b) SELECT * FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.18</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.23</td>
        <td width="500">(b) SELECT part_key1, part_key2, col_not_index FROM t1 GROUP BY part_key1, part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1, part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.26</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &lt; 5 GROUP BY part_key1, part_key2</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key1, part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.30</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key1, part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.20</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 AND part_key2 &gt; 12 GROUP BY part_key1, part_key2</td>
        <td>91</td>
        <td>100.0</td>
        <td>Using where; Using index</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(o) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1, part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.45</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.29</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key1</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>single_key</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT part_key1, id FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where; Using index</td>
        <td>part_key1_key2</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(q) SELECT part_key1, part_key2 FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using where; Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.21</td>
        <td width="500">(q) SELECT part_key1, MIN(part_key2) FROM t1 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.34</td>
        <td width="500">(q) SELECT part_key1, part_key2 FROM t1 WHERE col_not_index &#x3D; 5 GROUP BY part_key1</td>
        <td>191503</td>
        <td>10.0</td>
        <td>Using where</td>
        <td>single_key</td>
        <td>index</td>
    </tr>
    <tr>
        <td>0.43</td>
        <td width="500">(o) SELECT part_key1 FROM t1 WHERE part_key2 &#x3D; 5 GROUP BY part_key1</td>
        <td>1936</td>
        <td>100.0</td>
        <td>Using where; Using index for group-by</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    <tr>
        <td>0.32</td>
        <td width="500">(b) SELECT * FROM t1 GROUP BY part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr>
        <td>0.35</td>
        <td width="500">(b) SELECT * FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td></td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.38</td>
        <td width="500">(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.47</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.37</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &gt; 5 GROUP BY part_key2</td>
        <td>95751</td>
        <td>100.0</td>
        <td>Using where; Using index; Using temporary</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.27</td>
        <td width="500">(b) SELECT part_key2 FROM t1 WHERE part_key1 &lt; 5 GROUP BY part_key2</td>
        <td>317</td>
        <td>100.0</td>
        <td>Using where; Using index; Using temporary</td>
        <td>part_key1_key2</td>
        <td>range</td>
    </tr>
    <tr>
        <td>0.48</td>
        <td width="500">(b) SELECT part_key1 FROM t1 WHERE part_key1 &#x3D; 5 GROUP BY part_key2</td>
        <td>97</td>
        <td>100.0</td>
        <td>Using index</td>
        <td>part_key1_key2</td>
        <td>ref</td>
    </tr>
    <tr>
        <td>0.31</td>
        <td width="500">(o) SELECT * FROM t1 GROUP BY part_key2</td>
        <td>191503</td>
        <td>100.0</td>
        <td>Using temporary</td>
        <td></td>
        <td>ALL</td>
    </tr>
    <tr style="height:2px">
        <td align="center" colspan="10">---------------</td>
    </tr>
    </tbody>
</table>
<br/>
</body>
</html>

