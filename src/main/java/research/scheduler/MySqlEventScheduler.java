package research.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import research.repository.EventHolder;
import research.service.SQLStatementStatistics;

@Component
public class MySqlEventScheduler {

    @Autowired
    EventHolder eventHolder;

    @Autowired
    SQLStatementStatistics sqlStatementStatistics;

    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    public void reportCurrentTime() {
        eventHolder.refresh();
    }

    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    public void minitor() {
       sqlStatementStatistics.statisticsWhere();
    }
}
