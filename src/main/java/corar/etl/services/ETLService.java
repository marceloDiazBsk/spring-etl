package corar.etl.services;

import corar.etl.core.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Autowired
    OperationService operationService;

    public void migrate(){
        List<Class<?>> resourceList = resourceService.getResourceList();
        for (Class<?> resource : resourceList) {
          migrateResource(resource);
        }
    }

    private void migrateResource(Class<?> resource) {
        HashMap<Long, Object> sourceMap = sourceExtractionService.getBillMap();
        HashMap<Long, Object> targetMap = targetExtractionService.getBillMap();
        ArrayList<Operation> operationList = compareService.getChanges(sourceMap,targetMap);
        operationService.process(resource, operationList);
    }

}
