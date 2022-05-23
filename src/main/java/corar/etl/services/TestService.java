package corar.etl.services;

import corar.etl.data.CommercialTransaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TestService {


    public HashMap<Long, Object> compareObject(){
        HashMap<Long, Object> sourceMap = new HashMap<>();
        sourceMap.put(1L, new CommercialTransaction());

        HashMap<Long, Object> targetMap = new HashMap<>();
        targetMap.put(1L, new CommercialTransaction());


        //Insert

        //Delete
        //Update

        return null;
    }
}
