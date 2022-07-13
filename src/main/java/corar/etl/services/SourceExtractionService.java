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
public class SourceExtractionService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SourceExtractionService.class);

    @Qualifier("mysql")
    @Autowired
    private DataSource dataSource;

    public HashMap<Long,Object> getBillMap(){
        HashMap<Long,Object> map = new HashMap<>();
        try(Connection connection = dataSource.getConnection()){
            try(PreparedStatement ps = connection.prepareStatement(getBillSQL())){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        Bill bill = new Bill();
                        bill.setBillId(rs.getLong("factura_id"));
                        bill.setProviderId(rs.getLong("proveedor_id"));
                        bill.setModeId(rs.getLong("proveedor_id"));
                        bill.setCurrencyId(rs.getLong("moneda_id"));
                        bill.setDate(rs.getString("factura_fecha"));
                        bill.setBillNumber(rs.getString("factura_numero"));
                        bill.setAmount(rs.getBigDecimal("factura_monto"));
                        bill.setVat(rs.getBigDecimal("factura_iva"));
                        bill.setRealTotalAmount(rs.getBigDecimal("factura_totalreal"));
                        bill.setAccumulated(rs.getBigDecimal("factura_sumatoria"));
                        bill.setTotalAmount(rs.getBigDecimal("factura_total"));
                        bill.setStatus(rs.getString("factura_estado"));
                        bill.setDays(rs.getInt("factura_dias"));
                        bill.setSourceId(rs.getLong("centro_id"));
                        bill.setReceiptId(rs.getLong("comprobante_id"));
                        bill.setExpiredDate(rs.getString("factura_vencimiento"));
                        bill.setPaymentDate(rs.getString("factura_fechapago"));
                        bill.setHiddenStatus(rs.getString("factura_hidden"));
                        bill.setDifferentiatedVat(rs.getBoolean("factura_ivadiferenciado"));

                        map.put(rs.getLong("factura_id"), bill);
                    }
                }
            }
        }catch (SQLException e) {
            LOGGER.info("getBillMap " + e);
        }
        return map;
    }

    private String getBillSQL(){
        return "select factura_id, proveedor_id, forma_id, moneda_id, factura_fecha, " +
                "factura_numero, factura_monto, factura_iva, factura_totalreal, factura_sumatoria, " +
                "factura_total, factura_estado, factura_dias, centro_id, comprobante_id, " +
                "factura_vencimiento, factura_fechapago, factura_status, factura_hidden, " +
                "factura_timestamp, factura_ivadiferenciado from factura order by factura_id asc limit 3; ";
    }
}
