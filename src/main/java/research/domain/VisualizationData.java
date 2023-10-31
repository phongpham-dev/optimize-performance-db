package research.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import research.domain.mysql.ExplainData;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisualizationData {

    private Boolean isBadQuery;

    private Double durationInMs;

    private String sqlOrigin;

    private String sqlText;

    private ExplainData explain;

    private Boolean isEnd;
}
