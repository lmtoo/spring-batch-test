package cn.lmtoo.batch.skip;

import cn.lmtoo.batch.retry.CustomRetryException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
public class SkipDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("retryItemProcessor")
    private ItemProcessor<String,String> retryItemProcessor;

    @Bean
    public Job skipDemoJob() {
        return jobBuilderFactory.get("skipDemoJob")
                .start(skipDemoStep())
                .build();
    }

    @Bean
    public Step skipDemoStep() {
        return stepBuilderFactory.get("skipDemoStep")
                .<String, String>chunk(10)
                .reader(skipreader())
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
                .skip(CustomRetryException.class)
                .skipLimit(5)
                .listener(new SkipListener<String, String>() {
                    @Override
                    public void onSkipInRead(Throwable t) {

                    }

                    @Override
                    public void onSkipInWrite(String item, Throwable t) {

                    }

                    @Override
                    public void onSkipInProcess(String item, Throwable t) {

                    }
                })
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> skipreader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<String>(items);
    }

}
