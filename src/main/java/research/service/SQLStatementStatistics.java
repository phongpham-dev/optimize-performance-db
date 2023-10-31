package research.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import research.domain.VisualizationData;
import research.domain.mysql.ExplainData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
@Getter
public class SQLStatementStatistics {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    private List<VisualizationData> visualizationDatas = new ArrayList<>();

    public ExplainData execute(String explainSql) throws InterruptedException, JsonProcessingException {
        var explainData = entityManager
                .createNativeQuery(explainSql, ExplainData.class)
                .getSingleResult();
//        System.out.println("explain " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(explainData));
//        Thread.sleep(500);
        return (ExplainData) explainData;
    }

    public List<Pair<String, String>> conditionPushDownSQl() {
        var str = """
                (b) SELECT key, not_index FROM t1 WHERE not_index = 'blue';
                (b) SELECT key, not_index FROM t1 WHERE key = 10;
                (b) SELECT key, not_index FROM t1 WHERE key < 2
                """.trim();
        String[] whereOptimization = str.split(";");

        var sqlList = Arrays
                .stream(whereOptimization)
                .filter(v -> !v.isEmpty())
                .map(originalSql -> {
                    var sql = originalSql
                            .replace("\n", "")
                            .replaceAll("t1", "product")
                            .replaceAll("key", "list_price")
                            .replaceAll("not_index", "color");
                    return Pair.of(originalSql, sql);
                }).toList();
        return sqlList;
    }

    public List<Pair<String, String>> indexMerge() {
        var str = """
                (b) SELECT * FROM t1 WHERE key1 = 10 OR key2 = 20;
                (b) SELECT * FROM t1 WHERE (key1 = 10 OR key2 = 20) AND non_key = 30;
                (b) SELECT * FROM t1, t2 WHERE (t1.key1 IN (1, 2) OR t1.key2 LIKE '2%') AND t2.primary_key = t1.some_col;
                (o) SELECT * FROM t1, t2 WHERE t1.key1 = 1 AND t2.primary_key = t1.some_col;
                                
                (b) SELECT * FROM t1 WHERE primary_key < 10 AND key1 = 4;
                (b) SELECT * FROM t1 WHERE key_part1 = 500 AND key_part2 = 10;
                (b) SELECT * FROM t1 WHERE key_part1 = 500 AND key_part2 = 10 AND key1 = 4;
                (b) SELECT * FROM t1 WHERE key1 = 1 AND key2 = 100;
                (o) SELECT COUNT(*) FROM t1 WHERE key1 = 1 AND key2 = 1;
                                
                (b) SELECT * FROM t1 WHERE primary_key < 10 OR key1 = 4;   
                (b) SELECT * FROM t1 WHERE key_part1 = 500 OR key_part2 = 10;             
                (b) SELECT * FROM t1 WHERE key1 = 1 OR key2 = 2 OR key3 = 'name 3';              
                (o) SELECT * FROM t1 WHERE (key1 = 1 AND key2 = 2) OR (key3 = 'name 3' AND key4 = 'PN-3');                 
                                
                (b) SELECT * FROM t1 WHERE key1 < 10 OR key2 < 20;               
                (o) SELECT * FROM t1 WHERE (key1 > 10 OR key2 = 20) AND non_key = 30;
                  """.trim();
        String[] whereOptimization = str.split(";");

        var sqlList = Arrays
                .stream(whereOptimization)
                .filter(v -> !v.isEmpty())
                .map(originalSql -> {
                    var sql = originalSql
                            .replace("\n", "")
                            .replaceAll("key_part1", "list_price")
                            .replaceAll("key_part2", "weight")
                            .replaceAll("key1", "product_subcategory_id")
                            .replaceAll("key2", "list_price")
                            .replaceAll("key3", "name")
                            .replaceAll("key4", "product_number")
                            .replaceAll("t1", "product")
                            .replaceAll("t2", "category")
                            .replaceAll("non_key", "color")
                            .replaceAll("key_part2", "weight")
                            .replaceAll("col_not_index", "weight")
                            .replaceAll("primary_key", "id")
                            .replaceAll("some_col", "product_subcategory_id");
                    return Pair.of(originalSql, sql);
                }).toList();
        return sqlList;
    }


