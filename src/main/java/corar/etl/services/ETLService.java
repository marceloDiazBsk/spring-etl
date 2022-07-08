package corar.etl.services;

import corar.etl.data.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ETLService {

    @Autowired
    ResourceService resourceService;

    @Autowired
    SourceExtractionService sourceExtractionService;

    @Autowired
    TargetExtractionService targetExtractionService;

    public void migrate(){
        migrateBill();
    }

    private void migrateBill(){
        HashMap<Long, Bill> sourceMap = sourceExtractionService.getBillMap();
        HashMap<Long, Bill> targetMap = targetExtractionService.getBillMap();

    }

}
