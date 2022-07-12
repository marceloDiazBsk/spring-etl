package corar.etl.services;

import corar.etl.data.Bill;
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
import java.util.HashMap;

@Service
public class TargetExtractionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TargetExtractionService.class);

    @Qualifier("postgres")
    @Autowired
    private DataSource dataSource;

    public HashMap<Long,Object> getBillMap(){
        HashMap<Long,Object> map = new HashMap<>();
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(getBillSQL())){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Bill bill = new Bill();
                        bill.setId(rs.getLong("id"));
                        bill.setBillId(rs.getLong("bill_id"));
                        bill.setProviderId(rs.getLong("provider_id"));
                        bill.setModeId(rs.getLong("mode_id"));
                        bill.setCurrencyId(rs.getLong("currency_id"));
                        bill.setDate(rs.getString("date"));
                        bill.setBillNumber(rs.getString("bill_number"));
                        bill.setAmount(rs.getBigDecimal("amount"));
                        bill.setVat(rs.getBigDecimal("vat"));
                        bill.setRealTotalAmount(rs.getBigDecimal("real_total_amount"));
                        bill.setAccumulated(rs.getBigDecimal("accumulated"));
                        bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                        bill.setStatus(rs.getString("status"));
                        bill.setDays(rs.getInt("days"));
                        bill.setSourceId(rs.getLong("source_id"));
                        bill.setReceiptId(rs.getLong("receipt_id"));
                        bill.setExpiredDate(rs.getString("expired_date"));
                        bill.setPaymentDate(rs.getString("payment_date"));
                        bill.setHiddenStatus(rs.getString("hidden_status"));
                        bill.setDifferentiatedVat(rs.getBoolean("differentiated_vat"));

                        map.put(rs.getLong("bill_id"), bill);
                    }
                }
            }
        }catch (SQLException e) {
            LOGGER.info("getBillMap " + e);
        }
        return map;
    }

    private String getBillSQL(){
        return "select id, bill_id, provider_id, mode_id, currency_id, date, bill_number, " +
                "amount, vat, real_total_amount, accumulated, total_amount, status, " +
                "days, source_id, receipt_id, expired_date, payment_date, hidden_status, differentiated_vat " +
                "from bill_copy";
    }
}