    public List<Pair<String, String>> whereSql() {
        String[] whereOptimization = {
                //constant table
                "(m) SELECT * FROM t1, t2 WHERE t1.primary_key=1 AND t2.primary_key=t1.id",
                "(m) SELECT * FROM t1 WHERE primary_key=1",
                "(m) SELECT * FROM t1 WHERE primary_key IN (1,2,3,4)",
                "(m) SELECT * FROM t1 WHERE primary_key < 20",
                "(m) SELECT * FROM t1 WHERE unique_key= 'name 3'",
                "(m) SELECT * FROM t1 WHERE list_price = 50",
                "(m) SELECT * FROM t1 WHERE list_price < 50",
                "(m) SELECT * FROM t1 WHERE product_subcategory_id = 2",
                "(o) SELECT * FROM t1 WHERE primary_key=1 AND name = 'name 1'",

                "(b) SELECT * FROM t1",
                "(b) SELECT primary_key FROM t1",
                "(b) SELECT * FROM t1 WHERE key1 = 1 OR col_not_index = 'constant1'",
                "(b) SELECT COUNT(*) FROM t1 LIMIT 100",
                "(m) SELECT MIN(p1), MAX(p1) FROM t1",
                "(m) SELECT MAX(p2) FROM t1 WHERE p1='constant'",
                "(o) SELECT MIN(p1), MAX(p2) FROM t1 WHERE p1='val'",

                "(b) SELECT * FROM t1 ORDER BY p1, p2",
                "(b) SELECT * FROM t1 WHERE p1 = 'constant' ORDER BY p1, p2",
                "(b) SELECT * FROM t1 ORDER BY p1 DESC, p2 DESC",
                "(b) SELECT * FROM t1 ORDER BY p1 DESC, p2 ASC LIMIT 10",
                "(b) SELECT * FROM t1 ORDER BY p1 ASC, p2 DESC LIMIT 10",
                "(m) SELECT * FROM t1 ORDER BY p1, p2 LIMIT 10",
                "(m) SELECT * FROM t1 ORDER BY p1 DESC, p2 DESC LIMIT 10",
                "(o) SELECT * FROM t1 ORDER BY p1 ASC, p2 ASC LIMIT 10",

                "(b) SELECT * FROM t1 WHERE p2 = 'val2'",
                "(b) SELECT * FROM t1 WHERE p1='val1' OR p2 = 'val2'",
                "(m) SELECT * FROM t1 WHERE p1='val1'",
                "(m) SELECT * FROM t1 WHERE p1='val1' AND p2 = 'val2'",
                "(m) SELECT * FROM t1 WHERE p1='val1' AND p3 = 43.73",
                "(m) SELECT * FROM t1 WHERE p1='val1' AND p2 = 'val2' AND color = 'red'",
                "(m) SELECT * FROM t1 WHERE p1='val1' AND (p2 = 'val2' OR p2 = 'val3')",
                "(o) SELECT * FROM t1 WHERE p1='val1' AND (p2 <= 'val2' AND p2 >= 'val3')",


                "(b) SELECT p1, p2 FROM t1 WHERE p1='val'",
                "(b) SELECT COUNT(*) FROM t1 WHERE p1='val1' AND p2='val2'",
                "(o) SELECT MAX(p2) FROM t1 GROUP BY p1",

                "(o) SELECT * FROM `op-research`.product WHERE product_line LIKE 'M%'",
        };

        var sqlList = Arrays.stream(whereOptimization)
                .map(v -> {
                    var sql = v.replaceAll("col_not_index", "color")
                            .replaceAll("primary_key", "id")
                            .replaceAll("unique_key", "name")
                            .replaceAll("key1", "product_subcategory_id")
                            .replaceAll("key2", "list_price")
                            .replaceAll("p1", "product_line")
                            .replaceAll("p2", "size")
                            .replaceAll("p3", "weight")
                            .replaceAll("constant", "M")
                            .replaceAll("constant1", "red")
                            .replaceAll("val1", "M")
                            .replaceAll("val3", "XL")
                            .replaceAll("val2", "L")
                            .replaceAll("val", "M")
                            .replaceAll("t1", "product")
                            .replaceAll("t2", "category");

                    return Pair.of(v, sql);
                }).toList();
        return sqlList;
    }

