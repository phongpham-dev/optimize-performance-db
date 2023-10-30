package research;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import research.domain.Product;
import research.domain.StatementHistoryEvent;
import research.domain.mysql.ExplainData;
import research.repository.EventHolder;
import research.repository.ProductRepository;
import research.service.SQLStatementStatistics;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
public class MyTest {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EventHolder eventHolder;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SQLStatementStatistics sqlStatementStatistics;


    @Test
    public void random() {
        double rangeMin = 1;
        double rangeMax = 2000;
        Random random = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        System.out.println(randomValue);
    }


    @Test
    public void select() throws JsonProcessingException {
        var sql = "SELECT * FROM `op-research`.product\n" +
                "WHERE product_line = 'M'";
        entityManager.createNativeQuery(sql);
        var event = eventHolder.getEvent();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(event));
    }

    @Test
    public void statisticsWhere() {
       var rs =  sqlStatementStatistics.getMonitorHtml();
        System.out.println(rs);
        //sqlStatementStatistics.statisticsWhere();
    }
}
