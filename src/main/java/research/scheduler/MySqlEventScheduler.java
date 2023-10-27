package research.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import research.repository.EventHolder;

@Component
public class MySqlEventScheduler {

    @Autowired
    EventHolder eventHolder;

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        eventHolder.refresh();
    }
}
