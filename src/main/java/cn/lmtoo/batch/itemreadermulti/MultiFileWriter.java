package cn.lmtoo.batch.itemreadermulti;

import cn.lmtoo.batch.itemreaderfile.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("multiFileWriter")
public class MultiFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        for (Customer customer : items) {
            System.out.println(customer);
        }
    }
}
