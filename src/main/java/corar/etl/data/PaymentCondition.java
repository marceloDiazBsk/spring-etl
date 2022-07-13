package corar.etl.data;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableAnnotation(sourceTable = "pago_forma", targetTable = "payment_condition_copy")
@ToString
public class PaymentCondition {

    @Id
    @FieldAnnotation(sourceName = "forma_id")
    public Long paymentConditionId;

    @FieldAnnotation(sourceName = "forma_nombre")
    public String name;

    @FieldAnnotation(sourceName = "forma_status")
    public Boolean status;

    @FieldAnnotation(sourceName = "forma_hidden")
    public Boolean hidden;

    @FieldAnnotation(sourceName = "forma_timestamp")
    public String createdAt;


}
