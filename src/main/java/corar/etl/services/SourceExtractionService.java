package corar.etl.services;

import corar.etl.annotations.FieldAnnotation;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
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
public class SourceExtractionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceExtractionService.class);

    @Qualifier("mysql")
    @Autowired
    private DataSource dataSource;

    public HashMap<Long,Object> getMap(Class<?> resource){
        HashMap<Long,Object> map = new HashMap<>();
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(getSelectSQL(resource))){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Object object = resource.newInstance();
                        Field identifier = null;
                        for (Field field : resource.getDeclaredFields()) {
                            if(field.isAnnotationPresent(FieldAnnotation.class)){
                                FieldAnnotation fieldAnnotation = field.getAnnotation(FieldAnnotation.class);
                                if (BigDecimal.class.equals(field.getType()))
                                    field.set(object, rs.getBigDecimal(fieldAnnotation.sourceName()));

                                if(String.class.equals(field.getType()))
                                    field.set(object, rs.getString(fieldAnnotation.sourceName()));

                                if(Integer.class.equals(field.getType()))
                                    field.set(object, rs.getInt(fieldAnnotation.sourceName()));

                                if(Long.class.equals(field.getType()))
                                    field.set(object, rs.getLong(fieldAnnotation.sourceName()));

                                if(Boolean.class.equals(field.getType()))
                                    field.set(object, rs.getBoolean(fieldAnnotation.sourceName()));

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

    private String getSelectSQL(Class<?> resource){
        StringBuilder sql = new StringBuilder();
        if (resource.isAnnotationPresent(TableAnnotation.class)) {
            TableAnnotation tableAnnotation = resource.getAnnotation(TableAnnotation.class);
            String sourceTable = tableAnnotation.sourceTable();
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



}
