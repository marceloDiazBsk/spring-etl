package corar.etl.services;

import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import corar.etl.core.Operation;
import corar.etl.data.Bill;
import corar.etl.emun.OperationType;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

    @Qualifier("postgres")
    @Autowired
    private DataSource dataSource;

    public void process(Class<?> resource, ArrayList<Operation> operationList) {
        List<Operation> insertList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.INSERT.getCode()))
                .collect(Collectors.toList());

        long insertStartTime = System.currentTimeMillis();
        insertOperationList(resource,insertList);
        long insertEndTime = System.currentTimeMillis();
        LOGGER.info("INSERT "+ resource.getSimpleName() +" QUANTITY: " + insertList.size() + " FINISH IN:  " + (insertEndTime - insertStartTime) + " ms");


        List<Operation> updateList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.UPDATE.getCode()))
                .collect(Collectors.toList());

        long updateStartTime = System.currentTimeMillis();
        updateOperationList(updateList);
        long updateEndTime = System.currentTimeMillis();
        LOGGER.info("UPDATE "+ resource.getSimpleName()+ " QUANTITY: " + updateList.size() + " FINISH IN:  " + (updateEndTime - updateStartTime) + " ms");

        List<Operation> deleteList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.DELETE.getCode()))
                .collect(Collectors.toList());

        long deleteStartTime = System.currentTimeMillis();
        long deleteEndTime = System.currentTimeMillis();
        LOGGER.info("DELETE " + resource.getSimpleName()+ " QUANTITY: " + deleteList.size() + " FINISH IN:  " + (deleteEndTime - deleteStartTime) + " ms");

    }

    private void insertOperationList(Class<?> resource, List<Operation> operationList) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(getInsertSQL(resource))) {
                for (Operation operation : operationList) {
                    Object source = operation.getData();
                    int index = 1;
                    for (Field field : resource.getDeclaredFields()) {
                        if (BigDecimal.class.equals(field.getType())) {
                            ps.setBigDecimal(index, (BigDecimal) field.get(source));
                        }
                        if(String.class.equals(field.getType())){
                            ps.setString(index, (String) field.get(source));
                        }
                        if(Integer.class.equals(field.getType())){
                            ps.setInt(index, (Integer) field.get(source));
                        }
                        if(Long.class.equals(field.getType())){
                            ps.setLong(index, (Long) field.get(source));
                        }
                        if(Boolean.class.equals(field.getType())){
                            ps.setBoolean(index,(Boolean) field.get(source));
                        }
                        index++;
                    }
                    ps.executeUpdate();
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("insertOperationList:", e);
        }

    }

    private void updateOperationList(List<Operation> operationList) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement st = connection.createStatement()) {
                for (Operation operation : operationList) {
                    Object data = operation.getData();
                    String updateScript = getUpdateSQL(data, operation.getChangeList());
                    //LOGGER.info("STATEMENT: " + updateScript);
                    st.executeUpdate(updateScript);
                }
            }
            connection.commit();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("updateOperationList:", e);
        }

    }

    private String getUpdateSQL(Object data, ArrayList<String> changeList)
            throws NoSuchFieldException, IllegalAccessException {
        StringBuilder statement = new StringBuilder();
        Class<?> dataClass = data.getClass();
        if (dataClass.isAnnotationPresent(TableAnnotation.class)) {
            TableAnnotation tableAnnotation = dataClass.getAnnotation(TableAnnotation.class);
            String targetTable = tableAnnotation.targetTable();
            statement
                    .append("UPDATE ")
                    .append(targetTable)
                    .append(" SET ");

            for (String change : changeList) {
                Field field = dataClass.getField(change);
                statement
                        .append(StringUtil.camelToSnakeCase(change))
                        .append(" = ")
                        .append("'")
                        .append(field.get(data).toString())
                        .append("'");
            }

            for (Field field : dataClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    statement
                            .append(" WHERE ")
                            .append(StringUtil.camelToSnakeCase(field.getName()))
                            .append(" = ")
                            .append("'")
                            .append(field.get(data))
                            .append("'");
                }
            }
        }
        return statement.toString();
    }

    private String getInsertSQL(Class<?> resource) {
        StringBuilder statement = new StringBuilder();
        StringBuilder parameterStatement = new StringBuilder();
        if (resource.isAnnotationPresent(TableAnnotation.class)) {
            TableAnnotation tableAnnotation = resource.getAnnotation(TableAnnotation.class);
            String targetTable = tableAnnotation.targetTable();

            statement
                    .append("INSERT INTO ")
                    .append(targetTable)
                    .append(" (");

            for (Field field : resource.getDeclaredFields()) {
                statement
                        .append(StringUtil.camelToSnakeCase(field.getName()))
                        .append(",");

                parameterStatement
                        .append("?,");
            }

            if (statement.length() > 0) statement.setLength(statement.length() - 1);
            if (parameterStatement.length() > 0) parameterStatement.setLength(parameterStatement.length() - 1);

            statement
                    .append(") VALUES( ")
                    .append(parameterStatement)
                    .append(")");
        }
        return statement.toString();
    }
}
