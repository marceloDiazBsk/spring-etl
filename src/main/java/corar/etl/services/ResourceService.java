package corar.etl.services;

import corar.etl.data.Bill;
import corar.etl.data.BillDetail;
import corar.etl.data.Provider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ResourceService {

    public List<Class<?>> getResourceList(){
        return Arrays.asList(
                Bill.class,
                BillDetail.class,
                Provider.class
        );
    }
}
