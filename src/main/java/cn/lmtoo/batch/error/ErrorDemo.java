package cn.lmtoo.batch.error;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ErrorDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job errorDemoJob() {
        return jobBuilderFactory.get("errorDemoJob")
                .start(errorStep1())
                .next(errorStep2())
                .build();
    }

    @Bean
    public Step errorStep2() {
        return stepBuilderFactory.get("errorStep2")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    public Step errorStep1() {
        return stepBuilderFactory.get("errorStep1")
                .tasklet(errorHandling())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet errorHandling() {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                Map<String, Object> stepExecContext = chunkContext.getStepContext().getStepExecutionContext();
                if (stepExecContext.containsKey("qianfeng")) {
                    System.out.println("The second run will success");
                    return RepeatStatus.FINISHED;
                } else {
                    System.out.println("The first run will fail");
                    chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qianfeng", true);
                    throw new RuntimeException("error...");
                }
            }
        };
    }
}
