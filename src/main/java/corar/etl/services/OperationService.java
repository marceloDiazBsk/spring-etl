package corar.etl.services;

import corar.etl.core.Operation;
import corar.etl.data.Bill;
import corar.etl.emun.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

    @Qualifier("postgres")
    @Autowired
    private DataSource dataSource;

    public void processBill(ArrayList<Operation> operationList){
        List<Operation> insertList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.INSERT.getCode()))
                .collect(Collectors.toList());
        insertBillList(insertList);

        List<Operation> updateList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.UPDATE.getCode()))
                .collect(Collectors.toList());

        List<Operation> deleteList = operationList
                .stream()
                .filter(operation -> operation.getOperation().equals(OperationType.UPDATE.getCode()))
                .collect(Collectors.toList());

    }

    private void insertBillList(List<Operation> insertList){
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement ps = connection.prepareStatement(getBillSQL())){
                for (Operation operation : insertList) {
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
        }catch (SQLException e) {
            LOGGER.error("insertBillList:", e);
        }

    }

    private String getBillSQL(){
        return "INSERT INTO public.bill_copy(" +
                "bill_id, provider_id, mode_id, currency_id, date, \"number\", " +
                "amount, vat, real_total_amount, accumulated, total_amount, status, days, " +
                "source_id, receipt_id, expired_date, payment_date, hidden_status, differentiated_vat)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    }
}
