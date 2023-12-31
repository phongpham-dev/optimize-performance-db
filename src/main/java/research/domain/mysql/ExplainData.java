package research.domain.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Column(name = "filtered")
    private Float filtered;

    public Map<String, Object> toMap() {
        return Map.of(
                "rows", Optional.ofNullable(rows).orElse(0L),
                "extra", addSpace1(Optional.ofNullable(extra).orElse("")),
                "selectType", Optional.ofNullable(selectType).orElse(""),
                "type", Optional.ofNullable(type).orElse(""),
                "possibleKeys", Optional.ofNullable(possibleKeys).orElse(""),
                "key", addSpace1(Optional.ofNullable(key).orElse("")),
                "ref", Optional.ofNullable(ref).orElse(""),
                "filtered", Optional.ofNullable(filtered).orElse(0f)
        );
    }

    public String addSpace(String text) {
        List<String> strings = new ArrayList<String>();
        int index = 0;
        while (index < text.length()) {
            strings.add(text.substring(index, Math.min(index + 4, text.length())));
            index += 4;
        }
        return StringUtils.collectionToDelimitedString(strings, "\n");
    }

    public String addSpace1(String text) {
        String regex = "(,|;)";
        var arr = Arrays.stream(text.split(regex)).toList();
        return StringUtils.collectionToDelimitedString(arr, ",\n");
    }
}
