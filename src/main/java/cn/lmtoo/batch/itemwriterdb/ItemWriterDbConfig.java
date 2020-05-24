package cn.lmtoo.batch.itemwriterdb;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class ItemWriterDbConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<Customer> itemWriterDb() {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("insert into customer(id,firstName,lastName,birthday) values(:id,:firstName,:lastName,:birthday)");
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return writer;
    }


}