    public List<Pair<String, String>> rangeSQL() {
        var str = """
                (b) SELECT * FROM tbl_name WHERE col_not_index > 1 AND col_not_index < 10;
                (b) SELECT * FROM tbl_name WHERE key_col < 500;
                (b) SELECT * FROM tbl_name WHERE key_col > 500;
                (m) SELECT * FROM tbl_name WHERE key_col >= 1 AND key_col <= 10;                                              
                (m) SELECT * FROM tbl_name WHERE key_col IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
                (o) SELECT * FROM tbl_name WHERE key_col BETWEEN 1 AND 10;
                                
                (b) SELECT * FROM tbl_name WHERE name LIKE 'name 1%';
                (b) SELECT * FROM tbl_name WHERE name LIKE '%1';
                (b) SELECT id, name FROM tbl_name WHERE name LIKE '%1';
                (o) SELECT id, name FROM tbl_name WHERE name LIKE 'name 1%';
                                
                (b) SELECT * FROM tbl_name WHERE key_part1 > 20 AND key_part2 < 50;
                (b) SELECT * FROM tbl_name WHERE key_part1 > 20 AND key_part1 < 50 OR key_part2 > 20 AND key_part2 < 50;
                (b) SELECT * FROM tbl_name WHERE key_part1 > 20 AND key_part2 IS NOT NULL;
                (b) SELECT * FROM tbl_name WHERE key_part1 >= 20 AND key_part2 < 50;
                (m) SELECT * FROM tbl_name WHERE key_part1 = 20 AND key_part2 < 50;
                (m) SELECT * FROM tbl_name WHERE (key_part1, key_part2) IN((1274, 43.73));
                (o) SELECT * FROM tbl_name WHERE (key_part1 = 1274 AND key_part2 = 43.73);
                """.trim();

        String[] whereOptimization = str.split(";");

        var sqlList = Arrays
                .stream(whereOptimization)
                .filter(v -> !v.isEmpty())
                .map(originalSql -> {
                    var sql = originalSql
                            .replace("\n", "")
                            .replaceAll("primary_key", "id")
                            .replaceAll("tbl_name", "product")
                            .replaceAll("key_col_str", "name")
                            .replaceAll("key_col", "list_price")
                            .replaceAll("key_part1", "list_price")
                            .replaceAll("key_part2", "weight")
                            .replaceAll("col_not_index", "weight");
                    return Pair.of(originalSql, sql);
                }).toList();
        return sqlList;
    }

