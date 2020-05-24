package cn.lmtoo.batch.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RetryDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("retryItemProcessor")
    private ItemProcessor<String,String> retryItemProcessor;

    @Bean
    public Job retryDemoJob() {
        return jobBuilderFactory.get("retryDemoJob")
                .start(retryDemoStep())
                .build();
    }

    @Bean
    public Step retryDemoStep() {
        return stepBuilderFactory.get("retryDemoStep")
                .<String, String>chunk(10)
                .reader(retryreader())
                .processor(retryItemProcessor)
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        for (String item : items) {
                            System.out.println(item);
                        }
                    }
                })
                .faultTolerant()
                .retry(CustomRetryException.class)
                .retryLimit(5)
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> retryreader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<String>(items);
    }

}
