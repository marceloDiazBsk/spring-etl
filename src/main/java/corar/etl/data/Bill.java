package corar.etl.data;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.TableAnnotation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableAnnotation(sourceName = "factura")
public class Bill {
    private Long id;

    @FieldAnnotation(sourceName = "factura_id")
    private Long billId;

    @FieldAnnotation(sourceName = "proveedor_id")
    private Long providerId;

    @FieldAnnotation(sourceName = "forma_id")
    private Long modelId;

    @FieldAnnotation(sourceName = "moneda_id")
    private Long currencyId;

    @FieldAnnotation(sourceName = "factura_fecha")
    private String date;

    @FieldAnnotation(sourceName = "factura_numero")
    private String number;

    @FieldAnnotation(sourceName = "factura_monto")
    private BigDecimal amount;

    @FieldAnnotation(sourceName = "factura_iva")
    private BigDecimal vat;

    @FieldAnnotation(sourceName = "factura_totalreal")
    private BigDecimal realTotalAmount;

    @FieldAnnotation(sourceName = "factura_sumatoria")
    private BigDecimal accumulated;

    @FieldAnnotation(sourceName = "factura_total")
    private BigDecimal totalAmount;

    @FieldAnnotation(sourceName = "factura_estado")
    private String status;

    @FieldAnnotation(sourceName = "factura_dias")
    private Integer days;

    @FieldAnnotation(sourceName = "centro_id")
    private Long sourceId;

    @FieldAnnotation(sourceName = "comprobante_id")
    private Long receiptId;

    @FieldAnnotation(sourceName = "factura_vencimiento")
    private String expiredDate;

    @FieldAnnotation(sourceName = "factura_fechapago")
    private String paymentDate;

    @FieldAnnotation(sourceName = "factura_status")
    private String hiddenStatus;

    @FieldAnnotation(sourceName = "factura_ivadiferenciado")
    private Boolean differentiatedVat;
}
