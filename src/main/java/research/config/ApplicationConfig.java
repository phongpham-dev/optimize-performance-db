package research.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import research.domain.Product;
import research.repository.ProductRepository;

import java.util.Random;
import java.util.stream.IntStream;

@Configuration
public class ApplicationConfig {

    @Autowired
    ProductRepository productRepository;

    @Bean
    public Boolean initData() {
        try {
            System.out.println("init data");
            Random random = new Random();
            String[] sizeList = {"M", "L", "XL"};
            String[] productLineList = {"M", "R", "S", "T"};
            Integer[] categories = {1, 2, 3, 4, 5};
            String[] colors = {"red", "yellow", "blue", "black", "while", "orange"};
            var products = IntStream.range(1, 200000).mapToObj(i -> Product.builder()
                    .name("name " + i)
                    .productNumber("PN-" + i)
                    .makeFlag(random.nextBoolean())
                    .color(colors[random.nextInt(colors.length)])
                    .listPrice(1 + (2000 - 1) * random.nextDouble())
                    .size(sizeList[random.nextInt(sizeList.length)])
                    .weight(1 + (200 - 1) * random.nextDouble())
                    .productLine(productLineList[random.nextInt(productLineList.length)])
                    .productSubcategoryId(categories[random.nextInt(categories.length)])
                    .discontinuedDate(System.currentTimeMillis())
                    .modifiedDate(System.currentTimeMillis())
                    .build()).toList();
            productRepository.saveAll(products);
        } catch (Exception ex) {
            return true;
        }
        return true;
    }
}
