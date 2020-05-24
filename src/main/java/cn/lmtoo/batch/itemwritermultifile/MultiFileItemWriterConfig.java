package cn.lmtoo.batch.itemwritermultifile;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiFileItemWriterConfig {

    @Bean
    public ClassifierCompositeItemWriter<Customer> multiFileItemWriter() {
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            public ItemWriter<? super Customer> classify(Customer classifiable) {


                return null;
            }
        });
        return writer;
    }
}
