package research;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import research.domain.Product;
import research.domain.StatementHistoryEvent;
import research.domain.mysql.ExplainData;
import research.repository.EventHolder;
import research.repository.ProductRepository;
import research.service.SQLStatementStatistics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void statisticsWhere() throws IOException, InterruptedException {
        try {
            var resource = resourceLoader.getResource("classpath:/export.txt");
            var file = resource.getFile();
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(resource.getFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("hello");
            bufferedWriter.append("acasdasddasdasd");

            bufferedWriter.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }


//        sqlStatementStatistics.statisticsWhere();
//        sqlStatementStatistics.getVisualizationDatas().forEach(v -> {
//
//        });
        //sqlStatementStatistics.statisticsWhere();
    }

    @Test
    public void export() {
        sqlStatementStatistics.export();
    }

    @Test
    public void export1() throws Exception {
        sqlStatementStatistics.export01();
    }
}
