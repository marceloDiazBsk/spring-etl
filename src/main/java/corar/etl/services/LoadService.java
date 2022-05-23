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
public class LoadService {

    @Qualifier("postgres")
    @Autowired
    private DataSource postgresDataSource;

    private final static Logger LOGGER = LoggerFactory.getLogger(LoadService.class);


    public void setCommercialTransactionList(ArrayList<CommercialTransaction> commercialTransactionList){
        try(Connection connection = postgresDataSource.getConnection()){
            connection.setAutoCommit(false);
            try(PreparedStatement psExist = connection.prepareStatement(getExistCommercialTransactionSQL());
                PreparedStatement psInsert = connection.prepareStatement(setCommercialTransactionSQL()) ;
                PreparedStatement psUpdate = connection.prepareStatement(getUpdateCommercialTransactionSQL())){
                for (CommercialTransaction commercialTransaction : commercialTransactionList) {
                    psExist.setLong(1, commercialTransaction.getId());
                    try(ResultSet rsExist = psExist.executeQuery()){
                        if(rsExist.next()){
                            psUpdate.setString(1, commercialTransaction.getDate());
                            psUpdate.setString(2, commercialTransaction.getGroup());
                            psUpdate.setString(3, commercialTransaction.getType());
                            psUpdate.setString(4, commercialTransaction.getConcept());
                            psUpdate.setBigDecimal(5, commercialTransaction.getAmount());
                            psUpdate.setLong(6, commercialTransaction.getId());
                            psUpdate.executeUpdate();
                        }else{
                            psInsert.setString(1, commercialTransaction.getDate());
                            psInsert.setString(2, commercialTransaction.getGroup());
                            psInsert.setString(3, commercialTransaction.getType());
                            psInsert.setString(4, commercialTransaction.getConcept());
                            psInsert.setBigDecimal(5, commercialTransaction.getAmount());
                            psInsert.setLong(6, commercialTransaction.getId());
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOGGER.info("FINISH setCommercialTransactionList");
    }

    private String setCommercialTransactionSQL() {
        return "INSERT INTO commercial_fact(date_id, group_id, type_id, concept_id, amount_usd, external_id) VALUES ( " +
                "(select date_id from time_dim where date = to_date(?,'YYYY-MM-DD')), " +
                "(select group_id from group_dim where group_code = ?), " +
                "(select type_id from type_dim where type_code = ?), " +
                "(select concept_id from concept_dim where concept_code = ?), " +
                "?, " +
                "?" +
                ");";
    }

    private String getExistCommercialTransactionSQL(){
        return "SELECT 1 FROM commercial_fact where external_id = ?";
    }

    private String getUpdateCommercialTransactionSQL(){
        return "UPDATE commercial_fact set date_id = (select date_id from time_dim where date = to_date(?,'YYYY-MM-DD')), " +
                "group_id = (select group_id from group_dim where group_code = ?), " +
                "type_id = (select type_id from type_dim where type_code = ?), " +
                "concept_id = (select concept_id from concept_dim where concept_code = ?), " +
                "amount_usd = ?  " +
               "WHERE external_id = ? ";
    }

}
