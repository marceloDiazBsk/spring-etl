package corar.etl.data;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@TableAnnotation(sourceTable = "centrocosto", targetTable = "source_copy")
@ToString
public class Source {

    @Id
    @FieldAnnotation(sourceName = "centro_id")
    public Long sourceId;

    @FieldAnnotation(sourceName = "centro_nombre")
    public String name;

    @FieldAnnotation(sourceName = "centro_status")
    public Boolean status;

    @FieldAnnotation(sourceName = "centro_hidden")
    public Boolean hidden;

    @FieldAnnotation(sourceName = "centro_timestamp")
    public String createdAt;


}
