package corar.etl.services;

import corar.etl.core.Operation;
import corar.etl.data.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ETLService {

    @Autowired
    ResourceService resourceService;

    @Autowired
    SourceExtractionService sourceExtractionService;

    @Autowired
    TargetExtractionService targetExtractionService;

    @Autowired
    CompareService compareService;

    public void migrate(){
        migrateBill();
    }

    private void migrateBill(){
        HashMap<Long, Object> sourceMap = sourceExtractionService.getBillMap();
        HashMap<Long, Object> targetMap = targetExtractionService.getBillMap();
        ArrayList<Operation> operationList = compareService.getChanges(sourceMap,targetMap);
        for(Operation operation : operationList) {
            System.out.println("Operation:" + operation.getOperation());
        }
    }

}
