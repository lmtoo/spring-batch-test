package cn.lmtoo.batch.launcher;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyJobIncrementor implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        if (parameters == null) {
            parameters = new JobParameters();
        }
        return new JobParametersBuilder(parameters)
                .addDate("jobScheduledParam", new Date())
                .toJobParameters();
    }
}
