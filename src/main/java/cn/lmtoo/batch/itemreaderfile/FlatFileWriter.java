package cn.lmtoo.batch.itemreaderfile;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("flatFileWriter")
public class FlatFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        for (Customer c : items) {
            System.out.println(c);
        }
    }
}
