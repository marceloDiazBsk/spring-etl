package corar.etl.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommercialTransaction {
    private Long id;
    private String date;
    private String type;
    private String group;
    private BigDecimal amount;
    private String concept;
}
