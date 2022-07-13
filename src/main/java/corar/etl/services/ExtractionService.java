package corar.etl.services;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import corar.etl.emun.ResourceType;
import corar.etl.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Service
public class ExtractionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExtractionService.class);

    @Qualifier("mysql")
    @Autowired
    private DataSource sourceDataSource;

    @Qualifier("postgres")
    @Autowired
    private DataSource targetDataSource;

    public HashMap<Long,Object> getMap(Class<?> resource, String type){
        HashMap<Long,Object> map = new HashMap<>();
        DataSource dataSource = getDataSource(type);
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(getSelectSQL(resource, type))){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Object object = resource.newInstance();
                        Field identifier = null;
                        for (Field field : resource.getDeclaredFields()) {
                            if(field.isAnnotationPresent(FieldAnnotation.class)){
                                String fieldName = getFieldName(field, type);
                                if (BigDecimal.class.equals(field.getType()))
                                    field.set(object, rs.getBigDecimal(fieldName));

                                if(String.class.equals(field.getType()))
                                    field.set(object, rs.getString(fieldName));

                                if(Integer.class.equals(field.getType()))
                                    field.set(object, rs.getInt(fieldName));

                                if(Long.class.equals(field.getType()))
                                    field.set(object, rs.getLong(fieldName));

                                if(Boolean.class.equals(field.getType()))
                                    field.set(object, rs.getBoolean(fieldName));

                                if(field.isAnnotationPresent(Id.class)) identifier = field;
                            }
                        }

                        if(identifier != null) map.put((Long)identifier.get(object), object);

                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (SQLException e) {
            LOGGER.info("getBillMap " + e);
        }
        return map;
    }



    private String getSelectSQL(Class<?> resource, String type){
        StringBuilder sql = new StringBuilder();
        if (resource.isAnnotationPresent(TableAnnotation.class)) {
            String sourceTable = getTableName(resource, type);
            sql.append(" SELECT ");

            for (Field field : resource.getDeclaredFields()){
                if(field.isAnnotationPresent(FieldAnnotation.class)){
                    FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
                    sql
                            .append(fieldAnnotation.sourceName())
                            .append(',');
                }
            }

            if (sql.length() > 0) sql.setLength(sql.length() - 1);

            sql
                    .append(" FROM ")
                    .append(sourceTable)
                    .append(';');
        }

        return sql.toString();
    }

    private String getFieldName(Field field, String type) {
        FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
        return type.equals(ResourceType.SOURCE.name()) ?
                fieldAnnotation.sourceName() : StringUtil.camelToSnakeCase(field.getName());
    }

    private String getTableName(Class<?> resource, String type){
        TableAnnotation tableAnnotation = resource.getAnnotation(TableAnnotation.class);
        return type.equals(ResourceType.SOURCE.name()) ?
                tableAnnotation.sourceTable() : tableAnnotation.targetTable();
    }

    private DataSource getDataSource(String type) {
        return type.equals(ResourceType.SOURCE.name()) ? sourceDataSource : targetDataSource;
    }



}
