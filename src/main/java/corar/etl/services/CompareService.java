package corar.etl.services;

import corar.etl.core.Operation;
import corar.etl.emun.OperationType;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CompareService{

    private final static Logger LOGGER = LoggerFactory.getLogger(CompareService.class);

    public ArrayList<Operation> getChanges(HashMap<Long, Object> sourceMap, HashMap<Long, Object> targetMap){
        LOGGER.info("sourceMap: " + sourceMap);
        LOGGER.info("targetMap: " + targetMap);
        Javers javers = JaversBuilder.javers().build();
        ArrayList<Operation> operationList = new ArrayList<>();
        for(HashMap.Entry<Long, Object> entry : sourceMap.entrySet()){
            Object source = entry.getValue();
            if(targetMap.containsKey(entry.getKey())){
                Object target = targetMap.get(entry.getKey());

                //Normalize object, use same precision for numeric values, etc;
                normalizeObject(source);
                normalizeObject(target);

                //Evaluate with the target object if we have changes
                Diff diff = javers.compare(source, target);
                List<ValueChange> changeList = diff.getChangesByType(ValueChange.class);
                if(changeList.size() > 0){
                    ArrayList<String> propertiesChanged = new ArrayList<>();
                    changeList.forEach( valueChange -> propertiesChanged.add(valueChange.getPropertyName()));
                    propertiesChanged.remove("id"); //Remove id because never we will have the same id
                    if(propertiesChanged.size() > 0){
                        //We have changes then create an update operation
                        operationList.add(new Operation(OperationType.UPDATE.getCode(), propertiesChanged,source));
                    }
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

    private void normalizeObject(Object object){
        if(object != null){
            for(Field field : object.getClass().getDeclaredFields()){
                if(BigDecimal.class.equals(field.getType())){
                    try {
                        BigDecimal value = (BigDecimal) field.get(object);
                        if(value != null) field.set(object, value.setScale(4, RoundingMode.HALF_UP));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
