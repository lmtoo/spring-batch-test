package cn.lmtoo.batch.itemprocessor;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ItemProcessorDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemProcessorDemoJob() {
        return jobBuilderFactory.get("itemProcessorDemoJob")
                .start(itemProcessorDemoStep())
                .build();
    }

    @Bean
    public FlatFileItemReader<Customer> itemProcessorDemoDbReader() {
        FlatFileItemReader<Customer> reader =new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("customer.csv"));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id","firstName","lastName","birthday");

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
    public Step itemProcessorDemoStep() {
        return stepBuilderFactory.get("itemProcessorDemoStep")
                .<Customer,Customer>chunk(10)
                .reader(itemProcessorDemoDbReader())
                .processor(process())
                .writer(new ItemWriter<Customer>() {
                    @Override
                    public void write(List<? extends Customer> items) throws Exception {
                        for (Customer customer : items) {
                            System.out.println(customer);
                        }
                    }
                })
                .build();
    }


    @Bean
    public CompositeItemProcessor<Customer, Customer> process() {
        CompositeItemProcessor<Customer, Customer> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(
                new ItemProcessor<Customer, Customer>() {
                    @Override
                    public Customer process(Customer item) throws Exception {
                        item.setFirstName(item.getFirstName().toUpperCase());
                        return item;
                    }
                },
                new ItemProcessor<Customer, Customer>() {
                    @Override
                    public Customer process(Customer item) throws Exception {
                        if (item.getId() % 2 == 0) {
                            return item;
                        }else {
                            return null;
                        }
                    }
                }
        ));
        return processor;
    }
}
