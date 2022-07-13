package corar.etl.data;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableAnnotation(sourceTable = "factura_detalle", targetTable = "bill_detail_copy")
@ToString
public class BillDetail {

    @Id
    @FieldAnnotation(sourceName = "detalle_id")
    public Long detailId;

    @FieldAnnotation(sourceName = "factura_id")
    public Long billId;

    @FieldAnnotation(sourceName = "detalle_cantidad")
    public Integer quantity;

    @FieldAnnotation(sourceName = "detalle_descripcion")
    public String description;

    @FieldAnnotation(sourceName = "detalle_preciounitario")
    public BigDecimal unitPrice;

    @FieldAnnotation(sourceName = "detalle_iva")
    public String vat;

    @FieldAnnotation(sourceName = "detalle_totaliva")
    public BigDecimal totalVat;

    @FieldAnnotation(sourceName = "detalle_total")
    public BigDecimal totalAmount;

    @FieldAnnotation(sourceName = "detalle_status")
    public Boolean status;

    @FieldAnnotation(sourceName = "detalle_hidden")
    public Boolean hidden;

    @FieldAnnotation(sourceName = "detalle_iva5")
    public BigDecimal vat5;

    @FieldAnnotation(sourceName = "detalle_iva10")
    public BigDecimal vat10;

    @FieldAnnotation(sourceName = "detalle_ivaexenta")
    public BigDecimal exempt;

    @FieldAnnotation(sourceName = "detalle_timestamp")
    public String createdAt;


}
