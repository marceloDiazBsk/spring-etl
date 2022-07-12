package corar.etl.services;

import com.google.common.base.CaseFormat;
import corar.etl.annotations.Id;
import corar.etl.annotations.TableAnnotation;
import corar.etl.core.Operation;
import corar.etl.data.Bill;
import corar.etl.emun.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.reflect.Field;
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

    public void processBill(ArrayList<Operation> operationList) {
        List<Operation> insertList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.INSERT.getCode()))
                .collect(Collectors.toList());

        long insertStartTime = System.currentTimeMillis();
        insertBillList(insertList);
        long insertEndTime = System.currentTimeMillis();
        System.out.println("INSERT QUANTITY: " + insertList.size() + " FINISH IN:  " + (insertEndTime - insertStartTime) + " ms");


        List<Operation> updateList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.UPDATE.getCode()))
                .collect(Collectors.toList());

        long updateStartTime = System.currentTimeMillis();
        updateBillList(updateList);
        long updateEndTime = System.currentTimeMillis();
        System.out.println("UPDATE QUANTITY: " + updateList.size() + " FINISH IN:  " + (updateEndTime - updateStartTime) + " ms");

        List<Operation> deleteList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.DELETE.getCode()))
                .collect(Collectors.toList());

        long deleteStartTime = System.currentTimeMillis();
        long deleteEndTime = System.currentTimeMillis();
        System.out.println("DELETE QUANTITY: " + deleteList.size() + " FINISH IN:  " + (deleteEndTime - deleteStartTime) + " ms");

    }

    private void insertBillList(List<Operation> operationList) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement(getInsertBillSQL())) {
                for (Operation operation : operationList) {
                    Bill source = (Bill) operation.getData();
                    ps.setLong(1, source.getBillId());
                    ps.setLong(2, source.getProviderId());
                    ps.setLong(3, source.getModeId());
                    ps.setLong(4, source.getCurrencyId());
                    ps.setString(5, source.getDate());
                    ps.setString(6, source.getNumber());
                    ps.setBigDecimal(7, source.getAmount());
                    ps.setBigDecimal(8, source.getVat());
                    ps.setBigDecimal(9, source.getRealTotalAmount());
                    ps.setBigDecimal(10, source.getAccumulated());
                    ps.setBigDecimal(11, source.getTotalAmount());
                    ps.setString(12, source.getStatus());
                    ps.setInt(13, source.getDays());
                    ps.setLong(14, source.getSourceId());
                    ps.setLong(15, source.getReceiptId());
                    ps.setString(16, source.getExpiredDate());
                    ps.setString(17, source.getPaymentDate());
                    ps.setString(18, source.getHiddenStatus());
                    ps.setBoolean(19, source.getDifferentiatedVat());

                    ps.executeUpdate();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("insertBillList:", e);
        }

    }

    private void updateBillList(List<Operation> operationList) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (Statement st = connection.createStatement()) {
                for (Operation operation : operationList) {
                    Object data = operation.getData();
                    String updateScript = getUpdateSQL(data, operation.getChangeList());
                    LOGGER.info("STATEMENT: " + updateScript);
                    st.executeUpdate(updateScript);
                }
            }
            connection.commit();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("updateBillList:", e);
        }

    }

    private String getInsertBillSQL() {
        return "INSERT INTO public.bill_copy(" +
                "bill_id, provider_id, mode_id, currency_id, date, \"number\", " +
                "amount, vat, real_total_amount, accumulated, total_amount, status, days, " +
                "source_id, receipt_id, expired_date, payment_date, hidden_status, differentiated_vat)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    }

    private String camelToSnakeCase(String value) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }

    private String getUpdateSQL(Object data, ArrayList<String> changeList) throws NoSuchFieldException, IllegalAccessException {
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
                        .append(camelToSnakeCase(change))
                        .append(" = ")
                        .append("'")
                        .append(field.get(data).toString())
                        .append("'");
            }

            for (Field field : dataClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    statement
                            .append(" WHERE ")
                            .append(camelToSnakeCase(field.getName()))
                            .append(" = ")
                            .append("'")
                            .append(field.get(data))
                            .append("'");
                }
            }
        }
        return statement.toString();
    }
}
