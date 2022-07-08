package corar.etl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ETLService2 {

    @Autowired
    ResourceService resourceService;

    @Autowired
    ExtractionService2 extractionService;



    public void migrate(){
        List<Class> resourceList = resourceService.getResourceList();
        for(Class resource: resourceList) {

        }
    }

}
