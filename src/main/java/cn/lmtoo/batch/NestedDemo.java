package cn.lmtoo.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class NestedDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private Job childJobOne;

    @Autowired
    private Job childJobTwo;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public Job parentJob(JobRepository repository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory.get("parentJob")
                .start(childJob1(repository, transactionManager))
                .next(childJob2(repository, transactionManager))
                .build();
    }

    @Bean
    public Step childJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob1"))
                .job(childJobOne)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step childJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(childJobTwo)
                .launcher(jobLauncher)
                .repository(repository)
                .transactionManager(transactionManager)
                .build();
    }
}
