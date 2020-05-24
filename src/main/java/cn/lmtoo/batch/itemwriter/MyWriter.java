package cn.lmtoo.batch.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("myWriter")
public class MyWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        System.out.println(items.size());
        for (String str : items) {
            System.out.println(str);
        }

    }
}
