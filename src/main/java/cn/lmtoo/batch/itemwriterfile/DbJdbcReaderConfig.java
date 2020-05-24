package cn.lmtoo.batch.itemwriterfile;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DbJdbcReaderConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<Customer> dbJdbcReaderWriteFile() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(20);
        reader.setRowMapper(BeanPropertyRowMapper.newInstance(Customer.class));

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,firstName,lastName,birthday");
        provider.setFromClause("from customer");

        Map<String, Order> sort = new HashMap<>(1);
        sort.put("id", Order.ASCENDING);

        provider.setSortKeys(sort);

        reader.setQueryProvider(provider);
        return reader;
    }
}
