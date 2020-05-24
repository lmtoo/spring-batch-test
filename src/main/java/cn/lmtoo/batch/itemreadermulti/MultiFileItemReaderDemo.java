package cn.lmtoo.batch.itemreadermulti;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

@Configuration
public class MultiFileItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath*:/cust*.csv")
    private Resource[] fileResources;

    @Autowired
    @Qualifier("multiFileWriter")
    private ItemWriter<? super Customer> multiFileWriter;

    @Bean
    public Job multiFileItemReaderDemoJob() {
        return jobBuilderFactory.get("multiFileItemReaderDemoJob")
                .start(multiFileItemReaderDemoStep())
                .build();
    }

    @Bean
    public Step multiFileItemReaderDemoStep() {
        return stepBuilderFactory.get("multiFileItemReaderDemoStep")
                .<Customer, Customer>chunk(100)
                .reader(multiFileReader())
                .writer(multiFileWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> multiFlatFileReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "firstName", "lastName", "birthday");

        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                Customer customer = new Customer();
                customer.setId(fieldSet.readLong("id"));
                customer.setFirstName(fieldSet.readString("firstName"));
                customer.setLastName(fieldSet.readString("lastName"));
                customer.setBirthday(fieldSet.readString("birthday"));
                return customer;
            }
        });
        mapper.afterPropertiesSet();
        reader.setLineMapper(mapper);
        return reader;
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Customer> multiFileReader() {
        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();
        reader.setDelegate(multiFlatFileReader());
        reader.setResources(fileResources);
        return reader;
    }
}
