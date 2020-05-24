package cn.lmtoo.batch.itemwriterfile;

import cn.lmtoo.batch.itemreaderfile.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class FileItemWriterConfig {

    @Bean
    public FlatFileItemWriter<Customer> flatFileItemWriterFile() throws Exception {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        String path = "/Users/lile/IdeaProjects/spring-batch-test/src/main/resources/customer.txt";
        writer.setResource(new FileSystemResource(path));

        writer.setLineAggregator(new LineAggregator<Customer>() {

            ObjectMapper mapper = new ObjectMapper();

            @Override
            public String aggregate(Customer item) {
                String line = null;
                try {
                    line = mapper.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return line;
            }
        });
        writer.afterPropertiesSet();
        return writer;
    }
}
