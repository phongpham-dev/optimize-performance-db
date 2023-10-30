package research.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import research.domain.StatementHistoryEvent;
import research.domain.mysql.ExplainData;

import java.util.*;

@Component
@Getter
@Transactional
public class EventHolder {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    private List<StatementHistoryEvent> statementHistoryEventsHolder = new ArrayList<>();

    private Long lastStatementId = 0L;

    public StatementHistoryEvent getEvent() {
        refresh();
        return statementHistoryEventsHolder.get(0);
    }

    public void refresh() {
        var sql = "SELECT EVENT_ID, TRUNCATE(TIMER_WAIT/1000000000,2) as Duration, SQL_TEXT, STATEMENT_ID\n" +
                "FROM performance_schema.events_statements_history_long\n" +
                "WHERE CURRENT_SCHEMA='op-research' and EVENT_NAME='statement/sql/select' \n" +
                "AND SQL_TEXT like '%`op-research`.product%' \n" +
                "AND SQL_TEXT NOT LIKE '%performance_schema.events_statements_history_long%'\n" +
                "AND SQL_TEXT NOT LIKE 'EXPLAIN%'\n" +
                "AND STATEMENT_ID >" + lastStatementId + " " +
                "ORDER BY STATEMENT_ID DESC\n" +
                "LIMIT 20" +
                ";";

        var query = entityManager.createNativeQuery(sql, StatementHistoryEvent.class);

        List<StatementHistoryEvent> statementHistoryEventsForAdd = query.getResultList().stream().map(v -> {
            try {
                StatementHistoryEvent statementHistoryEvent = (StatementHistoryEvent) v;

                var sqlText = statementHistoryEvent.getSqlText();

                var sqlExplainQuery = "";
                if (!sqlText.startsWith("EXPLAIN"))
                    sqlExplainQuery = "EXPLAIN " + sqlText;
                else sqlExplainQuery = sqlText;

                var singleRs = entityManager.createNativeQuery(sqlExplainQuery, ExplainData.class).getSingleResult();

                var explainData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(singleRs);

                statementHistoryEvent.setExplain((ExplainData) singleRs);

                return statementHistoryEvent;

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        if (!statementHistoryEventsForAdd.isEmpty()) {
            lastStatementId = statementHistoryEventsForAdd.get(0).getStatementId();
            statementHistoryEventsHolder.addAll(statementHistoryEventsForAdd);
        }

    }

    public Map<String, Object> getMonitorHtml() {
        var listItem = statementHistoryEventsHolder.stream()
                .sorted(Comparator.comparing(StatementHistoryEvent::getEventId).reversed())
                .map(v -> {
                    return Map.of(
                            "eventId", v.getEventId(),
                            "durationInMs", String.format("%.2f", v.getDurationInMs()),
                            "sqlText", v.getSqlText(),
                            "explain", v.getExplain().toMap()
                    );
                })
                .limit(20)
                .toList();
        return Map.of(
                "refreshInSecond", 2,
                "listItem", listItem
        );
    }

}