    public List<Pair<String, String>> groupBySql() {
        var str = """
                (b) SELECT COUNT(*) FROM t1 GROUP BY c1, c2;
                (b) SELECT c1, c2 FROM t1 GROUP BY c2, c3;
                (b) SELECT c1, col_not_index FROM t1 GROUP BY c1, c2;
                (m) SELECT c1, c3 FROM t1 GROUP BY c1, c2;
                (m) SELECT c1, c2 FROM t1 GROUP BY c1;
                (o) SELECT c1, c2 FROM t1 GROUP BY c1, c2;
                                
                (b) SELECT COUNT(DISTINCT c1), SUM(DISTINCT c1) FROM t1;
                (o) SELECT COUNT(DISTINCT c1, c2), COUNT(DISTINCT c2, c1) FROM t1;
                                
                (b) SELECT c1, c2, c3 FROM t1 WHERE c2 = 'M' GROUP BY c1, c3;
                (o) SELECT c1, c2, c3 FROM t1 WHERE c1 = 'M' GROUP BY c2, c3;
                                
                (b) SELECT DISTINCT c2, c3 FROM t1;
                (b) SELECT DISTINCT col_not_index FROM t1;
                (o) SELECT DISTINCT c1, c2 FROM t1;
                                
                (b) SELECT DISTINCT c1, c2, c3 FROM t1 WHERE c1 > 'constC1';
                (o) SELECT c1, c2, c3 FROM t1 WHERE c1 > 'constC1' GROUP BY c1, c2, c3;
                                
                (o) SELECT c1, MIN(c2) FROM t1 GROUP BY c1;
                                
                (o) SELECT c1, c2 FROM t1 WHERE c1 < 'constC1' GROUP BY c1, c2;
                                
                (o) SELECT MAX(c3), MIN(c3), c1, c2 FROM t1 WHERE c2 > 'constC2' GROUP BY c1, c2;
                                
                (o) SELECT c2 FROM t1 WHERE c1 < 'constC1' GROUP BY c1, c2;
                                
                (o) SELECT c1, c2 FROM t1 WHERE c3 = constC3 GROUP BY c1, c2;
                  """.trim();

        String[] whereOptimization = str.split(";");

        var sqlList = Arrays.stream(whereOptimization)
                .filter(v -> !v.isEmpty())
                .map(originSql -> {
                    var replacedSql = originSql
                            .replace("\n", "")
                            .replaceAll("col_not_index", "color")
                            .replaceAll("t1", "product")
                            .replaceAll("c1", "product_line")
                            .replaceAll("c2", "size")
                            .replaceAll("c3", "weight")
                            .replaceAll("constC1", "M")
                            .replaceAll("constC2", "M")
                            .replaceAll("constC3", "50");

                    return Pair.of(originSql, replacedSql);
                })
                .toList();
        return sqlList;
    }

    public List<Pair<String, String>> orderBySql() {
        var str = """
                (b) SELECT * FROM t1 WHERE p2 = 'constantP2' ORDER BY p1, p2;
                (m) SELECT id, p1, p2 FROM t1 ORDER BY p1, p2;
                (m) SELECT * FROM t1 WHERE p2 = 'constantP2' ORDER BY p1, p2;
                (m) SELECT id, p1, p2 FROM t1 WHERE p2 = 'constantP2' ORDER BY p1, p2;
                (o) SELECT * FROM t1 WHERE p1 = 'constantP1' ORDER BY p2;
                                
                (b) SELECT * FROM t1 WHERE p1 = 'constantP1' ORDER BY p1 DESC, p2 ASC;
                (b) SELECT * FROM t1 WHERE p1 > 'constantP1' ORDER BY p1 ASC;
                (b) SELECT * FROM t1 WHERE p1 < 'constantP1' ORDER BY p1 DESC;
                (m) SELECT * FROM t1 WHERE p1 = 'constantP1' ORDER BY p2 DESC;
                (o) SELECT * FROM t1 WHERE p1 = 'constantP1' AND p2 > 'constantP2' ORDER BY p2;
                                
                (b) SELECT * FROM t1 ORDER BY -key LIMIT 10;
                (b) SELECT * FROM t1 ORDER BY ABS(key) LIMIT 10;
                (o) SELECT * FROM t1 ORDER BY key LIMIT 10;
                                
                (b) SELECT * FROM t1 ORDER BY key1, key2 LIMIT 100;
                (m) SELECT * FROM t1 WHERE key2=constKey2 ORDER BY key1_p1, key1_p3;
                (o) SELECT * FROM t1 WHERE key2=constKey2 ORDER BY key1;
                                
                (b) SELECT * FROM t1 WHERE col_not_index > 'blue' ORDER BY col_not_index;
                (b) SELECT price_index FROM t1 ORDER BY price_index;
                (b) SELECT ABS(price_index) AS a FROM t1 ORDER BY price_index;
                (o) SELECT ABS(price_index) AS b FROM t1 ORDER BY price_index;
                                
                (b) SELECT * FROM t1 WHERE col_not_index > 'blue' ORDER BY col_not_index DESC LIMIT 0, 5;
                (b) SELECT name FROM t1 ORDER BY RAND() LIMIT 15;      
                (o) SELECT name FROM t1 ORDER BY name LIMIT 10;
                  """.trim();

        String[] whereOptimization = str.split(";");

        var sqlList = Arrays.stream(whereOptimization)
                .filter(v -> !v.isEmpty())
                .map(originSql -> {
                    var replacedSql = originSql
                            .replace("\n", "")
                            .replaceAll("col_not_index", "color")
                            .replaceAll("t1", "product")
                            .replaceAll("key1_p1", "product_line")
                            .replaceAll("p1", "product_line")
                            .replaceAll("p2", "size")
                            .replaceAll("key1_p3", "weight")
                            .replaceAll("p3", "weight")
                            .replaceAll("constantP1", "R")
                            .replaceAll("constantP2", "M")
                            .replaceAll("key1", "id")
                            .replaceAll("key2", "product_subcategory_id")
                            .replaceAll("key", "id")
                            .replaceAll("constKey2", "1")
                            .replaceAll("price_index", "list_price");

                    return Pair.of(originSql, replacedSql);
                })
                .toList();
        return sqlList;
    }


