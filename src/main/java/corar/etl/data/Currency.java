package corar.etl.data;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableAnnotation(sourceTable = "moneda", targetTable = "currency_copy")
@ToString
public class Currency {

    @Id
    @FieldAnnotation(sourceName = "moneda_id")
    public Long currencyId;

    @FieldAnnotation(sourceName = "moneda_nombre")
    public String name;

    @FieldAnnotation(sourceName = "moneda_simbolo")
    public String symbol;

    @FieldAnnotation(sourceName = "moneda_status")
    public Boolean status;

    @FieldAnnotation(sourceName = "moneda_hidden")
    public Boolean hidden;

    @FieldAnnotation(sourceName = "moneda_timestamp")
    public String createdAt;


}
