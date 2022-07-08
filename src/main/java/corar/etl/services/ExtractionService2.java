package corar.etl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class ExtractionService2 {

    @Qualifier("mysql")
    @Autowired
    private DataSource mysqlDataSource;


}
