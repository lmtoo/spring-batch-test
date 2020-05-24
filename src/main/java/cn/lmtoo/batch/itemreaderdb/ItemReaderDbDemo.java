package cn.lmtoo.batch.itemreaderdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ItemReaderDbDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("dbJdbcWriter")
    private ItemWriter<User> dbJdbcWriter;

    @Bean
    public Job itemReaderDbJob() {
        return jobBuilderFactory.get("itemReaderDbJob")
                .start(itemReaderDbStep())
                .build();
    }

    @Bean
    public Step itemReaderDbStep() {
        return stepBuilderFactory.get("itemReaderDbStep")
                .<User, User>chunk(2)
                .reader(dbJdbcReader())
                .writer(dbJdbcWriter)
                .build();
    }

   @Bean
    @StepScope
    public JdbcPagingItemReader<User> dbJdbcReader() {
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(2);
        reader.setRowMapper(BeanPropertyRowMapper.newInstance(User.class));
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,username,password,age");
        provider.setFromClause("from user");

        Map<String, Order> sort = new HashMap<>(1);
        sort.put("id", Order.ASCENDING);

        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);
        return reader;
    }
}
