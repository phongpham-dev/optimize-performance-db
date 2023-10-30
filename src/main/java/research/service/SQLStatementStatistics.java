package research.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import research.domain.StatementHistoryEvent;
import research.domain.VisualizationData;
import research.domain.mysql.ExplainData;

import java.util.*;

@Service
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

    public List<Pair<String, String>> whereSql() {

        String[] whereOptimization = {
                "(b) SELECT * FROM t1 LIMIT 1",
                "(b) SELECT primary_key FROM t1 LIMIT 1",
                "(m) SELECT * FROM t1, t2 WHERE t1.primary_key=1 AND t2.primary_key=t1.id",
                "(m) SELECT * FROM t1 WHERE key = 1 OR col_not_index = 'red' AND list_price = 50",
                "(o) SELECT * FROM t1 WHERE primary_key=1",

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
                            .replaceAll("key", "product_subcategory_id")
                            .replaceAll("p1", "product_line")
                            .replaceAll("p2", "size")
                            .replaceAll("p3", "weight")
                            .replaceAll("constant", "M")
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

    public void statisticsWhere() {
        var sqlList = whereSql();
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
                    System.out.println("statis " + v);
                    return Map.of(
                            "isBadQuery", v.getIsBadQuery(),
                            "durationInMs", String.format("%.2f", v.getDurationInMs()),
                            "sqlOrigin", v.getSqlOrigin(),
                            "sqlText", v.getSqlText(),
                            "explain", v.getExplain().toMap()
                    );
                })
                .limit(30)
                .toList();
        return Map.of(
                "refreshInSecond", 2,
                "listItem", listItem
        );
    }
}
