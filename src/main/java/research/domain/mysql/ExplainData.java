package research.domain.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ExplainData {
    @Id
    private Long id;

    @Column(name = "rows")
    private Long rows;

    @Column(name = "Extra")
    private String extra;

    @Column(name = "select_type")
    private String selectType;

    @Column(name = "type")
    private String type;

    @Column(name = "possible_keys")
    private String possibleKeys;

    @Column(name = "key")
    private String key;

    @Column(name = "ref")
    private String ref;

    public Map<String, Object> toMap() {
        return Map.of(
                "rows", Optional.ofNullable(rows).orElse(0L),
                "extra", Optional.ofNullable(extra).orElse(""),
                "selectType", Optional.ofNullable(selectType).orElse(""),
                "type", Optional.ofNullable(type).orElse(""),
                "possibleKeys", Optional.ofNullable(possibleKeys).orElse(""),
                "key", Optional.ofNullable(key).orElse(""),
                "ref", Optional.ofNullable(ref).orElse("")
        );
    }
}
