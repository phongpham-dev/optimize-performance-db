package research;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import research.domain.Product;
import research.domain.StatementHistoryEvent;
import research.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
public class MyTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    public void random() {
        double rangeMin = 1;
        double rangeMax = 2000;
        Random random = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        System.out.println(randomValue);
    }

}