    public List<Pair<String, String>> commonSql() {
        String[] whereOptimization = {
                //constant table
                "(q) SELECT * FROM t1 WHERE primary_key = 1",
                "(q) SELECT * FROM t1 WHERE unique_key = 'name 1'",
                "(q) SELECT * FROM t1 WHERE unique_key = 'name 1'",
                "(o) SELECT * FROM t1 WHERE primary_key = 1 AND unique_key = 'name 1'",

                //index merge
                "(q) SELECT * FROM t1 WHERE single_key = 50 OR single_key1 = 2",
                "(q) SELECT * FROM t1 WHERE single_key < 50 OR single_key1 < 2",
                "(q) SELECT * FROM t1 WHERE single_key = 50 AND single_key1 = 2",
                "(q) SELECT * FROM t1 WHERE (single_key = 50 AND col_not_index = 50) OR single_key1 = 2",
                "(o) SELECT * FROM t1 WHERE primary_key < 50 AND single_key = 5",

                //ref
                "(b) SELECT * FROM t1 WHERE col_not_index = 500",
                "(q) SELECT * FROM t1 WHERE single_key = 3",
                "(q) SELECT single_key FROM t1 WHERE single_key = 3",
                "(q) SELECT * FROM t1 WHERE single_key = 3 OR col_not_index = 50",
                "(q) SELECT * FROM t1 WHERE single_key = 3 AND col_not_index = 50",
                "(q) SELECT * FROM t1 WHERE single_key > 5",
                "(q) SELECT single_key FROM t1 WHERE single_key > 5",
                "(q) SELECT * FROM t1 WHERE single_key < 5",
                "(o) SELECT single_key FROM t1 WHERE single_key < 5",

                "(b) SELECT * FROM t1 WHERE part_key2 = 12",
                "(b) SELECT * FROM t1 WHERE part_key1 = 1000 OR part_key2 = 12",
                "(b) SELECT * FROM t1 WHERE part_key1 = 500 OR col_not_index = 500",
                "(q) SELECT * FROM t1 WHERE part_key1 = 500",
                "(q) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 = 12",
                "(q) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 > 12",
                "(o) SELECT * FROM t1 WHERE part_key1 = 1000 AND part_key2 < 12",

                //range
                "(b) SELECT * FROM t1 WHERE col_not_index >= 1 AND col_not_index < 5",
                "(q) SELECT * FROM t1 WHERE single_key >= 1 AND single_key < 5",
                "(q) SELECT * FROM t1 WHERE single_key >= 1 AND single_key < 5 AND col_not_index = 50",
                "(q) SELECT * FROM t1 WHERE single_key IN(1, 2, 3, 4)",
                "(q) SELECT * FROM t1 WHERE single_key = 1 OR single_key = 2",
                "(q) SELECT * FROM t1 WHERE single_key < 5",
                "(o) SELECT * FROM t1 WHERE single_key BETWEEN 1 AND 4",

                //like
                "(b) SELECT * FROM t1 WHERE single_key_as_string LIKE '%1'",
                "(o) SELECT * FROM t1 WHERE single_key_as_string LIKE 'name1%'",

                //order by
                "(b) SELECT * FROM t1 ORDER BY single_key",
                "(q) SELECT single_key FROM t1 ORDER BY single_key",
                "(q) SELECT * FROM t1 ORDER BY single_key DESC LIMIT 100",
                "(b) SELECT * FROM t1 WHERE single_key > 5 ORDER BY single_key DESC",
                "(q) SELECT * FROM t1 WHERE single_key = 3 ORDER BY single_key DESC",
                "(b) SELECT single_key FROM t1 WHERE single_key = 3 ORDER BY single_key DESC",
                "(b) SELECT * FROM t1 WHERE single_key < 5 ORDER BY single_key",
                "(o) SELECT single_key FROM t1 WHERE single_key < 5 ORDER BY single_key",

                "(q) SELECT * FROM t1 ORDER BY part_key1, part_key2",
                "(q) SELECT * FROM t1 WHERE part_key1 = 2 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100",
                "(q) SELECT * FROM t1 ORDER BY part_key1 DESC, part_key2 DESC LIMIT 100",
                "(q) SELECT * FROM t1 ORDER BY part_key1 ASC, part_key2 ASC LIMIT 100",
                "(q) SELECT * FROM t1 WHERE part_key1 = 2 ORDER BY part_key2",
                "(q) SELECT * FROM t1 WHERE part_key1 = 2 AND part_key2 > 12 ORDER BY part_key2",
                "(o) SELECT * FROM t1 WHERE part_key1 = 2 AND part_key2 < 12 ORDER BY part_key2",

                //group by
                "(b) SELECT col_not_index FROM t1 WHERE col_not_index > 5 GROUP BY col_not_index",
                "(b) SELECT * FROM t1 GROUP BY single_key",
                "(b) SELECT single_key FROM t1 GROUP BY single_key",

                "(b) SELECT * FROM t1 WHERE single_key < 5 GROUP BY single_key",
                "(b) SELECT single_key FROM t1 WHERE single_key < 5 GROUP BY single_key",

                "(b) SELECT * FROM t1 WHERE single_key = 5 GROUP BY single_key",
                "(b) SELECT single_key FROM t1 WHERE single_key = 5 GROUP BY single_key",

                "(b) SELECT * FROM t1 WHERE single_key > 5 GROUP BY single_key",
                "(b) SELECT single_key FROM t1 WHERE single_key > 5 GROUP BY single_key",
                "(b) SELECT id, single_key FROM t1 WHERE single_key > 5 GROUP BY single_key",
                "(o) SELECT id, single_key, col_not_index FROM t1 WHERE single_key > 5 GROUP BY single_key",

                "(b) SELECT DISTINCT single_key FROM t1",
                "(b) SELECT COUNT(DISTINCT single_key) FROM t1",
                "(b) SELECT SUM(single_key) FROM t1",
                "(b) SELECT SUM(single_key) FROM t1 GROUP BY single_key",
                "(o) SELECT MIN(single_key) FROM t1",

                "(b) SELECT * FROM t1 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1, part_key2 FROM t1 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1, part_key2, col_not_index FROM t1 GROUP BY part_key1, part_key2",
                "(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 < 5 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 > 5 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1 FROM t1 WHERE part_key1 > 5 GROUP BY part_key1, part_key2",
                "(b) SELECT part_key1 FROM t1 WHERE part_key1 = 5 AND part_key2 > 12 GROUP BY part_key1, part_key2",
                "(o) SELECT part_key1 FROM t1 WHERE part_key1 = 5 GROUP BY part_key1, part_key2",

                "(b) SELECT * FROM t1 WHERE part_key2 = 5 GROUP BY part_key1",
                "(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key1",
                "(q) SELECT part_key1, id FROM t1 WHERE part_key2 = 5 GROUP BY part_key1",
                "(q) SELECT part_key1, part_key2 FROM t1 WHERE part_key2 = 5 GROUP BY part_key1",
                "(q) SELECT part_key1, MIN(part_key2) FROM t1 GROUP BY part_key1",
                "(q) SELECT part_key1, part_key2 FROM t1 WHERE col_not_index = 5 GROUP BY part_key1",
                "(o) SELECT part_key1 FROM t1 WHERE part_key2 = 5 GROUP BY part_key1",

                "(b) SELECT * FROM t1 GROUP BY part_key2",
                "(b) SELECT * FROM t1 WHERE part_key1 = 5 GROUP BY part_key2",
                "(b) SELECT part_key1, part_key2 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2",
                "(b) SELECT part_key2 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2",
                "(b) SELECT part_key2 FROM t1 WHERE part_key1 > 5 GROUP BY part_key2",
                "(b) SELECT part_key2 FROM t1 WHERE part_key1 < 5 GROUP BY part_key2",
                "(b) SELECT part_key1 FROM t1 WHERE part_key1 = 5 GROUP BY part_key2",
                "(o) SELECT * FROM t1 GROUP BY part_key2",
                //index merge

        };

        var sqlList = Arrays.stream(whereOptimization)
                .map(v -> {
                    var sql = v
                            .replaceAll("col_not_index", "col_not_index")
                            .replaceAll("primary_key", "id")
                            .replaceAll("unique_key", "name")
                            .replaceAll("part_key1", "list_price")
                            .replaceAll("part_key2", "weight")
                            .replaceAll("single_key_as_string", "name")
                            .replaceAll("single_key1", "product_subcategory_id")
                            .replaceAll("single_key", "list_price")
                            .replaceAll("t1", "product")
                            .replaceAll("t2", "category");

                    return Pair.of(v, sql);
                }).toList();
        return sqlList;
    }


