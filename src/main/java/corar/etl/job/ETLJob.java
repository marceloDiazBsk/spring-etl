package corar.etl.job;

import corar.etl.services.ETLService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class ETLJob implements Job {

    @Autowired
    ETLService etlService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        etlService.commercial();
    }
}
