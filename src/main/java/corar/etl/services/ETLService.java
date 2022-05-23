package corar.etl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ETLService {

    @Autowired
    ExtractionService extractionService;

    @Autowired
    LoadService loadService;

    public void commercial(){
        loadService.setCommercialTransactionList(extractionService.getCommercialTransactionList());
    }
}
