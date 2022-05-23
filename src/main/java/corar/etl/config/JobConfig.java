package corar.etl.config;

import corar.etl.job.ETLJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {
    @Bean
    public JobDetail jobDetails() {
        return JobBuilder.newJob(ETLJob.class).withIdentity("ETLJob").storeDurably().build();
    }

    @Bean
    public Trigger jobTrigger(JobDetail jobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetails)
                .withIdentity("jobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("* 0/5 * ? * * *"))
                .build();
    }

}
