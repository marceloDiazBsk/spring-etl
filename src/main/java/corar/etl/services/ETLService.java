package corar.etl.services;

import corar.etl.core.Operation;
import corar.etl.emun.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ETLService {

    @Autowired
    ResourceService resourceService;

    @Autowired
    ExtractionService extractionService;

    @Autowired
    CompareService compareService;

    @Autowired
    OperationService operationService;

    public void migrate() throws SQLException {
        List<Class<?>> resourceList = resourceService.getResourceList();
        for (Class<?> resource : resourceList) {
          migrateResource(resource);
        }
    }

    private void migrateResource(Class<?> resource) throws SQLException {
        HashMap<Long, Object> sourceMap = extractionService.getMap(resource, ResourceType.SOURCE.name());
        HashMap<Long, Object> targetMap = extractionService.getMap(resource, ResourceType.TARGET.name());
        ArrayList<Operation> operationList = compareService.getChanges(sourceMap,targetMap);
        operationService.process(resource, operationList);
    }

}
