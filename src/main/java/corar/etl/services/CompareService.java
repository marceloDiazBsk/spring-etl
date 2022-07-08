package corar.etl.services;

import corar.etl.core.Operation;
import corar.etl.emun.OperationType;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CompareService{

    public ArrayList<Operation> getChanges(HashMap<Long, Object> sourceMap, HashMap<Long, Object> targetMap){
        Javers javers = JaversBuilder.javers().build();
        ArrayList<Operation> operationList = new ArrayList<>();
        for(HashMap.Entry<Long, Object> entry : sourceMap.entrySet()){
            Object source = entry.getValue();
            if(targetMap.containsKey(entry.getKey())){
                Object target = targetMap.get(entry.getKey());
                //Evaluate with the target object if we have changes
                Diff diff = javers.compare(source, target);
                List<ValueChange> changeList = diff.getChangesByType(ValueChange.class);
                if(changeList.size() > 0){
                    ArrayList<String> propertiesChanged = new ArrayList<>();
                    changeList.forEach( valueChange -> propertiesChanged.add(valueChange.getPropertyName()));
                    operationList.add(new Operation(OperationType.UPDATE.getCode(), propertiesChanged,source));
                }
            }else{
                //If targetMap no contains object, need to insert
                operationList.add(new Operation(OperationType.INSERT.getCode(),null,source));
            }
        }

        for(HashMap.Entry<Long, Object> entry : targetMap.entrySet()){
            if(!sourceMap.containsKey(entry.getKey()))
                operationList.add(new Operation(OperationType.DELETE.getCode(),null,null));
        }

        return operationList;
    }
}
