package corar.etl.job;

import corar.etl.services.ETLService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class ETLJob implements Job {

    private final static Logger LOGGER = LoggerFactory.getLogger(ETLJob.class);

    @Autowired
    ETLService etlService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            etlService.migrate();
        } catch (SQLException e) {
            LOGGER.error("",e);
        }
    }
}
