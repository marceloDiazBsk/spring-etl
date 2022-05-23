package corar.etl.services;

import corar.etl.data.CommercialTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class ExtractionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExtractionService.class);

    @Qualifier("mysql")
    @Autowired
    private DataSource mysqlDataSource;

    public ArrayList<CommercialTransaction> getCommercialTransactionList(){
        ArrayList<CommercialTransaction> commercialTransactionList = new ArrayList<>();
        try(Connection connection = mysqlDataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(getCommercialTransactionSQL())){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        CommercialTransaction commercialTransaction = new CommercialTransaction();
                        commercialTransaction.setId(rs.getLong("id"));
                        commercialTransaction.setDate(rs.getString("date"));
                        commercialTransaction.setType(rs.getString("type"));
                        commercialTransaction.setGroup(rs.getString("group_code"));
                        commercialTransaction.setAmount(rs.getBigDecimal("amount"));
                        commercialTransaction.setConcept(rs.getString("concept"));
                        commercialTransactionList.add(commercialTransaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.info("FINISH getCommercialTransactionList");
        return commercialTransactionList;
    }

    private String getCommercialTransactionSQL(){
        return "select commercial_transaction_id as id, commercial_transaction_fecha as date, " +
                "commercial_transaction_tipo as type, commercial_transaction_grupo as group_code, " +
                "commercial_transaction_monto as amount, commercial_transaction_concept as concept " +
                "from commercial_transaction;";
    }

}
