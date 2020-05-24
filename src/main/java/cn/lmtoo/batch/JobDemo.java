package cn.lmtoo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobDemoJob() {
        return jobBuilderFactory.get("jobDemoJob")
//                .start(jobDemoStep1())
//                .next(jobDemoStep2())
//                .next(jobDemoStep3())
//                .build();

                .start(jobDemoStep1()).on("COMPLETED").to(jobDemoStep2())
                .from(jobDemoStep2()).on("COMPLETED").to(jobDemoStep3())
                .from(jobDemoStep3()).end()
                .build();
    }

    @Bean
    public Step jobDemoStep1() {
        return stepBuilderFactory.get("jobDemoStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobDemoStep1");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step jobDemoStep2() {
        return stepBuilderFactory.get("jobDemoStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobDemoStep2");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step jobDemoStep3() {
        return stepBuilderFactory.get("jobDemoStep3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("jobDemoStep3");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