    public List<Pair<String, String>> allSQl() {
        List<Pair<String, String>> list = new ArrayList<>();
        list.addAll(whereSql());
        list.addAll(rangeSQL());
        list.addAll(indexMerge());
        list.addAll(orderBySql());
        list.addAll(groupBySql());
        list.addAll(conditionPushDownSQl());
        return list;
    }

    public void statisticsWhere() {
        var sqlList = commonSql();
        sqlList.forEach(pair -> {
            var originSql = pair.getFirst();
            var sql = pair.getSecond();
            try {
                System.out.println("originSql " + originSql);
                System.out.println("sql " + sql);
                var explainSql = "EXPLAIN " + sql.substring(4);
                System.out.println("explain sql " + explainSql);

                var explainData = execute(explainSql);

                var jsonEx = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(explainData);

                var profilingList = entityManager.createNativeQuery("SHOW PROFILES").getResultList();
                var duration = 0d;

                if (profilingList.size() - 1 >= 0) {
                    var profiling = profilingList.get(profilingList.size() - 1);
                    var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(profiling);
                    var jsonNode = objectMapper.readTree(json);
                    duration = jsonNode.get(1).asDouble();
                }

                var isBadQuery = !sql.startsWith("(o)");
                VisualizationData visualizationData = new VisualizationData(isBadQuery, duration * 1000d, originSql, sql, explainData);

                System.out.println("explaon data " + explainData);
                System.out.println(visualizationData);

                visualizationDatas.add(visualizationData);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void statisticsWhere1() {
        var sqlList = commonSql();
        sqlList.forEach(pair -> {
            var originSql = pair.getFirst();
            var sql = pair.getSecond();
            try {
                System.out.println("originSql " + originSql);
                System.out.println("sql " + sql);
                var explainSql = "EXPLAIN " + sql.substring(4);
                System.out.println("explain sql " + explainSql);

                var explainData = execute(explainSql);

                var jsonEx = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(explainData);

                var profilingList = entityManager.createNativeQuery("SHOW PROFILES").getResultList();
                var duration = 0d;

                if (profilingList.size() - 1 >= 0) {
                    var profiling = profilingList.get(profilingList.size() - 1);
                    var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(profiling);
                    var jsonNode = objectMapper.readTree(json);
                    duration = jsonNode.get(1).asDouble();
                }

                var isBadQuery = !sql.startsWith("(o)");
                VisualizationData visualizationData = new VisualizationData(isBadQuery, duration * 1000d, originSql, sql, explainData);

                System.out.println("explaon data " + explainData);
                System.out.println(visualizationData);

                visualizationDatas.add(visualizationData);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public Map<String, Object> getMonitorHtml() {
        var listItem = visualizationDatas.stream()
                .map(v -> {
                    return Map.of(
                            "isBadQuery", v.getIsBadQuery(),
                            "durationInMs", String.format("%.2f", v.getDurationInMs()),
                            "sqlOrigin", v.getSqlOrigin(),
                            "sqlText", v.getSqlText(),
                            "explain", v.getExplain().toMap()
                    );
                })
                .limit(500)
                .toList();
        return Map.of(
                "refreshInSecond", 2,
                "listItem", listItem
        );
    }

    @Autowired
    MustacheResourceTemplateLoader mustacheResourceTemplateLoader;

    public void export01() throws Exception {
        statisticsWhere();
        var reader = mustacheResourceTemplateLoader.getTemplate("visualize");
        var m = Mustache.compiler().compile(reader);
        var object = getMonitorHtml();
        var rs = m.execute(object);
        System.out.println("template " + rs);

        File file = new File("/Users/phong/working/research/optimize-performance-db/README.md");
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(rs);
        bufferedWriter.close();
    }

    public String addSpace(String text) {
        List<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index, Math.min(index + 4,text.length())));
            index += 4;
        }
        return StringUtils.collectionToDelimitedString(strings, "<br>");
    }

    public void export() {
        statisticsWhere();
        try {
            File file = new File("/Users/phong/working/research/optimize-performance-db/README.md");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            var dataForExport =visualizationDatas.stream().map(v -> {

                var list = List.of(
                        Optional.ofNullable(String.format("%.2f", v.getDurationInMs())).orElse(" "),
                        Optional.ofNullable(v.getSqlOrigin()).orElse(" "),
                        Optional.ofNullable(v.getExplain().getRows()).orElse(0L),
                        Optional.ofNullable(v.getExplain().getFiltered()).orElse(0f),
                        Optional.ofNullable(v.getExplain().getExtra()).orElse(" "),
                        Optional.ofNullable(v.getExplain().getKey()).orElse(" "),
                        Optional.ofNullable(v.getExplain().getType()).orElse(" ")
                );

                var l = list.stream().map(o -> o.toString()).toList();

                return StringUtils.collectionToDelimitedString(l, "|");
                //System.out.println( l.toString().replaceAll(",", "|"));
            }).toList();


           dataForExport.forEach(v -> {
               try {
                   bufferedWriter.write(v);
                   bufferedWriter.newLine();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }

           });

            bufferedWriter.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
