package research.domain;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "product")
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "product_number")
    private String productNumber;

    @Column(name = "make_flag")
    private boolean makeFlag;

    @Column(name = "color")
    private String color;

    @Column(name = "list_price")
    private Double listPrice;

    @Column(name = "col_not_index")
    private Double colNotIndex;

    @Column(name = "size")
    private String size;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "product_line")
    private String productLine;

    @Column(name = "product_subcategory_id")
    private Integer productSubcategoryId;

    @Column(name = "discontinued_date")
    private Long discontinuedDate;

    @Column(name = "modified_date")
    private Long modifiedDate;
}
