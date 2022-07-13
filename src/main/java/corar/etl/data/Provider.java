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
@TableAnnotation(sourceTable = "proveedor", targetTable = "providers_copy")
@ToString
public class Provider {

    @Id
    @FieldAnnotation(sourceName = "proveedor_id")
    public Long providerId;

    @FieldAnnotation(sourceName = "proveedor_nombre")
    public String name;

    @FieldAnnotation(sourceName = "proveedor_nombrefantasia")
    public String fantasyName;

    @FieldAnnotation(sourceName = "proveedor_ruc")
    public String fiscalNumber;

    @FieldAnnotation(sourceName = "proveedor_direccion")
    public String address;

    @FieldAnnotation(sourceName = "departamento_id")
    public Long departmentId;

    @FieldAnnotation(sourceName = "ciudad_id")
    public Long cityId;

    @FieldAnnotation(sourceName = "proveedor_rubro")
    public String area;

    @FieldAnnotation(sourceName = "proveedor_telefono")
    public String phone;

    @FieldAnnotation(sourceName = "proveedor_status")
    public Boolean status;

    @FieldAnnotation(sourceName = "proveedor_hidden")
    public Boolean hidden;

    @FieldAnnotation(sourceName = "proveedor_timestamp")
    public String createdAt;


}
