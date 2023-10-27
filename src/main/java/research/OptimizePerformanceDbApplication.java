package research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OptimizePerformanceDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimizePerformanceDbApplication.class, args);
	}

}
