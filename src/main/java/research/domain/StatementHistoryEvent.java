package research.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import research.domain.mysql.ExplainData;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class StatementHistoryEvent {
    @Id
    private Long eventId;
    @Column(name = "duration")
    private Double durationInMs;
    @Column(name = "sql_text")
    private String sqlText;
    @Transient
    private ExplainData explain;
    @Column(name = "statement_id")
    private Long statementId;
}
