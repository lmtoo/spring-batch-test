package cn.lmtoo.batch.launcher;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobLauncherDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MyJobIncrementor myJobIncrementor;

    @Bean
    public Job jobScheduledDemoJob() {
        return jobBuilderFactory.get("jobScheduledDemoJob")
                .incrementer(myJobIncrementor)
                .start(jobScheduledDemoStep())
                .build();
    }

    @Bean
    public Step jobScheduledDemoStep() {
        return stepBuilderFactory.get("jobScheduledDemoStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        JobParameters parameters = contribution.getStepExecution().getJobParameters();
                        System.out.println(parameters);
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Job jobLauncherDemoJob() {
        return jobBuilderFactory.get("jobLauncherDemoJob")
                .start(jobLauncherDemoStep())
                .build();
    }

    @Bean
    public Step jobLauncherDemoStep() {
        return stepBuilderFactory.get("jobLauncherDemoStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        JobParameters parameters = contribution.getStepExecution().getJobParameters();
                        System.out.println(parameters);
                        System.out.println(parameters.getString("msg"));
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
