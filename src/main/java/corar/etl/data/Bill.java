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
@TableAnnotation(sourceTable = "factura", targetTable = "bill_copy")
@ToString
public class Bill {
    public Long id;

    @Id
    @FieldAnnotation(sourceName = "factura_id")
    public Long billId;

    @FieldAnnotation(sourceName = "proveedor_id")
    public Long providerId;

    @FieldAnnotation(sourceName = "forma_id")
    public Long modeId;

    @FieldAnnotation(sourceName = "moneda_id")
    public Long currencyId;

    @FieldAnnotation(sourceName = "factura_fecha")
    public String date;

    @FieldAnnotation(sourceName = "factura_numero")
    public String billNumber;

    @FieldAnnotation(sourceName = "factura_monto")
    public BigDecimal amount;

    @FieldAnnotation(sourceName = "factura_iva")
    public BigDecimal vat;

    @FieldAnnotation(sourceName = "factura_totalreal")
    public BigDecimal realTotalAmount;

    @FieldAnnotation(sourceName = "factura_sumatoria")
    public BigDecimal accumulated;

    @FieldAnnotation(sourceName = "factura_total")
    public BigDecimal totalAmount;

    @FieldAnnotation(sourceName = "factura_estado")
    public String status;

    @FieldAnnotation(sourceName = "factura_dias")
    public Integer days;

    @FieldAnnotation(sourceName = "centro_id")
    public Long sourceId;

    @FieldAnnotation(sourceName = "comprobante_id")
    public Long receiptId;

    @FieldAnnotation(sourceName = "factura_vencimiento")
    public String expiredDate;

    @FieldAnnotation(sourceName = "factura_fechapago")
    public String paymentDate;

    @FieldAnnotation(sourceName = "factura_status")
    public String hiddenStatus;

    @FieldAnnotation(sourceName = "factura_ivadiferenciado")
    public Boolean differentiatedVat;
}
